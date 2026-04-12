package com.dating.service;

import com.dating.dto.UserDtos.ProfileResponse;
import java.util.List;
import java.util.UUID;

/**
 * Discovery service contract.
 * Responsible for finding potential matches for a user.
 */
public interface IDiscoveryService {

    List<ProfileResponse> getPotentialMatches(UUID userId, int limit);
}
