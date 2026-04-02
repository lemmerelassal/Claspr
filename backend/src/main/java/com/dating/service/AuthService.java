package com.dating.service;

import com.dating.entity.UserProfile;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.security.MessageDigest;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Set;
import java.util.UUID;

@ApplicationScoped
public class AuthService {

    public record AuthResult(String token, UUID userId, String displayName) {}
    public record TokenValidation(boolean valid, String userId) {}

    @Transactional
    public AuthResult register(String email, String password, String displayName,
                               String dateOfBirth, UserProfile.Gender gender) {
        if (UserProfile.findByEmail(email) != null) {
            throw new IllegalArgumentException("Email already registered");
        }

        var user = new UserProfile();
        user.email = email;
        user.passwordHash = hashPassword(password);
        user.displayName = displayName;
        if (dateOfBirth != null && !dateOfBirth.isEmpty()) {
            user.dateOfBirth = LocalDate.parse(dateOfBirth);
        }
        user.gender = gender;
        user.persist();

        String token = generateToken(user);
        return new AuthResult(token, user.id, user.displayName);
    }

    public AuthResult login(String email, String password) {
        UserProfile user = UserProfile.findByEmail(email);
        if (user == null || !verifyPassword(password, user.passwordHash)) {
            throw new SecurityException("Invalid credentials");
        }

        String token = generateToken(user);
        return new AuthResult(token, user.id, user.displayName);
    }

    public TokenValidation validateToken(String token) {
        // In production, verify JWT signature and claims
        // For now, a simplified check
        try {
            // The JWT library will verify on the resource side;
            // here we just confirm the token is structurally valid
            if (token == null || token.isBlank() || token.split("\\.").length != 3) {
                return new TokenValidation(false, null);
            }
            // Decode the payload
            String payload = new String(Base64.getUrlDecoder().decode(token.split("\\.")[1]));
            // Extract subject (user ID)
            String sub = extractJsonField(payload, "sub");
            return new TokenValidation(sub != null, sub);
        } catch (Exception e) {
            return new TokenValidation(false, null);
        }
    }

    private String generateToken(UserProfile user) {
        return Jwt.issuer("https://dating-app.io")
                .upn(user.email)
                .subject(user.id.toString())
                .groups(Set.of("user"))
                .claim("displayName", user.displayName)
                .expiresIn(Duration.ofDays(7))
                .sign();
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Failed to hash password", e);
        }
    }

    private boolean verifyPassword(String password, String hash) {
        return hashPassword(password).equals(hash);
    }

    private String extractJsonField(String json, String field) {
        String key = "\"" + field + "\":\"";
        int start = json.indexOf(key);
        if (start == -1) return null;
        start += key.length();
        int end = json.indexOf("\"", start);
        return json.substring(start, end);
    }
}
