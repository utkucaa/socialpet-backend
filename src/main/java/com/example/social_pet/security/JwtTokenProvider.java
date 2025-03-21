package com.example.social_pet.security;

import com.example.social_pet.config.JwtConfig;
import com.example.social_pet.entities.User;
import com.example.social_pet.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final SecretKey key;
    private final JwtConfig jwtConfig;
    
    @Autowired
    private UserRepository userRepository;

    public JwtTokenProvider(JwtConfig jwtConfig, UserRepository userRepository) {
        this.jwtConfig = jwtConfig;
        this.userRepository = userRepository;
        this.key = Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
                
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtConfig.getExpiration());

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .claim("email", userDetails.getUsername())
                .claim("role", user.getRole().name())
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            // Log error about invalid JWT signature
            return false;
        } catch (MalformedJwtException ex) {
            // Log error about invalid JWT token
            return false;
        } catch (ExpiredJwtException ex) {
            // Log error about expired JWT token
            return false;
        } catch (UnsupportedJwtException ex) {
            // Log error about unsupported JWT token
            return false;
        } catch (IllegalArgumentException ex) {
            // Log error about JWT claims string being empty
            return false;
        }
    }
}
