package com.dating.dto;

import com.dating.entity.UserProfile;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class UserDtos {

    public record RegisterRequest(
            String email,
            String password,
            String displayName,
            LocalDate dateOfBirth,
            UserProfile.Gender gender
    ) {}

    public record LoginRequest(String email, String password) {}

    public record AuthResponse(String token, UUID userId, String displayName) {}

    public record ProfileUpdateRequest(
            String displayName,
            String bio,
            UserProfile.Gender genderPreference,
            Double latitude,
            Double longitude,
            String city,
            Integer maxDistanceKm,
            Integer minAgePreference,
            Integer maxAgePreference,
            List<String> photoUrls,
            Set<String> interests
    ) {}

    public record ProfileResponse(
            UUID id,
            String displayName,
            int age,
            String bio,
            UserProfile.Gender gender,
            List<String> photoUrls,
            Set<String> interests,
            String city,
            double distanceKm
    ) {}

    public record SwipeRequest(UUID swipedId, String direction) {}

    public record SwipeResponse(boolean isMatch, UUID matchId, ProfileResponse matchedProfile) {}

    public record MatchResponse(
            UUID matchId,
            ProfileResponse profile,
            String matchedAt,
            LastMessageDto lastMessage
    ) {}

    public record LastMessageDto(String content, String sentAt, boolean fromMe) {}

    public record SendMessageRequest(String content, String type) {}

    public record MessageResponse(
            UUID id,
            UUID senderId,
            String content,
            String type,
            String sentAt,
            boolean read
    ) {}
}
