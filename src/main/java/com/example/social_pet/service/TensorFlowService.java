package com.example.social_pet.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.springframework.core.io.FileSystemResource;

@Service
public class TensorFlowService {
    private static final Logger logger = LoggerFactory.getLogger(TensorFlowService.class);
    
    private final ResourceLoader resourceLoader;
    private final String LABELS_PATH = "model/labels.txt";
    
    private List<String> labels;
    private AtomicBoolean initialized = new AtomicBoolean(false);
    private Process pythonApiProcess;
    private final RestTemplate restTemplate = new RestTemplate();
    private final String apiUrl = "http://localhost:5012";
    private final int MAX_RETRY_COUNT = 5;
    private final int RETRY_DELAY_MS = 2000;
    
    @Autowired
    public TensorFlowService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
    
    @PostConstruct
    public void init() {
        try {
            // Load labels for backup
            logger.info("Loading dog breed labels...");
            Resource labelsResource = resourceLoader.getResource("classpath:" + LABELS_PATH);
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(labelsResource.getInputStream()))) {
                labels = reader.lines().collect(Collectors.toList());
            }
            logger.info("Loaded {} dog breed labels", labels.size());
            
            // Kill any existing Python processes that might be using the port
            killExistingPythonProcesses();
            
            // Start the Python API
            startPythonApi();
            
            // Wait for API to be healthy with retries
            boolean isHealthy = waitForHealthyApi();
            
            if (isHealthy) {
                initialized.set(true);
                logger.info("Dog breed analyzer initialized with Python API");
            } else {
                logger.error("Failed to verify Python API is running after multiple attempts");
            }
        } catch (Exception e) {
            logger.error("Failed to initialize dog breed analyzer", e);
        }
    }
    
    private void killExistingPythonProcesses() {
        try {
            // Check which OS we're on
            String os = System.getProperty("os.name").toLowerCase();
            
            ProcessBuilder pb;
            if (os.contains("win")) {
                // Windows
                pb = new ProcessBuilder("cmd.exe", "/c", "netstat -ano | findstr :5012 | findstr LISTENING");
            } else {
                // Unix-like
                pb = new ProcessBuilder("bash", "-c", "lsof -i :5012 | grep LISTEN | awk '{print $2}'");
            }
            
            Process p = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    String pid = os.contains("win") 
                        ? line.substring(line.lastIndexOf(" ")).trim() 
                        : line;
                    
                    logger.info("Killing process using port 5012: {}", pid);
                    
                    if (os.contains("win")) {
                        Runtime.getRuntime().exec("taskkill /F /PID " + pid);
                    } else {
                        Runtime.getRuntime().exec("kill -9 " + pid);
                    }
                }
            }
        } catch (Exception e) {
            logger.warn("Failed to kill existing Python processes: {}", e.getMessage());
        }
    }
    
    private void startPythonApi() {
        try {
            logger.info("Starting Python API...");
            
            // Find the absolute path to the Python script
            File currentDir = new File(".");
            String scriptPath = new File(currentDir.getAbsolutePath(), "dog_breed_api.py").getAbsolutePath();
            
            logger.info("Python script path: {}", scriptPath);
            
            // Set environment variables for production
            Map<String, String> env = new HashMap<>(System.getenv());
            env.put("PORT", "5012");
            env.put("GUNICORN_WORKERS", "4");
            
            // Start Python API process with the full path
            ProcessBuilder pb = new ProcessBuilder(
                "python3", 
                scriptPath
            );
            
            // Set environment variables
            pb.environment().putAll(env);
            
            // Set the working directory to the project root
            pb.directory(currentDir);
            
            pb.redirectErrorStream(true);
            pythonApiProcess = pb.start();
            
            // Read output for debugging
            new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(pythonApiProcess.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        logger.info("Python API: {}", line);
                    }
                } catch (IOException e) {
                    logger.error("Error reading Python API output", e);
                }
            }).start();
            
            // Wait a bit for the API to start up
            Thread.sleep(3000);
            
            logger.info("Python API process started");
        } catch (Exception e) {
            logger.error("Failed to start Python API: {}", e.getMessage(), e);
        }
    }
    
    private boolean waitForHealthyApi() {
        for (int attempt = 1; attempt <= MAX_RETRY_COUNT; attempt++) {
            try {
                logger.info("Health check attempt {}/{}", attempt, MAX_RETRY_COUNT);
                
                if (isApiHealthy()) {
                    return true;
                }
                
                if (attempt < MAX_RETRY_COUNT) {
                    logger.info("API not healthy yet, waiting {} ms before retry", RETRY_DELAY_MS);
                    Thread.sleep(RETRY_DELAY_MS);
                }
            } catch (Exception e) {
                logger.warn("Health check attempt {} failed: {}", attempt, e.getMessage());
                try {
                    Thread.sleep(RETRY_DELAY_MS);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        
        return false;
    }
    
    private boolean isApiHealthy() {
        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(apiUrl + "/health", Map.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> healthData = response.getBody();
                logger.info("API health check: {}", healthData);
                return "UP".equals(healthData.get("status"));
            }
            return false;
        } catch (Exception e) {
            logger.warn("API health check failed: {}", e.getMessage());
            return false;
        }
    }
    
    public Map<String, Object> analyzeDogBreed(MultipartFile imageFile) throws IOException {
        logger.info("Analyzing dog breed for image: {}", imageFile.getOriginalFilename());
        
        // Check that initialization was successful
        if (!initialized.get()) {
            logger.error("Dog breed analyzer not initialized properly");
            throw new IOException("Dog breed analyzer not initialized. Please try again later.");
        }
        
        try {
            // Create a temporary file from the MultipartFile
            File tempFile = File.createTempFile("upload-", "-" + imageFile.getOriginalFilename());
            imageFile.transferTo(tempFile);
            
            // Prepare request to Python API
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            
            // Create multipart request
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            
            // Add file to the request using FileSystemResource instead of the MultipartFile resource
            body.add("image", new FileSystemResource(tempFile));
            
            // Create HTTP entity
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            
            // Call API
            ResponseEntity<Map> response = restTemplate.postForEntity(
                apiUrl + "/analyze", 
                requestEntity,
                Map.class
            );
            
            // Delete the temp file after use
            tempFile.delete();
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> result = response.getBody();
                
                // Check if there's an error
                if (result.containsKey("error")) {
                    logger.error("API returned error: {}", result.get("error"));
                    throw new IOException("Error in breed analysis: " + result.get("error"));
                }
                
                logger.info("Successfully determined dog breed: {}", result.get("primaryBreed"));
                return result;
            } else {
                logger.error("Failed to get valid response from API");
                throw new IOException("Failed to get valid response from API");
            }
            
        } catch (Exception e) {
            logger.error("Error analyzing dog breed", e);
            throw new IOException("Failed to analyze dog breed: " + e.getMessage());
        }
    }
} 