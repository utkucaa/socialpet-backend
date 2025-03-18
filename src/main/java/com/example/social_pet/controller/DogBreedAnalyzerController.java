package com.example.social_pet.controller;

import com.example.social_pet.service.TensorFlowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/dog-breed-analyzer")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class DogBreedAnalyzerController {

    private static final Logger logger = LoggerFactory.getLogger(DogBreedAnalyzerController.class);
    private final TensorFlowService tensorFlowService;

    @Autowired
    public DogBreedAnalyzerController(TensorFlowService tensorFlowService) {
        this.tensorFlowService = tensorFlowService;
        logger.info("DogBreedAnalyzerController initialized with TensorFlowService: {}", 
                   tensorFlowService != null ? "available" : "not available");
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "Dog breed analyzer service is operational");
        logger.info("Health check endpoint called - service is up");
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/analyze-dog", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> analyzeDogBreed(@RequestParam("image") MultipartFile imageFile) {
        logger.info("Received dog breed analysis request for file: {}", 
                   imageFile != null ? imageFile.getOriginalFilename() : "null");
        
        try {
            // Check if file is empty or null
            if (imageFile == null || imageFile.isEmpty()) {
                logger.warn("Empty or null file uploaded");
                return ResponseEntity.badRequest().body(createErrorResponse("Please upload an image file"));
            }

            // Check if file is an image
            String contentType = imageFile.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                logger.warn("Invalid file type uploaded: {}", contentType);
                return ResponseEntity.badRequest().body(createErrorResponse("Please upload a valid image file"));
            }

            // Log file details
            logger.info("Processing image file: name={}, size={}, type={}", 
                      imageFile.getOriginalFilename(), 
                      imageFile.getSize(), 
                      imageFile.getContentType());

            // Process the image with TensorFlow model
            Map<String, Object> analysisResult = tensorFlowService.analyzeDogBreed(imageFile);
            
            logger.info("Analysis complete. Identified primary breed: {}", analysisResult.get("primaryBreed"));
            
            return ResponseEntity.ok(analysisResult);
        } catch (IOException e) {
            logger.error("Error processing image: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error processing image: " + e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected error during dog breed analysis: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("An unexpected error occurred: " + e.getMessage()));
        }
    }

    private Map<String, String> createErrorResponse(String message) {
        Map<String, String> response = new HashMap<>();
        response.put("error", message);
        return response;
    }
} 