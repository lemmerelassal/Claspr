package com.dating.service.impl;

import com.dating.config.AppConfig;
import com.dating.service.IImageService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;

@ApplicationScoped
public class ImageServiceImpl implements IImageService {

    private final Path uploadDir;
    private final long maxFileSize;
    private final Set<String> allowedTypes;

    @Inject
    public ImageServiceImpl(AppConfig config) {
        this.uploadDir = Paths.get(config.uploadDirectory());
        this.maxFileSize = config.maxImageSizeBytes();
        this.allowedTypes = Set.of(config.allowedImageTypes().split(","));
        try {
            Files.createDirectories(this.uploadDir);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create upload directory: " + uploadDir, e);
        }
    }

    @Override
    public String saveImage(InputStream data, String contentType, String userId) throws IOException {
        if (!allowedTypes.contains(contentType)) {
            throw new IllegalArgumentException("Unsupported image type: " + contentType);
        }

        String extension = switch (contentType) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            case "image/gif" -> ".gif";
            default -> ".jpg";
        };

        String filename = userId + "_" + UUID.randomUUID().toString().substring(0, 8) + extension;
        Path filePath = uploadDir.resolve(filename);

        long bytes = Files.copy(data, filePath, StandardCopyOption.REPLACE_EXISTING);
        if (bytes > maxFileSize) {
            Files.delete(filePath);
            throw new IllegalArgumentException("File too large. Maximum size is " + (maxFileSize / 1024 / 1024) + "MB.");
        }

        return "/uploads/photos/" + filename;
    }

    @Override
    public boolean deleteImage(String urlPath) {
        try {
            String relativePath = urlPath.startsWith("/") ? urlPath.substring(1) : urlPath;
            return Files.deleteIfExists(Paths.get(relativePath));
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public Path getImagePath(String filename) {
        return uploadDir.resolve(filename);
    }
}
