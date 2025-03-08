package com.example.social_pet.response;

import com.example.social_pet.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class JwtAuthResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private Long userId;
    private String email;
    private LocalDateTime joinDate;
    private String username;
    private String firstName;
    private String lastName;
    private String avatarUrl;
    private Role role;

    public JwtAuthResponse(String accessToken, Long userId, String email, LocalDateTime joinDate, String username, String firstName, String lastName, String avatarUrl, Role role) {
        this.accessToken = accessToken;
        this.userId = userId;
        this.email = email;
        this.joinDate = joinDate;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatarUrl = avatarUrl;
        this.role = role;
    }
}
