package com.example.social_pet.controller;

import com.example.social_pet.dto.AdminUserDTO;
import com.example.social_pet.dto.UserDTO;
import com.example.social_pet.entities.Adoption;
import com.example.social_pet.entities.LostPet;
import com.example.social_pet.entities.Role;
import com.example.social_pet.entities.User;
import com.example.social_pet.service.AdoptionService;
import com.example.social_pet.service.LostPetService;
import com.example.social_pet.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {

    private final UserService userService;
    private final AdoptionService adoptionService;
    private final LostPetService lostPetService;

    public AdminController(UserService userService, AdoptionService adoptionService, LostPetService lostPetService) {
        this.userService = userService;
        this.adoptionService = adoptionService;
        this.lostPetService = lostPetService;
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

    // Get counts of pending listings for dashboard
    @GetMapping("/pending-counts")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getPendingCounts() {
        Map<String, Object> response = new HashMap<>();
        response.put("pendingAdoptionsCount", adoptionService.getPendingAdoptions().size());
        response.put("pendingLostPetsCount", lostPetService.getPendingLostPets().size());
        response.put("totalPendingCount", 
            adoptionService.getPendingAdoptions().size() + lostPetService.getPendingLostPets().size());
        return ResponseEntity.ok(response);
    }

    // Get all pending listings (both adoptions and lost pets)
    @GetMapping("/pending-listings")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllPendingListings() {
        Map<String, Object> response = new HashMap<>();
        response.put("pendingAdoptions", adoptionService.getPendingAdoptions());
        response.put("pendingLostPets", lostPetService.getPendingLostPets());
        return ResponseEntity.ok(response);
    }

    // Get pending adoption listings
    @GetMapping("/adoptions/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Adoption>> getPendingAdoptions() {
        List<Adoption> pendingAdoptions = adoptionService.getPendingAdoptions();
        return ResponseEntity.ok(pendingAdoptions);
    }

    // Approve adoption listing
    @PutMapping("/adoptions/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Adoption> approveAdoption(@PathVariable Long id) {
        try {
            Adoption adoption = adoptionService.approveAdoption(id);
            return ResponseEntity.ok(adoption);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Reject adoption listing
    @PutMapping("/adoptions/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Adoption> rejectAdoption(@PathVariable Long id) {
        try {
            Adoption adoption = adoptionService.rejectAdoption(id);
            return ResponseEntity.ok(adoption);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Get pending lost pet listings
    @GetMapping("/lostpets/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<LostPet>> getPendingLostPets() {
        List<LostPet> pendingLostPets = lostPetService.getPendingLostPets();
        return ResponseEntity.ok(pendingLostPets);
    }

    // Approve lost pet listing
    @PutMapping("/lostpets/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LostPet> approveLostPet(@PathVariable Long id) {
        try {
            LostPet lostPet = lostPetService.approveLostPet(id);
            return ResponseEntity.ok(lostPet);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Reject lost pet listing
    @PutMapping("/lostpets/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LostPet> rejectLostPet(@PathVariable Long id) {
        try {
            LostPet lostPet = lostPetService.rejectLostPet(id);
            return ResponseEntity.ok(lostPet);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
} 