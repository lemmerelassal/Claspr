package com.dating.service;

import com.dating.entity.UserProfile;
import java.util.UUID;

/**
 * Authentication service contract.
 * Handles user registration, login, and token validation.
 */
public interface IAuthService {

    record AuthResult(String token, UUID userId, String displayName) {}
    record TokenValidation(boolean valid, String userId) {}

    AuthResult register(String email, String password, String displayName,
                        String dateOfBirth, UserProfile.Gender gender);

    AuthResult login(String email, String password);

    TokenValidation validateToken(String token);
}
