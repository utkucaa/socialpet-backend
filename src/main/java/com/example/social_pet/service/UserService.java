package com.example.social_pet.service;

import com.example.social_pet.dto.UserDTO;
import com.example.social_pet.dto.UserStatsDTO;
import com.example.social_pet.entities.User;
import com.example.social_pet.entities.Role;
import com.example.social_pet.repository.UserRepository;
import com.example.social_pet.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

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

    public User loginUser(String email, String password) {
        User user = findByEmail(email);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        return user;
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
        user.setRole(Role.MEMBER); // Set default role as MEMBER
        
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

    // Add method to change user role (only admins can use this)
    public User changeUserRole(Long userId, Role newRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        user.setRole(newRole);
        return userRepository.save(user);
    }

    // Get all users (admin only)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    // Get user by ID (admin only)
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }
    
    // Create user (admin only)
    public User createUser(UserDTO userDTO) {
        if(userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already taken!");
        }
        
        if(userRepository.findByUserName(userDTO.getUserName()).isPresent()) {
            throw new RuntimeException("Username is already taken!");
        }

        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setUserName(userDTO.getUserName());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        
        // Set role if provided, otherwise default to MEMBER
        if (userDTO.getRole() != null) {
            user.setRole(userDTO.getRole());
        } else {
            user.setRole(Role.MEMBER);
        }
        
        return userRepository.save(user);
    }
    
    // Update user (admin only)
    public User updateUser(Long userId, UserDTO userDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        if(userDTO.getUserName() != null) {
            // Check if username is already taken by another user
            Optional<User> existingUser = userRepository.findByUserName(userDTO.getUserName());
            if(existingUser.isPresent() && !existingUser.get().getId().equals(userId)) {
                throw new RuntimeException("Username is already taken!");
            }
            user.setUserName(userDTO.getUserName());
        }
        
        if(userDTO.getEmail() != null) {
            // Check if email is already taken by another user
            Optional<User> existingUser = userRepository.findByEmail(userDTO.getEmail());
            if(existingUser.isPresent() && !existingUser.get().getId().equals(userId)) {
                throw new RuntimeException("Email is already taken!");
            }
            user.setEmail(userDTO.getEmail());
        }
        
        if(userDTO.getFirstName() != null) user.setFirstName(userDTO.getFirstName());
        if(userDTO.getLastName() != null) user.setLastName(userDTO.getLastName());
        if(userDTO.getPhoneNumber() != null) user.setPhoneNumber(userDTO.getPhoneNumber());
        if(userDTO.getPassword() != null) user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        if(userDTO.getRole() != null) user.setRole(userDTO.getRole());
        
        return userRepository.save(user);
    }
    
    // Delete user (admin only)
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found with id: " + userId);
        }
        userRepository.deleteById(userId);
    }
}
