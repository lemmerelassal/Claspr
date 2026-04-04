package com.dating.config;

import com.dating.entity.UserProfile;
import com.dating.service.IAuthService;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@ApplicationScoped
public class DataSeeder {

    @Inject
    IAuthService authService;

    @Transactional
    void onStart(@Observes StartupEvent ev) {
        if (UserProfile.count() > 0) return;

        // Register demo users through AuthService so password hashing is consistent
        record Seed(String name, String email, LocalDate dob, UserProfile.Gender gender,
                     String bio, double lat, double lon, String city,
                     List<String> photos, Set<String> interests) {}

        var seeds = List.of(
            new Seed("Sophie", "sophie@demo.com", LocalDate.of(1997, 3, 15), UserProfile.Gender.FEMALE,
                "Adventure seeker & coffee enthusiast. Let's explore the world together",
                49.6117, 6.1319, "Luxembourg City",
                List.of("https://images.unsplash.com/photo-1529626455594-4ff0802cfb7e?w=600",
                         "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=600"),
                Set.of("Travel", "Photography", "Coffee", "Hiking")),
            new Seed("Lucas", "lucas@demo.com", LocalDate.of(1995, 8, 22), UserProfile.Gender.MALE,
                "Chef by day, musician by night. Looking for someone who loves good food & live music",
                49.6000, 6.1333, "Luxembourg City",
                List.of("https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=600",
                         "https://images.unsplash.com/photo-1492562080023-ab3db95bfbce?w=600"),
                Set.of("Cooking", "Music", "Wine", "Concerts")),
            new Seed("Emma", "emma@demo.com", LocalDate.of(1998, 11, 5), UserProfile.Gender.FEMALE,
                "Yoga instructor who believes in good vibes only. Dog mom to a golden retriever",
                49.6200, 6.1500, "Kirchberg",
                List.of("https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=600",
                         "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=600"),
                Set.of("Yoga", "Dogs", "Wellness", "Nature")),
            new Seed("Max", "max@demo.com", LocalDate.of(1993, 5, 30), UserProfile.Gender.MALE,
                "Startup founder. Tech nerd with a soft spot for art galleries and craft beer",
                49.6050, 6.1290, "Gare",
                List.of("https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=600",
                         "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=600"),
                Set.of("Tech", "Startups", "Art", "Craft Beer")),
            new Seed("Clara", "clara@demo.com", LocalDate.of(1996, 7, 18), UserProfile.Gender.FEMALE,
                "Literature PhD student. Will judge you by your bookshelf. Fluent in 4 languages",
                49.6150, 6.1400, "Bonnevoie",
                List.of("https://images.unsplash.com/photo-1488426862026-3ee34a7d66df?w=600",
                         "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=600"),
                Set.of("Books", "Languages", "Cinema", "Philosophy")),
            new Seed("Noah", "noah@demo.com", LocalDate.of(1994, 1, 12), UserProfile.Gender.MALE,
                "Trail runner and nature photographer. Weekends are for mountains",
                49.5900, 6.1200, "Hesperange",
                List.of("https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?w=600",
                         "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=600"),
                Set.of("Running", "Photography", "Mountains", "Camping"))
        );

        for (var s : seeds) {
            // Register via AuthService to get correct password hash
            authService.register(
                s.email(), "password123", s.name(),
                s.dob().toString(), s.gender()
            );
            // Now update the extra profile fields
            UserProfile u = UserProfile.findByEmail(s.email());
            u.bio = s.bio();
            u.latitude = s.lat();
            u.longitude = s.lon();
            u.city = s.city();
            u.photoUrls = s.photos();
            u.interests = s.interests();
            u.persist();
        }
    }
}
