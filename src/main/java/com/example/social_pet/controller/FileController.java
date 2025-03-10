package com.example.social_pet.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.social_pet.service.FileStorageService;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/files")
@CrossOrigin(origins = "http://localhost:3000")
public class FileController {
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Value("${file.upload-dir}")
    private String uploadDir;
    
    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping("/{fileName:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String fileName) {
        try {
            File file = new File(uploadDir, fileName);
            Path filePath = file.toPath();
            
            logger.info("Trying to access file: " + filePath.toString());
            
            if (!Files.exists(filePath)) {
                logger.error("File not found: " + filePath.toString());
                return ResponseEntity.notFound().build();
            }

            Resource resource = new UrlResource(filePath.toUri());
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            logger.info("File found, serving with content type: " + contentType);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
            
        } catch (Exception ex) {
            logger.error("Error serving file: " + fileName, ex);
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            logger.info("Received file upload request: " + file.getOriginalFilename());
            
            if (file.isEmpty()) {
                logger.error("Failed to upload empty file");
                return ResponseEntity.badRequest().body("Please upload a file");
            }
            
            String fileName = fileStorageService.storeFile(file);
            
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/v1/files/")
                    .path(fileName)
                    .toUriString();
            
            Map<String, String> response = new HashMap<>();
            response.put("fileName", fileName);
            response.put("fileUrl", fileDownloadUri);
            response.put("message", "File uploaded successfully");
            
            logger.info("File uploaded successfully: " + fileName);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            logger.error("Error uploading file", ex);
            return ResponseEntity.badRequest().body("Could not upload the file: " + ex.getMessage());
        }
    }
    
    @PostMapping("/upload-multiple")
    public ResponseEntity<?> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        try {
            logger.info("Received multiple files upload request: " + files.length + " files");
            
            if (files.length == 0) {
                logger.error("No files to upload");
                return ResponseEntity.badRequest().body("Please upload at least one file");
            }
            
            Map<String, Object> response = new HashMap<>();
            Map<String, String> fileDetails = new HashMap<>();
            
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    String fileName = fileStorageService.storeFile(file);
                    String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                            .path("/api/v1/files/")
                            .path(fileName)
                            .toUriString();
                    
                    fileDetails.put(fileName, fileDownloadUri);
                    logger.info("File uploaded successfully: " + fileName);
                }
            }
            
            response.put("uploadedFiles", fileDetails);
            response.put("message", "Files uploaded successfully");
            
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            logger.error("Error uploading files", ex);
            return ResponseEntity.badRequest().body("Could not upload the files: " + ex.getMessage());
        }
    }
}
