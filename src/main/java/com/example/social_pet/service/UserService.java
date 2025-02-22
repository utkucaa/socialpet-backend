package com.example.social_pet.service;

import com.example.social_pet.dto.UserDTO;
import com.example.social_pet.dto.UserStatsDTO;
import com.example.social_pet.dto.UserCardDto;
import com.example.social_pet.entities.User;
import com.example.social_pet.repository.UserRepository;
import com.example.social_pet.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.Date;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    public User registerUser(UserDTO userDTO) {
        if(userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already taken!");
        }

        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setUserName(userDTO.getUserName());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        
        return userRepository.save(user);
    }

    public User updateUserProfile(Long userId, UserDTO userDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        if(userDTO.getFirstName() != null) user.setFirstName(userDTO.getFirstName());
        if(userDTO.getLastName() != null) user.setLastName(userDTO.getLastName());
        if(userDTO.getPhoneNumber() != null) {
            if (!userDTO.getPhoneNumber().matches("^\\+?[0-9]{10,15}$")) {
                throw new RuntimeException("Invalid phone number format");
            }
            user.setPhoneNumber(userDTO.getPhoneNumber());
        }

        return userRepository.save(user);
    }

    public User updateProfilePhoto(Long userId, MultipartFile photo) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        if (photo != null && !photo.isEmpty()) {
            String fileName = fileStorageService.storeFile(photo);
            user.setAvatarUrl(fileName);
            return userRepository.save(user);
        }
        throw new RuntimeException("Photo file is required");
    }

    public UserStatsDTO getUserStats(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        long totalAds = user.getAdoptions().size() + user.getLostPets().size();
        long activeAds = user.getAdoptions().stream().count() + user.getLostPets().stream().count();
        // For now, we'll use a simple view count
        long views = totalAds * 10; // Placeholder implementation

        return new UserStatsDTO(totalAds, activeAds, views, user.getJoinDate(), user.getAvatarUrl());
    }

    public String updateProfilePicture(MultipartFile file, String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        String email = jwtTokenProvider.getUsernameFromToken(token);
        User user = findByEmail(email);
        
        String fileName = fileStorageService.storeFile(file);
        String fileUrl = "/api/v1/files/" + fileName;
        
        user.setAvatarUrl(fileUrl);
        userRepository.save(user);
        
        return fileUrl;
    }
}
