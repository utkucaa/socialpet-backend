package com.example.social_pet.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.social_pet.service.GooglePlacesService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/places")
@CrossOrigin(origins = "http://localhost:3000")
public class GooglePlacesController {
    private static final Logger logger = LoggerFactory.getLogger(GooglePlacesController.class);
    
    @Autowired
    private GooglePlacesService googlePlacesService;
    
    @Value("${google.places.api.key}")
    private String apiKey;
    
    /**
     * Test endpoint to verify Google Places API integration
     */
    @GetMapping("/test")
    public ResponseEntity<Map<String, String>> testGooglePlacesApi() {
        logger.info("Testing Google Places API integration");
        
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Google Places API integration is working");
        response.put("apiKey", apiKey.substring(0, 5) + "..." + apiKey.substring(apiKey.length() - 5));
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Search for nearby places based on location and type
     * 
     * @param latitude The latitude coordinate
     * @param longitude The longitude coordinate
     * @param radius The search radius in meters (max 50000)
     * @param type The type of place (e.g., "veterinary_care", "pet_store")
     * @return ResponseEntity containing the Google Places API response
     */
    @GetMapping("/nearby")
    public ResponseEntity<String> searchNearbyPlaces(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(defaultValue = "5000") int radius,
            @RequestParam(defaultValue = "veterinary_care") String type) {
        
        logger.info("Received request to search for nearby places of type {} at location {},{} with radius {}m", 
                type, latitude, longitude, radius);
        
        return googlePlacesService.searchNearbyPlaces(latitude, longitude, radius, type);
    }
    
    /**
     * Search for nearby places based on city, district and type
     * 
     * @param city The city name
     * @param district The district name (optional)
     * @param radius The search radius in meters (max 50000)
     * @param type The type of place (e.g., "veterinary_care", "pet_store")
     * @return ResponseEntity containing the Google Places API response
     */
    @GetMapping("/nearby-by-location")
    public ResponseEntity<String> searchNearbyPlacesByLocation(
            @RequestParam String city,
            @RequestParam(required = false) String district,
            @RequestParam(defaultValue = "5000") int radius,
            @RequestParam(defaultValue = "veterinary_care") String type) {
        
        logger.info("Received request to search for nearby places of type {} in {}, {} with radius {}m", 
                type, district, city, radius);
        
        return googlePlacesService.searchNearbyPlacesByLocation(city, district, radius, type);
    }
    
    /**
     * Search for pet-related places (veterinarians, pet stores, etc.) in a specific area
     * 
     * @param latitude The latitude coordinate
     * @param longitude The longitude coordinate
     * @param radius The search radius in meters (max 50000)
     * @param keyword Optional keyword to further filter results (e.g., "dog", "cat")
     * @return ResponseEntity containing the Google Places API response
     */
    @GetMapping("/pet-places")
    public ResponseEntity<String> searchPetRelatedPlaces(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(defaultValue = "5000") int radius,
            @RequestParam(required = false) String keyword) {
        
        logger.info("Received request to search for pet-related places at location {},{} with radius {}m and keyword: {}", 
                latitude, longitude, radius, keyword);
        
        return googlePlacesService.searchPetRelatedPlaces(latitude, longitude, radius, keyword);
    }
    
    /**
     * Search for pet-related places based on city and district
     * 
     * @param city The city name
     * @param district The district name (optional)
     * @param radius The search radius in meters (max 50000)
     * @param keyword Optional keyword to further filter results (e.g., "dog", "cat")
     * @return ResponseEntity containing the Google Places API response
     */
    @GetMapping("/pet-places-by-location")
    public ResponseEntity<String> searchPetRelatedPlacesByLocation(
            @RequestParam String city,
            @RequestParam(required = false) String district,
            @RequestParam(defaultValue = "5000") int radius,
            @RequestParam(required = false) String keyword) {
        
        logger.info("Received request to search for pet-related places in {}, {} with radius {}m and keyword: {}", 
                district, city, radius, keyword);
        
        return googlePlacesService.searchPetRelatedPlacesByLocation(city, district, radius, keyword);
    }
    
    /**
     * Get details for a specific place by its place_id
     * 
     * @param placeId The Google Place ID
     * @return ResponseEntity containing the Google Places API response
     */
    @GetMapping("/details")
    public ResponseEntity<String> getPlaceDetails(@RequestParam String placeId) {
        logger.info("Received request to get details for place with ID: {}", placeId);
        
        return googlePlacesService.getPlaceDetails(placeId);
    }
    
    /**
     * Get autocomplete suggestions for a search query
     * 
     * @param input The search query
     * @param latitude Optional latitude for location bias
     * @param longitude Optional longitude for location bias
     * @param radius Optional radius for location bias in meters
     * @return ResponseEntity containing the Google Places API response
     */
    @GetMapping("/autocomplete")
    public ResponseEntity<String> getAutocompleteSuggestions(
            @RequestParam String input,
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude,
            @RequestParam(required = false) Integer radius) {
        
        logger.info("Received request for autocomplete suggestions for: {}", input);
        
        return googlePlacesService.getAutocompleteSuggestions(input, latitude, longitude, radius);
    }
    
    /**
     * Get autocomplete suggestions for a search query with city and district
     * 
     * @param input The search query
     * @param city The city name
     * @param district The district name (optional)
     * @param radius Optional radius for location bias in meters
     * @return ResponseEntity containing the Google Places API response
     */
    @GetMapping("/autocomplete-by-location")
    public ResponseEntity<String> getAutocompleteSuggestionsByLocation(
            @RequestParam String input,
            @RequestParam String city,
            @RequestParam(required = false) String district,
            @RequestParam(required = false) Integer radius) {
        
        logger.info("Received request for autocomplete suggestions for: {} in {}, {}", input, district, city);
        
        return googlePlacesService.getAutocompleteSuggestionsByLocation(input, city, district, radius);
    }
    
    /**
     * Perform a text search for places
     * 
     * @param query The search query
     * @param latitude Optional latitude for location bias
     * @param longitude Optional longitude for location bias
     * @param radius Optional radius for location bias in meters
     * @return ResponseEntity containing the Google Places API response
     */
    @GetMapping("/search")
    public ResponseEntity<String> textSearchPlaces(
            @RequestParam String query,
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude,
            @RequestParam(required = false) Integer radius) {
        
        logger.info("Received request for text search: {}", query);
        
        return googlePlacesService.textSearchPlaces(query, latitude, longitude, radius);
    }
    
    /**
     * Perform a text search for places with city and district
     * 
     * @param query The search query
     * @param city The city name
     * @param district The district name (optional)
     * @param radius Optional radius for location bias in meters
     * @return ResponseEntity containing the Google Places API response
     */
    @GetMapping("/search-by-location")
    public ResponseEntity<String> textSearchPlacesByLocation(
            @RequestParam String query,
            @RequestParam String city,
            @RequestParam(required = false) String district,
            @RequestParam(required = false) Integer radius) {
        
        logger.info("Received request for text search: {} in {}, {}", query, district, city);
        
        return googlePlacesService.textSearchPlacesByLocation(query, city, district, radius);
    }
} 