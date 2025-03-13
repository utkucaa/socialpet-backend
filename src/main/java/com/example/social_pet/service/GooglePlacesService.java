package com.example.social_pet.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class GooglePlacesService {
    private static final Logger logger = LoggerFactory.getLogger(GooglePlacesService.class);
    
    @Value("${google.places.api.key}")
    private String apiKey;
    
    private final RestTemplate restTemplate;
    
    // Base URLs for different Google Places API endpoints
    private static final String PLACES_NEARBY_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";
    private static final String PLACES_DETAILS_URL = "https://maps.googleapis.com/maps/api/place/details/json";
    private static final String PLACES_AUTOCOMPLETE_URL = "https://maps.googleapis.com/maps/api/place/autocomplete/json";
    private static final String PLACES_TEXT_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/textsearch/json";
    private static final String GEOCODING_URL = "https://maps.googleapis.com/maps/api/geocode/json";
    
    // List of pet-related place types
    private static final List<String> PET_RELATED_TYPES = Arrays.asList(
            "veterinary_care", 
            "pet_store", 
            "zoo", 
            "park", 
            "animal_shelter"
    );
    
    public GooglePlacesService() {
        this.restTemplate = new RestTemplate();
    }
    
    /**
     * Convert city and district names to coordinates using Google Geocoding API
     * 
     * @param city The city name
     * @param district The district name (optional)
     * @return Map containing latitude and longitude
     */
    public Map<String, Double> geocodeLocation(String city, String district) {
        logger.info("Geocoding location: city={}, district={}", city, district);
        
        String address = district != null ? district + ", " + city + ", Turkey" : city + ", Turkey";
        
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(GEOCODING_URL)
                .queryParam("address", address)
                .queryParam("key", apiKey);
        
        String url = builder.toUriString();
        logger.debug("Geocoding Request URL: {}", url);
        
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        Map<String, Object> responseBody = response.getBody();
        
        if (responseBody != null && "OK".equals(responseBody.get("status"))) {
            List<Map<String, Object>> results = (List<Map<String, Object>>) responseBody.get("results");
            if (!results.isEmpty()) {
                Map<String, Object> location = (Map<String, Object>) 
                        ((Map<String, Object>) results.get(0).get("geometry")).get("location");
                
                Double lat = (Double) location.get("lat");
                Double lng = (Double) location.get("lng");
                
                logger.info("Geocoded coordinates: lat={}, lng={}", lat, lng);
                return Map.of("latitude", lat, "longitude", lng);
            }
        }
        
        logger.warn("Failed to geocode address: {}", address);
        return Map.of("latitude", 41.0082, "longitude", 28.9784); // Default to Istanbul coordinates
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
    public ResponseEntity<String> searchNearbyPlaces(double latitude, double longitude, int radius, String type) {
        logger.info("Searching for nearby places of type {} at location {},{} with radius {}m", 
                type, latitude, longitude, radius);
        
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(PLACES_NEARBY_URL)
                .queryParam("location", latitude + "," + longitude)
                .queryParam("radius", radius)
                .queryParam("type", type)
                .queryParam("key", apiKey);
        
        String url = builder.toUriString();
        logger.debug("Request URL: {}", url);
        logger.debug("Request URL: {}", url);
        return restTemplate.getForEntity(url, String.class);
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
    public ResponseEntity<String> searchNearbyPlacesByLocation(String city, String district, int radius, String type) {
        logger.info("Searching for nearby places of type {} in {}, {} with radius {}m", 
                type, district, city, radius);
        
        Map<String, Double> coordinates = geocodeLocation(city, district);
        return searchNearbyPlaces(coordinates.get("latitude"), coordinates.get("longitude"), radius, type);
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
    public ResponseEntity<String> searchPetRelatedPlaces(double latitude, double longitude, int radius, String keyword) {
        logger.info("Searching for pet-related places at location {},{} with radius {}m and keyword: {}", 
                latitude, longitude, radius, keyword);
        
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(PLACES_TEXT_SEARCH_URL)
                .queryParam("location", latitude + "," + longitude)
                .queryParam("radius", radius)
                .queryParam("key", apiKey);
        
        // Build a query that combines the keyword with pet-related terms
        StringBuilder queryBuilder = new StringBuilder();
        if (keyword != null && !keyword.trim().isEmpty()) {
            queryBuilder.append(keyword).append(" ");
        }
        queryBuilder.append("pet veterinary animal");
        
        builder.queryParam("query", queryBuilder.toString());
        
        String url = builder.toUriString();
        logger.debug("Request URL: {}", url);
        
        return restTemplate.getForEntity(url, String.class);
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
    public ResponseEntity<String> searchPetRelatedPlacesByLocation(String city, String district, int radius, String keyword) {
        logger.info("Searching for pet-related places in {}, {} with radius {}m and keyword: {}", 
                district, city, radius, keyword);
        
        Map<String, Double> coordinates = geocodeLocation(city, district);
        return searchPetRelatedPlaces(coordinates.get("latitude"), coordinates.get("longitude"), radius, keyword);
    }
    
    /**
     * Get details for a specific place by its place_id
     * 
     * @param placeId The Google Place ID
     * @return ResponseEntity containing the Google Places API response
     */
    public ResponseEntity<String> getPlaceDetails(String placeId) {
        logger.info("Getting details for place with ID: {}", placeId);
        
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(PLACES_DETAILS_URL)
                .queryParam("place_id", placeId)
                .queryParam("key", apiKey);
        
        String url = builder.toUriString();
        logger.debug("Request URL: {}", url);
        
        return restTemplate.getForEntity(url, String.class);
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
    public ResponseEntity<String> getAutocompleteSuggestions(String input, Double latitude, Double longitude, Integer radius) {
        logger.info("Getting autocomplete suggestions for: {}", input);
        
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(PLACES_AUTOCOMPLETE_URL)
                .queryParam("input", input)
                .queryParam("key", apiKey);
        
        // Add location bias if coordinates are provided
        if (latitude != null && longitude != null) {
            builder.queryParam("location", latitude + "," + longitude);
            
            if (radius != null) {
                builder.queryParam("radius", radius);
            }
        }
        
        String url = builder.toUriString();
        logger.debug("Request URL: {}", url);
        
        return restTemplate.getForEntity(url, String.class);
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
    public ResponseEntity<String> getAutocompleteSuggestionsByLocation(String input, String city, String district, Integer radius) {
        logger.info("Getting autocomplete suggestions for: {} in {}, {}", input, district, city);
        
        Map<String, Double> coordinates = geocodeLocation(city, district);
        return getAutocompleteSuggestions(input, coordinates.get("latitude"), coordinates.get("longitude"), radius);
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
    public ResponseEntity<String> textSearchPlaces(String query, Double latitude, Double longitude, Integer radius) {
        logger.info("Performing text search for: {}", query);
        
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(PLACES_TEXT_SEARCH_URL)
                .queryParam("query", query)
                .queryParam("key", apiKey);
        
        // Add location bias if coordinates are provided
        if (latitude != null && longitude != null) {
            builder.queryParam("location", latitude + "," + longitude);
            
            if (radius != null) {
                builder.queryParam("radius", radius);
            }
        }
        
        String url = builder.toUriString();
        logger.debug("Request URL: {}", url);
        
        return restTemplate.getForEntity(url, String.class);
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
    public ResponseEntity<String> textSearchPlacesByLocation(String query, String city, String district, Integer radius) {
        logger.info("Performing text search for: {} in {}, {}", query, district, city);
        
        Map<String, Double> coordinates = geocodeLocation(city, district);
        return textSearchPlaces(query, coordinates.get("latitude"), coordinates.get("longitude"), radius);
    }
} 