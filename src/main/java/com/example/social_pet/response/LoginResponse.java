package com.example.social_pet.response;

public class LoginResponse {
    private String token;  // JWT token'ı tutacak
    private String message;  // Kullanıcıya gösterilecek mesaj

    // Constructor (yapıcı metot)
    public LoginResponse(String token, String message) {
        this.token = token;
        this.message = message;
    }

    // Getter ve Setter metodları
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

