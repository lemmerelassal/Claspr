package com.dating.config;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

/**
 * Application configuration — externalizes all magic values.
 * Values can be overridden via application.properties or environment variables.
 */
@ConfigMapping(prefix = "app")
public interface AppConfig {

    @WithDefault("50")
    int defaultMaxDistanceKm();

    @WithDefault("18")
    int defaultMinAge();

    @WithDefault("99")
    int defaultMaxAge();

    @WithDefault("10485760") // 10MB
    long maxImageSizeBytes();

    @WithDefault("uploads/photos")
    String uploadDirectory();

    @WithDefault("image/jpeg,image/png,image/webp,image/gif")
    String allowedImageTypes();

    @WithDefault("6")
    int maxPhotosPerUser();
}
