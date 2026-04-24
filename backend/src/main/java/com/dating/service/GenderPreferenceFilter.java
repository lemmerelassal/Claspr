package com.dating.service;

import com.dating.entity.UserProfile;
import java.util.List;

public interface GenderPreferenceFilter {
    List<UserProfile.Gender> getAllowedGenders(UserProfile user);
}
