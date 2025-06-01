package com.example.social_pet.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {
    private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    public String storeFile(MultipartFile file) {
        try {
            // Create the upload directory if it doesn't exist
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                logger.info("Created upload directory: {}", uploadPath);
            }

            // Generate a unique file name
            String originalFileName = file.getOriginalFilename();
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String fileName = UUID.randomUUID().toString() + fileExtension;

            // Copy the file to the upload directory
            Path targetLocation = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            logger.info("Stored file: {} at: {}", fileName, targetLocation);

            return fileName;
        } catch (IOException ex) {
            logger.error("Could not store file: {}", file.getOriginalFilename(), ex);
            throw new RuntimeException("Could not store file. Please try again!", ex);
        }
    }
    
    /**
     * Creates a basic placeholder image when a requested image is not found
     * @param path The path where to save the placeholder image
     * @throws IOException If an error occurs during image creation
     */
    public void createBasicPlaceholderImage(Path path) throws IOException {
        logger.info("Creating placeholder image at: {}", path);
        int width = 300;
        int height = 300;
        
        // Create a buffered image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        
        // Fill background with light gray
        g.setColor(new Color(240, 240, 240));
        g.fillRect(0, 0, width, height);
        
        // Draw a frame
        g.setColor(new Color(200, 200, 200));
        g.drawRect(10, 10, width - 20, height - 20);
        
        // Draw text
        g.setColor(Color.DARK_GRAY);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        FontMetrics fm = g.getFontMetrics();
        String text = "Image Not Found";
        int textWidth = fm.stringWidth(text);
        g.drawString(text, (width - textWidth) / 2, height / 2);
        
        g.dispose();
        
        // Write the image to the file
        ImageIO.write(image, "jpg", path.toFile());
        logger.info("Placeholder image created successfully");
    }
} 