package com.dating.service.impl;

import com.dating.service.PasswordEncoder;
import jakarta.enterprise.context.ApplicationScoped;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * PBKDF2WithHmacSHA256 password encoder with per-password random salt.
 * Stored format: "<iterations>:<base64-salt>:<base64-hash>"
 * Legacy SHA-256 hashes (no colons) are still verified for migration compatibility.
 */
@ApplicationScoped
public class Pbkdf2PasswordEncoder implements PasswordEncoder {

    private static final int ITERATIONS = 310_000;
    private static final int KEY_LENGTH_BITS = 256;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final SecureRandom RANDOM = new SecureRandom();

    @Override
    public String encode(String rawPassword) {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        byte[] hash = pbkdf2(rawPassword.toCharArray(), salt, ITERATIONS);
        return ITERATIONS + ":"
                + Base64.getEncoder().encodeToString(salt) + ":"
                + Base64.getEncoder().encodeToString(hash);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        if (encodedPassword == null) return false;
        if (!encodedPassword.contains(":")) {
            return legacySha256(rawPassword).equals(encodedPassword);
        }
        String[] parts = encodedPassword.split(":");
        if (parts.length != 3) return false;
        try {
            int iterations = Integer.parseInt(parts[0]);
            byte[] salt = Base64.getDecoder().decode(parts[1]);
            byte[] expected = Base64.getDecoder().decode(parts[2]);
            byte[] actual = pbkdf2(rawPassword.toCharArray(), salt, iterations);
            return MessageDigest.isEqual(actual, expected);
        } catch (Exception e) {
            return false;
        }
    }

    private byte[] pbkdf2(char[] password, byte[] salt, int iterations) {
        try {
            var spec = new PBEKeySpec(password, salt, iterations, KEY_LENGTH_BITS);
            return SecretKeyFactory.getInstance(ALGORITHM).generateSecret(spec).getEncoded();
        } catch (Exception e) {
            throw new RuntimeException("Password hashing failed", e);
        }
    }

    private String legacySha256(String password) {
        try {
            var md = java.security.MessageDigest.getInstance("SHA-256");
            return Base64.getEncoder().encodeToString(md.digest(password.getBytes()));
        } catch (Exception e) {
            return "";
        }
    }
}
