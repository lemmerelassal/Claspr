package com.dating.service.impl;

import com.dating.dto.UserDtos.LastMessageDto;
import com.dating.dto.UserDtos.MatchResponse;
import com.dating.dto.UserDtos.ProfileResponse;
import com.dating.entity.ChatMessageEntity;
import com.dating.entity.Match;
import com.dating.entity.UserProfile;
import com.dating.service.IGeoService;
import com.dating.service.IMatchService;
import com.dating.service.IProfileService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class MatchServiceImpl implements IMatchService {

    @Inject IProfileService profileService;
    @Inject IGeoService geoService;

    @Override
    public List<MatchResponse> getMatches(UUID userId) {
        UserProfile me = profileService.getById(userId);
        return Match.findByUserId(userId).stream()
                .map(m -> toMatchResponse(m, userId, me))
                .toList();
    }

    @Override
    @Transactional
    public void unmatch(UUID userId, UUID matchId) {
        Match match = Match.findById(matchId);
        if (match == null) throw new IllegalArgumentException("Match not found");
        if (!match.user1.id.equals(userId) && !match.user2.id.equals(userId)) {
            throw new SecurityException("Not authorized");
        }
        match.active = false;
        match.persist();
    }

    private MatchResponse toMatchResponse(Match m, UUID userId, UserProfile me) {
        UserProfile other = m.getOtherUser(userId);
        ChatMessageEntity lastMsg = ChatMessageEntity.findLastByMatchId(m.id);
        long unreadCount = ChatMessageEntity.countUnread(m.id, userId);

        LastMessageDto lastMessageDto = lastMsg == null ? null : new LastMessageDto(
                lastMsg.content, lastMsg.sentAt.toString(), lastMsg.sender.id.equals(userId)
        );

        double distance = geoService.calculateDistance(me, other);
        var profile = new ProfileResponse(
                other.id, other.displayName, other.getAge(), other.bio,
                other.gender, other.photoUrls, other.interests,
                other.city, Math.round(distance * 10.0) / 10.0
        );

        return new MatchResponse(m.id, profile, m.matchedAt.toString(), lastMessageDto, unreadCount);
    }
}
