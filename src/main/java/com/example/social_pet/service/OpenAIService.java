package com.example.social_pet.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OpenAIService {

    private final RestTemplate restTemplate;

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url:https://api.openai.com/v1/chat/completions}")
    private String apiUrl;

    public OpenAIService() {
        this.restTemplate = new RestTemplate();
    }

    public String analyzeCatBreed(MultipartFile imageFile) throws IOException {
        try {
            // Convert image to base64
            byte[] imageBytes = imageFile.getBytes();
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            // Prepare headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            // Prepare request body
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "gpt-4o");

            // Create messages array with system prompt and user content
            Map<String, Object> systemMessage = new HashMap<>();
            systemMessage.put("role", "system");
            systemMessage.put("content", "Sen bir kedi türü tanımlama uzmanısın. Verilen görüntüyü analiz ederek kedinin türünü belirle. Yalnızca kedi türünün adını ve çok kısa bir açıklamasını ver. Cevabın mutlaka Türkçe olmalıdır. Eğer kedinin türünü belirleyemezsen, 'unknown' yanıtını ver. Eğer görüntüde bir kedi yoksa, 'not_a_cat' yanıtını ver. JSON formatında dönüş yap. ve doğruluk oranını da döndür. Örnek cevap: { 'breed': 'Siyam', 'confidence': 95 }, asla markdown ya da başka format dönme, sadece JSON formatında dönüş yap.");

            Map<String, Object> userMessage = new HashMap<>();
            userMessage.put("role", "user");
            userMessage.put("content", List.of(
                new HashMap<String, Object>() {{
                    put("type", "text");
                    put("text", "What breed is this cat?");
                }},
                new HashMap<String, Object>() {{
                    put("type", "image_url");
                    put("image_url", new HashMap<String, String>() {{
                        put("url", "data:image/jpeg;base64," + base64Image);
                    }});
                }}
            ));

            requestBody.put("messages", List.of(systemMessage, userMessage));
            requestBody.put("max_tokens", 150);

            // Make the API call
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, request, Map.class);

            // Extract and return the response content
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    return (String) message.get("content");
                }
            }
            
            return "Failed to analyze the cat breed. Please try again.";
        } catch (Exception e) {
            throw new IOException("Error analyzing cat breed: " + e.getMessage(), e);
        }
    }
} 