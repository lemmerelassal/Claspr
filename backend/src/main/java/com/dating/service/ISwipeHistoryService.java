package com.dating.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Swipe history service contract.
 * Provides filtered access to past swipe actions.
 */
public interface ISwipeHistoryService {

    List<Map<String, Object>> getSwipeHistory(UUID userId, String direction, String namePrefix, String interest);

    List<String> autocompleteName(UUID userId, String prefix);
}
