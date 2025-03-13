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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import java.util.HashMap;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.ObjectMapper;

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
    private static final String PLACES_PHOTO_URL = "https://maps.googleapis.com/maps/api/place/photo";
    
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
        
        this.restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().set("Cache-Control", "no-cache, no-store, must-revalidate");
            request.getHeaders().set("Pragma", "no-cache");
            request.getHeaders().set("Expires", "0");
            return execution.execute(request, body);
        });
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
        
        // Şehir ve ilçe adlarını normalize et (küçük harfe çevir, boşlukları temizle)
        String normalizedCity = city != null ? city.toLowerCase().trim() : "";
        String normalizedDistrict = district != null ? district.toLowerCase().trim() : "";
        
        // İl-ilçe kombinasyonları için sabit koordinatlar
        Map<String, Map<String, double[]>> cityDistrictCoordinates = new HashMap<>();
        
        // Konya ve ilçeleri
        Map<String, double[]> konyaDistricts = new HashMap<>();
        konyaDistricts.put("çumra", new double[] {37.5667, 32.7667});
        konyaDistricts.put("karatay", new double[] {37.8833, 32.6333});
        konyaDistricts.put("meram", new double[] {37.8500, 32.4667});
        konyaDistricts.put("selçuklu", new double[] {37.9333, 32.5167});
        konyaDistricts.put("ereğli", new double[] {37.5133, 34.0517});
        konyaDistricts.put("akşehir", new double[] {38.3569, 31.4164});
        konyaDistricts.put("beyşehir", new double[] {37.6772, 31.7222});
        konyaDistricts.put("cihanbeyli", new double[] {38.6500, 32.9333});
        konyaDistricts.put("ilgın", new double[] {38.2778, 31.9167});
        konyaDistricts.put("kulu", new double[] {39.0944, 33.0806});
        konyaDistricts.put("seydişehir", new double[] {37.4194, 31.8486});
        konyaDistricts.put("", new double[] {37.8667, 32.4833}); // Konya merkez
        cityDistrictCoordinates.put("konya", konyaDistricts);
        
        // Kayseri ve ilçeleri
        Map<String, double[]> kayseriDistricts = new HashMap<>();
        kayseriDistricts.put("melikgazi", new double[] {38.7333, 35.4833});
        kayseriDistricts.put("kocasinan", new double[] {38.7500, 35.4667});
        kayseriDistricts.put("talas", new double[] {38.6833, 35.5500});
        kayseriDistricts.put("develi", new double[] {38.3833, 35.4833});
        kayseriDistricts.put("yahyalı", new double[] {38.0833, 35.3500});
        kayseriDistricts.put("bünyan", new double[] {38.8500, 35.8667});
        kayseriDistricts.put("", new double[] {38.7205, 35.4826}); // Kayseri merkez
        cityDistrictCoordinates.put("kayseri", kayseriDistricts);
        
        // İstanbul ve ilçeleri
        Map<String, double[]> istanbulDistricts = new HashMap<>();
        istanbulDistricts.put("kadıköy", new double[] {40.9833, 29.0667});
        istanbulDistricts.put("beşiktaş", new double[] {41.0500, 29.0100});
        istanbulDistricts.put("şişli", new double[] {41.0667, 28.9833});
        istanbulDistricts.put("beyoğlu", new double[] {41.0333, 28.9833});
        istanbulDistricts.put("fatih", new double[] {41.0167, 28.9500});
        istanbulDistricts.put("üsküdar", new double[] {41.0333, 29.0333});
        istanbulDistricts.put("ataşehir", new double[] {40.9833, 29.1167});
        istanbulDistricts.put("maltepe", new double[] {40.9333, 29.1500});
        istanbulDistricts.put("pendik", new double[] {40.8833, 29.2500});
        istanbulDistricts.put("kartal", new double[] {40.9000, 29.1833});
        istanbulDistricts.put("bakırköy", new double[] {40.9833, 28.8500});
        istanbulDistricts.put("bahçelievler", new double[] {41.0000, 28.8333});
        istanbulDistricts.put("bağcılar", new double[] {41.0333, 28.8500});
        istanbulDistricts.put("esenler", new double[] {41.0500, 28.8833});
        istanbulDistricts.put("eyüp", new double[] {41.0500, 28.9333});
        istanbulDistricts.put("sarıyer", new double[] {41.1667, 29.0500});
        istanbulDistricts.put("beylikdüzü", new double[] {41.0167, 28.6333});
        istanbulDistricts.put("esenyurt", new double[] {41.0333, 28.6833});
        istanbulDistricts.put("arnavutköy", new double[] {41.1833, 28.7333});
        istanbulDistricts.put("başakşehir", new double[] {41.0833, 28.8000});
        istanbulDistricts.put("sultanbeyli", new double[] {40.9667, 29.2667});
        istanbulDistricts.put("sancaktepe", new double[] {41.0000, 29.2333});
        istanbulDistricts.put("çekmeköy", new double[] {41.0333, 29.1833});
        istanbulDistricts.put("tuzla", new double[] {40.8167, 29.3000});
        istanbulDistricts.put("büyükçekmece", new double[] {41.0167, 28.5833});
        istanbulDistricts.put("silivri", new double[] {41.0833, 28.2500});
        istanbulDistricts.put("çatalca", new double[] {41.1500, 28.4667});
        istanbulDistricts.put("şile", new double[] {41.1833, 29.6167});
        istanbulDistricts.put("adalar", new double[] {40.8667, 29.0833});
        istanbulDistricts.put("", new double[] {41.0082, 28.9784}); // İstanbul merkez
        cityDistrictCoordinates.put("istanbul", istanbulDistricts);
        
        // Ankara ve ilçeleri
        Map<String, double[]> ankaraDistricts = new HashMap<>();
        ankaraDistricts.put("çankaya", new double[] {39.9000, 32.8667});
        ankaraDistricts.put("keçiören", new double[] {39.9833, 32.8667});
        ankaraDistricts.put("yenimahalle", new double[] {39.9667, 32.8167});
        ankaraDistricts.put("mamak", new double[] {39.9333, 32.9000});
        ankaraDistricts.put("etimesgut", new double[] {39.9500, 32.6833});
        ankaraDistricts.put("sincan", new double[] {39.9667, 32.5833});
        ankaraDistricts.put("altındağ", new double[] {39.9500, 32.8667});
        ankaraDistricts.put("pursaklar", new double[] {40.0333, 32.8833});
        ankaraDistricts.put("gölbaşı", new double[] {39.8167, 32.8000});
        ankaraDistricts.put("polatlı", new double[] {39.5833, 32.1500});
        ankaraDistricts.put("beypazarı", new double[] {40.1667, 31.9167});
        ankaraDistricts.put("", new double[] {39.9334, 32.8597}); // Ankara merkez
        cityDistrictCoordinates.put("ankara", ankaraDistricts);
        
        // İzmir ve ilçeleri
        Map<String, double[]> izmirDistricts = new HashMap<>();
        izmirDistricts.put("konak", new double[] {38.4167, 27.1333});
        izmirDistricts.put("karşıyaka", new double[] {38.4667, 27.1167});
        izmirDistricts.put("bornova", new double[] {38.4667, 27.2167});
        izmirDistricts.put("buca", new double[] {38.3833, 27.1833});
        izmirDistricts.put("çiğli", new double[] {38.5000, 27.0833});
        izmirDistricts.put("gaziemir", new double[] {38.3167, 27.1333});
        izmirDistricts.put("bayraklı", new double[] {38.4667, 27.1500});
        izmirDistricts.put("karabağlar", new double[] {38.3833, 27.1333});
        izmirDistricts.put("balçova", new double[] {38.3833, 27.0667});
        izmirDistricts.put("narlıdere", new double[] {38.4000, 27.0333});
        izmirDistricts.put("güzelbahçe", new double[] {38.3667, 26.8833});
        izmirDistricts.put("urla", new double[] {38.3167, 26.7667});
        izmirDistricts.put("çeşme", new double[] {38.3167, 26.3000});
        izmirDistricts.put("seferihisar", new double[] {38.2000, 26.8333});
        izmirDistricts.put("menderes", new double[] {38.2500, 27.1333});
        izmirDistricts.put("torbalı", new double[] {38.1667, 27.3667});
        izmirDistricts.put("kemalpaşa", new double[] {38.4333, 27.4167});
        izmirDistricts.put("menemen", new double[] {38.6000, 27.0667});
        izmirDistricts.put("aliağa", new double[] {38.8000, 26.9667});
        izmirDistricts.put("foça", new double[] {38.6667, 26.7500});
        izmirDistricts.put("bergama", new double[] {39.1167, 27.1833});
        izmirDistricts.put("dikili", new double[] {39.0667, 26.8833});
        izmirDistricts.put("kınık", new double[] {39.0833, 27.3833});
        izmirDistricts.put("kiraz", new double[] {38.2333, 28.2000});
        izmirDistricts.put("beydağ", new double[] {38.0833, 28.2000});
        izmirDistricts.put("ödemiş", new double[] {38.2167, 27.9667});
        izmirDistricts.put("tire", new double[] {38.0833, 27.7333});
        izmirDistricts.put("bayındır", new double[] {38.2167, 27.6500});
        izmirDistricts.put("selçuk", new double[] {37.9500, 27.3667});
        izmirDistricts.put("", new double[] {38.4192, 27.1287}); // İzmir merkez
        cityDistrictCoordinates.put("izmir", izmirDistricts);
        
        // Antalya ve ilçeleri
        Map<String, double[]> antalyaDistricts = new HashMap<>();
        antalyaDistricts.put("muratpaşa", new double[] {36.8833, 30.7000});
        antalyaDistricts.put("konyaaltı", new double[] {36.8667, 30.6333});
        antalyaDistricts.put("kepez", new double[] {36.9333, 30.7167});
        antalyaDistricts.put("aksu", new double[] {36.9333, 30.8333});
        antalyaDistricts.put("döşemealtı", new double[] {37.0000, 30.6667});
        antalyaDistricts.put("serik", new double[] {36.9167, 31.1000});
        antalyaDistricts.put("manavgat", new double[] {36.7833, 31.4333});
        antalyaDistricts.put("alanya", new double[] {36.5500, 32.0000});
        antalyaDistricts.put("gazipaşa", new double[] {36.2667, 32.3333});
        antalyaDistricts.put("kumluca", new double[] {36.3667, 30.2833});
        antalyaDistricts.put("finike", new double[] {36.3000, 30.1500});
        antalyaDistricts.put("demre", new double[] {36.2500, 29.9833});
        antalyaDistricts.put("kaş", new double[] {36.2000, 29.6333});
        antalyaDistricts.put("kemer", new double[] {36.6000, 30.5500});
        antalyaDistricts.put("korkuteli", new double[] {37.0667, 30.1667});
        antalyaDistricts.put("elmalı", new double[] {36.7333, 29.9167});
        antalyaDistricts.put("akseki", new double[] {37.0500, 31.7833});
        antalyaDistricts.put("gündoğmuş", new double[] {36.8167, 32.0167});
        antalyaDistricts.put("ibradı", new double[] {37.1000, 31.6000});
        antalyaDistricts.put("", new double[] {36.8969, 30.7133}); // Antalya merkez
        cityDistrictCoordinates.put("antalya", antalyaDistricts);
        
        // İl-ilçe kombinasyonu için sabit koordinat kontrolü
        if (cityDistrictCoordinates.containsKey(normalizedCity)) {
            Map<String, double[]> districtCoords = cityDistrictCoordinates.get(normalizedCity);
            
            // İlçe belirtilmişse ve bu ilçe için koordinat varsa
            if (normalizedDistrict != null && !normalizedDistrict.isEmpty() && districtCoords.containsKey(normalizedDistrict)) {
                double[] coords = districtCoords.get(normalizedDistrict);
                logger.info("Using predefined coordinates for {}, {}: {},{}", city, district, coords[0], coords[1]);
                return Map.of("latitude", coords[0], "longitude", coords[1]);
            } 
            // İlçe belirtilmemişse veya belirtilen ilçe için koordinat yoksa, il merkezini kullan
            else if (districtCoords.containsKey("")) {
                double[] coords = districtCoords.get("");
                logger.info("Using predefined city center coordinates for {}: {},{}", city, coords[0], coords[1]);
                return Map.of("latitude", coords[0], "longitude", coords[1]);
            }
        }
        
        // Sabit koordinatlarda bulunamadıysa, Google Geocoding API'yi dene
        try {
            // Adres oluştur - ilçe varsa "ilçe, il, Türkiye", yoksa "il, Türkiye" formatında
            String address = district != null && !district.isEmpty() 
                ? district + ", " + city + ", Turkey" 
                : city + ", Turkey";
            
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(GEOCODING_URL)
                    .queryParam("address", address)
                    .queryParam("key", apiKey)
                    .queryParam("language", "tr")  // Türkçe sonuçlar için
                    .queryParam("region", "tr")    // Türkiye bölgesi için
                    .queryParam("components", "country:TR") // Sadece Türkiye'deki sonuçları getir
                    .queryParam("timestamp", System.currentTimeMillis()); // Önbelleğe almayı engelle
            
            String url = builder.toUriString();
            logger.debug("Geocoding Request URL: {}", url);
            
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            Map<String, Object> responseBody = response.getBody();
            
            if (responseBody != null && "OK".equals(responseBody.get("status"))) {
                List<Map<String, Object>> results = (List<Map<String, Object>>) responseBody.get("results");
                if (!results.isEmpty()) {
                    // İlk sonucu al
                    Map<String, Object> result = results.get(0);
                    
                    // İlçe belirtilmişse, sonuçlar arasında ilçe adını içeren bir sonuç ara
                    if (district != null && !district.isEmpty()) {
                        for (Map<String, Object> r : results) {
                            String formattedAddress = (String) r.get("formatted_address");
                            if (formattedAddress != null && 
                                formattedAddress.toLowerCase().contains(district.toLowerCase())) {
                                result = r;
                                break;
                            }
                        }
                    }
                    
                    Map<String, Object> geometry = (Map<String, Object>) result.get("geometry");
                    if (geometry != null) {
                        Map<String, Object> location = (Map<String, Object>) geometry.get("location");
                        if (location != null) {
                            Double lat = (Double) location.get("lat");
                            Double lng = (Double) location.get("lng");
                            logger.info("Geocoded coordinates for {}, {}: lat={}, lng={}", 
                                    city, district != null ? district : "", lat, lng);
                            return Map.of("latitude", lat, "longitude", lng);
                        }
                    }
                }
            } else {
                logger.warn("Failed to geocode address: {}. Status: {}", address, 
                        responseBody != null ? responseBody.get("status") : "unknown");
            }
        } catch (Exception e) {
            logger.error("Error during geocoding: ", e);
        }
        
        // Özel durum: Konya Çumra için sabit koordinatlar (API başarısız olursa)
        if ("konya".equalsIgnoreCase(normalizedCity) && "çumra".equalsIgnoreCase(normalizedDistrict)) {
            logger.info("Fallback to hardcoded coordinates for Konya, Çumra");
            return Map.of("latitude", 37.5667, "longitude", 32.7667);
        }
        
        // Özel durum: Konya için sabit koordinatlar (API başarısız olursa)
        if ("konya".equalsIgnoreCase(normalizedCity)) {
            logger.info("Fallback to hardcoded coordinates for Konya");
            return Map.of("latitude", 37.8667, "longitude", 32.4833);
        }
        
        // Hata durumunda Türkiye merkezine yakın koordinatlar döndürülüyor
        logger.warn("Failed to get coordinates for {}, {}. Using Turkey center coordinates as fallback.", 
                city, district != null ? district : "");
        return Map.of("latitude", 39.0, "longitude", 35.0);
    }

    /**
     * Search for nearby places based on location and type
     * 
     * @param latitude The latitude coordinate
     * @param longitude The longitude coordinate
     * @param radius The search radius in meters (max 50000)
     * @param type The type of place (e.g., "veterinary_care", "pet_store")
     * @param openNow Whether to return only places that are open now
     * @return ResponseEntity containing the Google Places API response
     */
    public ResponseEntity<String> searchNearbyPlaces(double latitude, double longitude, int radius, String type, boolean openNow) {
        logger.info("Searching for nearby places of type {} at location {},{} with radius {}m, openNow: {}", 
                type, latitude, longitude, radius, openNow);
        
        // Radius kontrolü - 50000 (50km) Google Places API'nin izin verdiği maksimum değer
        if (radius > 50000) {
            radius = 50000;
        } else if (radius < 1000) {
            // Çok küçük radius değerleri sonuç getirmeyebilir, minimum 1km olarak ayarlıyoruz
            radius = 1000;
        }
        
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(PLACES_NEARBY_URL)
                .queryParam("location", latitude + "," + longitude)
                .queryParam("radius", radius)
                .queryParam("type", type)
                .queryParam("language", "tr")   // Türkçe sonuçlar için
                .queryParam("region", "tr")     // Türkiye bölgesi için
                .queryParam("key", apiKey)
                .queryParam("timestamp", System.currentTimeMillis());
        
        // Sadece açık olan işletmeleri getirmek için opennow parametresi ekleniyor
        if (openNow) {
            builder.queryParam("opennow", true);
        }
        
        // Daha fazla sonuç için sayfalama yapılıyor
        return fetchAllPagesAndCombine(builder.toUriString());
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
        // Varsayılan olarak tüm işletmeleri getir (hem açık hem kapalı)
        return searchNearbyPlaces(latitude, longitude, radius, type, false);
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
        
        // Radius kontrolü
        if (radius > 50000) {
            radius = 50000;
        } else if (radius < 1000) {
            radius = 1000;
        }
        
        // İlk olarak veteriner kliniklerini deneyelim (nearby search)
        try {
            ResponseEntity<String> vetResponse = searchNearbyPlaces(latitude, longitude, radius, "veterinary_care");
            // Sonuç kontrolü yapılabilir, sonuç yoksa diğer metoda geçilir
        } catch (Exception e) {
            logger.warn("Veterinary search failed, trying text search: ", e);
        }
        
        // Text search ile daha geniş bir arama yapalım
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(PLACES_TEXT_SEARCH_URL)
                .queryParam("location", latitude + "," + longitude)
                .queryParam("radius", radius)
                .queryParam("language", "tr")  // Türkçe sonuçlar için
                .queryParam("region", "tr")    // Türkiye bölgesi için
                .queryParam("key", apiKey)
                .queryParam("timestamp", System.currentTimeMillis());
        
        // Build a query that combines the keyword with pet-related terms
        StringBuilder queryBuilder = new StringBuilder();
        if (keyword != null && !keyword.trim().isEmpty()) {
            queryBuilder.append(keyword).append(" ");
        }
        // Türkçe ve İngilizce anahtar kelimeler ekleniyor
        queryBuilder.append("pet veteriner veterinary hayvan animal evcil");
        
        builder.queryParam("query", queryBuilder.toString());
        
        String url = builder.toUriString();
        logger.debug("Text Search Request URL: {}", url);
        
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            logger.info("Places API text search response status: {}", response.getStatusCode());
            return response;
        } catch (Exception e) {
            logger.error("Error while fetching places with text search: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"" + e.getMessage() + "\", \"status\": \"ERROR\"}");
        }
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
                .queryParam("language", "tr")  // Türkçe sonuçlar ve değerlendirmeler için
                .queryParam("reviews_no_translations", "false") // Değerlendirmelerin çevirisini iste
                .queryParam("reviews_sort", "newest") // En yeni değerlendirmeleri göster
                .queryParam("fields", "name,rating,formatted_address,formatted_phone_number,geometry,opening_hours,photos,reviews,types,website,url,vicinity,user_ratings_total,price_level,international_phone_number,address_components")
                .queryParam("key", apiKey)
                .queryParam("timestamp", System.currentTimeMillis());
        
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
                .queryParam("language", "tr")  // Türkçe sonuçlar için
                .queryParam("region", "tr")    // Türkiye bölgesi için  
                .queryParam("key", apiKey)
                .queryParam("timestamp", System.currentTimeMillis());
        
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
     * @param openNow Whether to return only places that are open now
     * @return ResponseEntity containing the Google Places API response
     */
    public ResponseEntity<String> textSearchPlaces(String query, Double latitude, Double longitude, Integer radius, boolean openNow) {
        logger.info("Performing text search for: {}, openNow: {}", query, openNow);
        
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(PLACES_TEXT_SEARCH_URL)
                .queryParam("query", query)
                .queryParam("language", "tr")  // Türkçe sonuçlar için
                .queryParam("region", "tr")    // Türkiye bölgesi için
                .queryParam("key", apiKey)
                .queryParam("timestamp", System.currentTimeMillis());
        
        // Add location bias if coordinates are provided
        if (latitude != null && longitude != null) {
            builder.queryParam("location", latitude + "," + longitude);
            
            if (radius != null) {
                builder.queryParam("radius", radius);
            }
        }
        
        // Sadece açık olan işletmeleri getirmek için opennow parametresi ekleniyor
        if (openNow) {
            builder.queryParam("opennow", true);
        }
        
        // Daha fazla sonuç için sayfalama yapılıyor
        return fetchAllPagesAndCombine(builder.toUriString());
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
        // Varsayılan olarak tüm işletmeleri getir (hem açık hem kapalı)
        return textSearchPlaces(query, latitude, longitude, radius, false);
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
    
    /**
     * Get a photo using its reference
     * 
     * @param photoReference The photo reference from Places API
     * @param maxWidth The maximum width of the image (optional)
     * @param maxHeight The maximum height of the image (optional)
     * @return ResponseEntity containing the image data
     */
    public ResponseEntity<byte[]> getPlacePhoto(String photoReference, Integer maxWidth, Integer maxHeight) {
        logger.info("Getting photo with reference: {}", photoReference);
        
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(PLACES_PHOTO_URL)
                .queryParam("photoreference", photoReference)
                .queryParam("key", apiKey)
                .queryParam("timestamp", System.currentTimeMillis());
        
        if (maxWidth != null) {
            builder.queryParam("maxwidth", maxWidth);
        } else {
            builder.queryParam("maxwidth", 400); // Default width
        }
        
        if (maxHeight != null) {
            builder.queryParam("maxheight", maxHeight);
        }
        
        String url = builder.toUriString();
        logger.debug("Photo Request URL: {}", url);
        
        // Configure RestTemplate to handle binary data
        RestTemplate photoRestTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        ResponseEntity<byte[]> response = photoRestTemplate.exchange(
                url, 
                HttpMethod.GET, 
                entity, 
                byte[].class);
        
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.IMAGE_JPEG);
        
        return new ResponseEntity<>(response.getBody(), responseHeaders, HttpStatus.OK);
    }
    
    /**
     * Tüm sayfaları getirip birleştiren yardımcı metod
     * 
     * @param initialUrl İlk sayfa için URL
     * @return Birleştirilmiş sonuçları içeren ResponseEntity
     */
    @SuppressWarnings("unchecked")
    private ResponseEntity<String> fetchAllPagesAndCombine(String initialUrl) {
        logger.debug("Initial Request URL: {}", initialUrl);
        
        try {
            // İlk sayfayı getir
            ResponseEntity<String> firstResponse = restTemplate.getForEntity(initialUrl, String.class);
            
            // İlk sayfa başarısız ise, doğrudan döndür
            if (firstResponse.getStatusCode() != HttpStatus.OK) {
                logger.warn("First page request failed with status: {}", firstResponse.getStatusCode());
                return firstResponse;
            }
            
            // JSON'ı parse et
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> firstPageData = mapper.readValue(firstResponse.getBody(), Map.class);
            
            // Sonuç yoksa veya hata varsa, doğrudan döndür
            if (!"OK".equals(firstPageData.get("status")) || !firstPageData.containsKey("results")) {
                logger.warn("First page has no results or error status: {}", firstPageData.get("status"));
                return firstResponse;
            }
            
            // Tüm sonuçları tutacak liste
            List<Map<String, Object>> allResults = new ArrayList<>((List<Map<String, Object>>) firstPageData.get("results"));
            
            // Sonraki sayfa token'ı varsa, diğer sayfaları da getir
            String nextPageToken = (String) firstPageData.get("next_page_token");
            int pageCount = 1;
            
            while (nextPageToken != null && !nextPageToken.isEmpty() && pageCount < 3) { // En fazla 3 sayfa (60 sonuç)
                // Google API'nin page token'ı işlemesi için biraz bekle
                try {
                    Thread.sleep(2000); // 2 saniye bekle
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                
                // Sonraki sayfayı getir
                String nextPageUrl = UriComponentsBuilder.fromHttpUrl(initialUrl.split("\\?")[0])
                        .queryParam("pagetoken", nextPageToken)
                        .queryParam("key", apiKey)
                        .toUriString();
                
                logger.debug("Next page URL: {}", nextPageUrl);
                
                ResponseEntity<String> nextPageResponse = restTemplate.getForEntity(nextPageUrl, String.class);
                
                // Sonraki sayfa başarısız ise, şu ana kadar toplanan sonuçlarla devam et
                if (nextPageResponse.getStatusCode() != HttpStatus.OK) {
                    logger.warn("Next page request failed with status: {}", nextPageResponse.getStatusCode());
                    break;
                }
                
                Map<String, Object> nextPageData = mapper.readValue(nextPageResponse.getBody(), Map.class);
                
                // Sonuç yoksa veya hata varsa, şu ana kadar toplanan sonuçlarla devam et
                if (!"OK".equals(nextPageData.get("status")) || !nextPageData.containsKey("results")) {
                    logger.warn("Next page has no results or error status: {}", nextPageData.get("status"));
                    break;
                }
                
                // Sonuçları ana listeye ekle
                allResults.addAll((List<Map<String, Object>>) nextPageData.get("results"));
                
                // Sonraki sayfa token'ını güncelle
                nextPageToken = (String) nextPageData.get("next_page_token");
                pageCount++;
            }
            
            // Tüm sonuçları içeren yeni bir JSON oluştur
            Map<String, Object> combinedData = new HashMap<>(firstPageData);
            combinedData.put("results", allResults);
            combinedData.put("result_count", allResults.size());
            
            // Artık kullanılmayacak olan next_page_token'ı kaldır
            combinedData.remove("next_page_token");
            
            logger.info("Combined {} pages, total results: {}", pageCount, allResults.size());
            
            // Birleştirilmiş sonuçları döndür
            return ResponseEntity.ok(mapper.writeValueAsString(combinedData));
            
        } catch (Exception e) {
            logger.error("Error while fetching and combining pages: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"" + e.getMessage() + "\", \"status\": \"ERROR\"}");
        }
    }
} 