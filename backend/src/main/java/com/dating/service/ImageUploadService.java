package com.dating.service;

import jakarta.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@ApplicationScoped
public class ImageUploadService {

    private static final String UPLOAD_DIR = "uploads/photos";
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final java.util.Set<String> ALLOWED_TYPES = java.util.Set.of(
            "image/jpeg", "image/png", "image/webp", "image/gif"
    );

    public ImageUploadService() {
        try {
            Files.createDirectories(Paths.get(UPLOAD_DIR));
        } catch (IOException e) {
            throw new RuntimeException("Failed to create upload directory", e);
        }
    }

    /**
     * Save an uploaded image to disk and return the URL path.
     */
    public String saveImage(InputStream data, String contentType, String userId) throws IOException {
        if (!ALLOWED_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("Unsupported image type: " + contentType + ". Allowed: JPEG, PNG, WebP, GIF");
        }

        String extension = switch (contentType) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            case "image/gif" -> ".gif";
            default -> ".jpg";
        };

        String filename = userId + "_" + UUID.randomUUID().toString().substring(0, 8) + extension;
        Path filePath = Paths.get(UPLOAD_DIR, filename);

        long bytes = Files.copy(data, filePath, StandardCopyOption.REPLACE_EXISTING);

        if (bytes > MAX_FILE_SIZE) {
            Files.delete(filePath);
            throw new IllegalArgumentException("File too large. Maximum size is 10MB.");
        }

        // Return the URL path that will be served by the static file endpoint
        return "/uploads/photos/" + filename;
    }

    /**
     * Delete an image file by its URL path.
     */
    public boolean deleteImage(String urlPath) {
        try {
            // Strip leading slash to get relative path
            String relativePath = urlPath.startsWith("/") ? urlPath.substring(1) : urlPath;
            Path filePath = Paths.get(relativePath);
            return Files.deleteIfExists(filePath);
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Get the file path for serving.
     */
    public Path getImagePath(String filename) {
        return Paths.get(UPLOAD_DIR, filename);
    }
}
