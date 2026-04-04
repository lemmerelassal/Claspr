package com.dating.service;

import com.dating.dto.UserDtos.SwipeResponse;
import java.util.UUID;

/**
 * Swipe service contract.
 * Records swipe actions and detects mutual matches.
 */
public interface ISwipeService {

    SwipeResponse recordSwipe(UUID swiperId, UUID swipedId, String direction);
}
