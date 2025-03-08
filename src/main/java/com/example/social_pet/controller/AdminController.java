package com.example.social_pet.controller;

import com.example.social_pet.dto.AdminUserDTO;
import com.example.social_pet.dto.UserDTO;
import com.example.social_pet.entities.Role;
import com.example.social_pet.entities.User;
import com.example.social_pet.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    // Get all users
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AdminUserDTO>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<AdminUserDTO> userDTOs = users.stream()
                .map(AdminUserDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }

    // Get user by ID
    @GetMapping("/users/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminUserDTO> getUserById(@PathVariable Long userId) {
        try {
            User user = userService.getUserById(userId);
            AdminUserDTO userDTO = new AdminUserDTO(user);
            return ResponseEntity.ok(userDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Create new user
    @PostMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {
        try {
            User user = userService.createUser(userDTO);
            AdminUserDTO responseDTO = new AdminUserDTO(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Update user
    @PutMapping("/users/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody UserDTO userDTO) {
        try {
            User user = userService.updateUser(userId, userDTO);
            AdminUserDTO responseDTO = new AdminUserDTO(user);
            return ResponseEntity.ok(responseDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Delete user
    @DeleteMapping("/users/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok("User deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Change user role
    @PutMapping("/users/{userId}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> changeUserRole(@PathVariable Long userId, @RequestParam Role role) {
        try {
            User user = userService.changeUserRole(userId, role);
            AdminUserDTO responseDTO = new AdminUserDTO(user);
            return ResponseEntity.ok(responseDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
} 