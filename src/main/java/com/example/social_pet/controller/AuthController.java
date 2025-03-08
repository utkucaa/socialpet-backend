package com.example.social_pet.controller;

import com.example.social_pet.dto.UserDTO;
import com.example.social_pet.entities.User;
import com.example.social_pet.request.LoginRequest;
import com.example.social_pet.response.JwtAuthResponse;
import com.example.social_pet.security.JwtTokenProvider;
import com.example.social_pet.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager,
                         JwtTokenProvider tokenProvider,
                         UserService userService) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            logger.info("Login attempt for email: {}", loginRequest.getEmail());
            
            if (loginRequest.getEmail() == null || loginRequest.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(createErrorResponse("Email cannot be empty"));
            }
            
            if (loginRequest.getPassword() == null || loginRequest.getPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(createErrorResponse("Password cannot be empty"));
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = tokenProvider.generateToken(authentication);
            User user = userService.findByEmail(loginRequest.getEmail());
            
            logger.info("Login successful for user: {}", loginRequest.getEmail());
            return ResponseEntity.ok(new JwtAuthResponse(jwt, user.getId(), user.getEmail(), 
                user.getJoinDate() != null ? LocalDateTime.ofInstant(user.getJoinDate().toInstant(), ZoneId.systemDefault()) : null,
                user.getUserName(), user.getFirstName(), user.getLastName(), user.getAvatarUrl(), user.getRole()));

        } catch (BadCredentialsException e) {
            logger.error("Invalid credentials for user: {}", loginRequest.getEmail());
            return ResponseEntity.badRequest().body(createErrorResponse("Invalid email or password"));
        } catch (Exception e) {
            logger.error("Login error for user: {}", loginRequest.getEmail(), e);
            return ResponseEntity.badRequest().body(createErrorResponse("Authentication failed: " + e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        try {
            logger.info("Registration attempt for email: {}", userDTO.getEmail());
            
            // Validation
            if (userDTO.getEmail() == null || userDTO.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(createErrorResponse("Email cannot be empty"));
            }
            
            if (userDTO.getPassword() == null || userDTO.getPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(createErrorResponse("Password cannot be empty"));
            }

            User user = userService.registerUser(userDTO);
            
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userDTO.getEmail(),
                            userDTO.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.generateToken(authentication);
            
            logger.info("Registration successful for user: {}", userDTO.getEmail());
            return ResponseEntity.ok(new JwtAuthResponse(jwt, user.getId(), user.getEmail(),
                user.getJoinDate() != null ? LocalDateTime.ofInstant(user.getJoinDate().toInstant(), ZoneId.systemDefault()) : null,
                user.getUserName(), user.getFirstName(), user.getLastName(), user.getAvatarUrl(), user.getRole()));

        } catch (Exception e) {
            logger.error("Registration error for user: {}", userDTO.getEmail(), e);
            return ResponseEntity.badRequest().body(createErrorResponse("Registration failed: " + e.getMessage()));
        }
    }

    private Map<String, String> createErrorResponse(String message) {
        Map<String, String> response = new HashMap<>();
        response.put("error", message);
        return response;
    }
}
