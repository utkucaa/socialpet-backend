package com.example.social_pet.controller;
import com.example.social_pet.dto.UserCardDto;
import com.example.social_pet.dto.UserDTO;
import com.example.social_pet.dto.UserStatsDTO;
import com.example.social_pet.entities.User;
import com.example.social_pet.request.LoginRequest;
import com.example.social_pet.response.FileUrlResponse;
import com.example.social_pet.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserCardDto> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            User user = userService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
            UserCardDto userDTO = new UserCardDto(user);
            return ResponseEntity.ok(userDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        try {
            User user = userService.registerUser(userDTO);
            UserCardDto responseDTO = new UserCardDto(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Kayıt işlemi başarısız. Lütfen tekrar deneyin.");
        }
    }

    @GetMapping("/{userId}/stats")
    public ResponseEntity<UserStatsDTO> getUserStats(@PathVariable Long userId) {
        try {
            UserStatsDTO stats = userService.getUserStats(userId);
            return ResponseEntity.ok(stats);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/{userId}/profile")
    public ResponseEntity<User> updateProfile(
            @PathVariable Long userId,
            @RequestBody UserDTO userDTO) {
        try {
            User updatedUser = userService.updateUserProfile(userId, userDTO);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping(value = "/{userId}/profile-photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<User> updateProfilePhoto(
            @PathVariable Long userId,
            @RequestParam("photo") MultipartFile photo) {
        try {
            User updatedUser = userService.updateProfilePhoto(userId, photo);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping(value = "/profile-picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileUrlResponse> uploadProfilePicture(
            @RequestParam("file") MultipartFile file,
            @RequestHeader("Authorization") String token) {
        try {
            String fileUrl = userService.updateProfilePicture(file, token);
            return ResponseEntity.ok(new FileUrlResponse(fileUrl));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}
