package com.dating.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "user_profiles")
public class UserProfile extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;

    @Column(nullable = false, unique = true)
    public String email;

    @Column(nullable = false)
    public String passwordHash;

    @Column(nullable = false)
    public String displayName;

    public LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    public Gender gender;

    @Enumerated(EnumType.STRING)
    public Gender genderPreference;

    // Discovery filter toggles
    public boolean showMen = true;
    public boolean showWomen = true;
    public boolean showMtfTrans = true;
    public boolean showFtmTrans = true;
    public boolean showNonBinary = true;

    @Column(length = 500)
    public String bio;

    public Double latitude;
    public Double longitude;
    public String city;

    public Integer maxDistanceKm = 50;
    public Integer minAgePreference = 18;
    public Integer maxAgePreference = 99;

    // Weight stored in kg internally
    public Double weight;
    public String weightUnit = "kg"; // "kg" or "lb"

    // Height stored in cm internally
    public Double height;
    public String heightUnit = "cm"; // "cm" or "ft"

    // Filter preferences for weight (stored in kg)
    public Double minWeightPreference;
    public Double maxWeightPreference;

    // Filter preferences for height (stored in cm)
    public Double minHeightPreference;
    public Double maxHeightPreference;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_photos", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "photo_url")
    @OrderColumn(name = "sort_order")
    public List<String> photoUrls = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_interests", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "interest")
    public Set<String> interests = new HashSet<>();

    public boolean active = true;
    public boolean verified = false;

    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public int getAge() {
        if (dateOfBirth == null) return 0;
        return java.time.Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    // Panache finder methods
    public static UserProfile findByEmail(String email) {
        return find("email", email).firstResult();
    }

    public enum Gender {
        MALE, FEMALE, MTF_TRANS, FTM_TRANS, NON_BINARY, OTHER
    }
}
