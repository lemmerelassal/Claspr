package com.dating.service;

import com.dating.dto.UserDtos.MatchResponse;
import java.util.List;
import java.util.UUID;

/**
 * Match service contract.
 * Manages existing matches (list, unmatch).
 */
public interface IMatchService {

    List<MatchResponse> getMatches(UUID userId);

    void unmatch(UUID userId, UUID matchId);
}
