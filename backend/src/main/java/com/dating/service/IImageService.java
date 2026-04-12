package com.dating.service;

import java.io.IOException;
import java.io.InputStream;

/**
 * Image storage contract.
 * Handles saving, deleting, and resolving uploaded images.
 * Implementations can use local disk, S3, GCS, etc.
 */
public interface IImageService {

    String saveImage(InputStream data, String contentType, String userId) throws IOException;

    boolean deleteImage(String urlPath);

    java.nio.file.Path getImagePath(String filename);
}
