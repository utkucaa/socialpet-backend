package com.example.social_pet.request;

public class LoginRequest {
    private String email;
    private String password;

    // Constructor (isteğe bağlı)
    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getter ve Setter metodları
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
