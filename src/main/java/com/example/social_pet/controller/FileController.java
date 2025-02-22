package com.example.social_pet.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/v1/files")
@CrossOrigin(origins = "http://localhost:3000")
public class FileController {
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Value("${file.upload-dir}")
    private String uploadDir;

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
}
