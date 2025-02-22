package com.example.social_pet.service;
import com.example.social_pet.dto.UserDTO;
import com.example.social_pet.dto.UserStatsDTO;
import com.example.social_pet.entities.User;
import com.example.social_pet.repository.UserRepository;
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

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User loginUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        return user;
    }

    public User registerUser(UserDTO userDTO) {
        if (!userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
            throw new RuntimeException("Password and Confirm Password do not match");
        }

        if (userRepository.findByUserName(userDTO.getUserName()).isPresent()) {
            throw new RuntimeException("Username is already taken");
        }

        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already in use");
        }

        if (!userDTO.getPhoneNumber().matches("^\\+?[0-9]{10,15}$")) {
            throw new RuntimeException("Invalid phone number format");
        }

        User newUser = new User();
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        newUser.setUserName(userDTO.getUserName());
        newUser.setEmail(userDTO.getEmail());
        newUser.setPhoneNumber(userDTO.getPhoneNumber());
        newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        return userRepository.save(newUser);
    }

    public UserStatsDTO getUserStats(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        long totalAds = user.getAdoptions().size() + user.getLostPets().size();
        long activeAds = user.getAdoptions().stream().count() + user.getLostPets().stream().count();
        // For now, we'll use a simple view count
        long views = totalAds * 10; // Placeholder implementation

        return new UserStatsDTO(totalAds, activeAds, views, user.getJoinDate(), user.getAvatarUrl());
    }

    public User updateUserProfile(Long userId, UserDTO userDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (userDTO.getFirstName() != null) user.setFirstName(userDTO.getFirstName());
        if (userDTO.getLastName() != null) user.setLastName(userDTO.getLastName());
        if (userDTO.getPhoneNumber() != null) {
            if (!userDTO.getPhoneNumber().matches("^\\+?[0-9]{10,15}$")) {
                throw new RuntimeException("Invalid phone number format");
            }
            user.setPhoneNumber(userDTO.getPhoneNumber());
        }

        return userRepository.save(user);
    }

    public User updateProfilePhoto(Long userId, MultipartFile photo) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (photo != null && !photo.isEmpty()) {
            String fileName = fileStorageService.storeFile(photo);
            user.setAvatarUrl(fileName);
            return userRepository.save(user);
        }
        throw new RuntimeException("Photo file is required");
    }
}
