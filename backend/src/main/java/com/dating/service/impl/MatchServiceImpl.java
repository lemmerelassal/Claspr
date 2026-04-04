package com.dating.service.impl;

import com.dating.dto.UserDtos.*;
import com.dating.entity.ChatMessageEntity;
import com.dating.entity.Match;
import com.dating.entity.UserProfile;
import com.dating.service.IMatchService;
import com.dating.service.IProfileService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class MatchServiceImpl implements IMatchService {

    @Inject
    IProfileService profileService;

    @Override
    public List<MatchResponse> getMatches(UUID userId) {
        UserProfile me = profileService.getById(userId);
        List<Match> matches = Match.findByUserId(userId);

        return matches.stream().map(m -> {
            UserProfile other = m.getOtherUser(userId);
            ChatMessageEntity lastMsg = ChatMessageEntity.findLastByMatchId(m.id);
            long unreadCount = ChatMessageEntity.countUnread(m.id, userId);

            LastMessageDto lastMessageDto = null;
            if (lastMsg != null) {
                lastMessageDto = new LastMessageDto(
                        lastMsg.content, lastMsg.sentAt.toString(),
                        lastMsg.sender.id.equals(userId)
                );
            }

            double distance = profileService.calculateDistance(me, other);
            var profile = new ProfileResponse(
                    other.id, other.displayName, other.getAge(), other.bio,
                    other.gender, other.photoUrls, other.interests,
                    other.city, Math.round(distance * 10.0) / 10.0
            );

            return new MatchResponse(m.id, profile, m.matchedAt.toString(), lastMessageDto, unreadCount);
        }).collect(Collectors.toList());
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
}
