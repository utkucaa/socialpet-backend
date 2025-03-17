package com.example.social_pet.controller;

import com.example.social_pet.service.OpenAIService;
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
@RequestMapping("/api/v1/breed-analyzer")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class BreedAnalyzerController {

    private final OpenAIService openAIService;

    @Autowired
    public BreedAnalyzerController(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    @PostMapping(value = "/analyze", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> analyzeCatBreed(@RequestParam("image") MultipartFile imageFile) {
        try {
            // Check if file is empty
            if (imageFile.isEmpty()) {
                return ResponseEntity.badRequest().body(createErrorResponse("Please upload an image file"));
            }

            // Check if file is an image
            String contentType = imageFile.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.badRequest().body(createErrorResponse("Please upload a valid image file"));
            }

            // Process the image with GPT-4o
            String analysisResult = openAIService.analyzeCatBreed(imageFile);
            
            Map<String, String> response = new HashMap<>();
            response.put("result", analysisResult);
            
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error processing image: " + e.getMessage()));
        }
    }

    private Map<String, String> createErrorResponse(String message) {
        Map<String, String> response = new HashMap<>();
        response.put("error", message);
        return response;
    }
} 