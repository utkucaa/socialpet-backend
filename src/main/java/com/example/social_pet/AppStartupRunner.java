package com.example.social_pet;

import com.example.social_pet.service.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class AppStartupRunner implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(AppStartupRunner.class);
    
    @Value("${file.upload-dir}")
    private String uploadDir;
    
    @Autowired
    private FileStorageService fileStorageService;
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("Application started, initializing upload directory and placeholder images");
        
        // Ensure upload directory exists
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            logger.info("Creating upload directory: " + uploadPath);
            Files.createDirectories(uploadPath);
        }
        
        // Create placeholder image if it doesn't exist
        Path placeholderPath = uploadPath.resolve("placeholder.jpg");
        if (!Files.exists(placeholderPath)) {
            logger.info("Creating placeholder image at: " + placeholderPath);
            fileStorageService.createBasicPlaceholderImage(placeholderPath);
        }
        
        logger.info("Initialization complete");
    }
} 