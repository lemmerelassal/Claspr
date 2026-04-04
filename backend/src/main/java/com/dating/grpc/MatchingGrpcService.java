package com.dating.grpc;

import com.dating.service.IDiscoveryService;
import com.dating.service.ISwipeService;
import com.dating.service.IMatchService;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import io.quarkus.grpc.GrpcService;
import jakarta.inject.Inject;
import java.util.UUID;

@GrpcService
public class MatchingGrpcService extends MatchingServiceGrpc.MatchingServiceImplBase {

    @Inject
    IDiscoveryService discoveryService;

    @Inject
    ISwipeService swipeService;

    @Inject
    IMatchService matchService;

    @Override
    public void getPotentialMatches(MatchRequest request, StreamObserver<MatchResponse> responseObserver) {
        try {
            UUID userId = UUID.fromString(request.getUserId());
            int limit = request.getLimit() > 0 ? request.getLimit() : 10;

            var profiles = discoveryService.getPotentialMatches(userId, limit);

            MatchResponse.Builder builder = MatchResponse.newBuilder();
            profiles.forEach(p -> {
                ProfileCard.Builder card = ProfileCard.newBuilder()
                        .setUserId(p.id().toString())
                        .setDisplayName(p.displayName())
                        .setAge(p.age())
                        .setBio(p.bio() != null ? p.bio() : "")
                        .setDistanceKm(p.distanceKm())
                        .setLocationCity(p.city() != null ? p.city() : "");
                p.photoUrls().forEach(card::addPhotoUrls);
                p.interests().forEach(card::addInterests);
                builder.addProfiles(card.build());
            });

            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void recordSwipe(SwipeRequest request, StreamObserver<SwipeResponse> responseObserver) {
        try {
            UUID swiperId = UUID.fromString(request.getSwiperId());
            UUID swipedId = UUID.fromString(request.getSwipedId());
            String direction = request.getDirection().name();

            var result = swipeService.recordSwipe(swiperId, swipedId, direction);

            SwipeResponse.Builder builder = SwipeResponse.newBuilder()
                    .setIsMatch(result.isMatch());

            if (result.matchId() != null) {
                builder.setMatchId(result.matchId().toString());
            }
            if (result.matchedProfile() != null) {
                var p = result.matchedProfile();
                ProfileCard.Builder card = ProfileCard.newBuilder()
                        .setUserId(p.id().toString())
                        .setDisplayName(p.displayName())
                        .setAge(p.age())
                        .setBio(p.bio() != null ? p.bio() : "")
                        .setDistanceKm(p.distanceKm())
                        .setLocationCity(p.city() != null ? p.city() : "");
                if (p.photoUrls() != null) p.photoUrls().forEach(card::addPhotoUrls);
                if (p.interests() != null) p.interests().forEach(card::addInterests);
                builder.setMatchedProfile(card.build());
            }

            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void getMatches(GetMatchesRequest request, StreamObserver<GetMatchesResponse> responseObserver) {
        try {
            UUID userId = UUID.fromString(request.getUserId());
            var matches = matchService.getMatches(userId);

            GetMatchesResponse.Builder builder = GetMatchesResponse.newBuilder()
                    .setTotal(matches.size());

            matches.forEach(m -> {
                MatchEntry.Builder entry = MatchEntry.newBuilder()
                        .setMatchId(m.matchId().toString())
                        .setMatchedAt(m.matchedAt())
                        .setProfile(ProfileCard.newBuilder()
                                .setUserId(m.profile().id().toString())
                                .setDisplayName(m.profile().displayName())
                                .setAge(m.profile().age())
                                .setBio(m.profile().bio() != null ? m.profile().bio() : "")
                                .setDistanceKm(m.profile().distanceKm())
                                .setLocationCity(m.profile().city() != null ? m.profile().city() : "")
                                .build());

                if (m.lastMessage() != null) {
                    entry.setLastMessage(ChatMessage.newBuilder()
                            .setContent(m.lastMessage().content())
                            .setSentAt(m.lastMessage().sentAt())
                            .setSenderId(m.lastMessage().fromMe() ? userId.toString() : "other")
                            .build());
                }

                builder.addMatches(entry.build());
            });

            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void unmatchUser(UnmatchRequest request, StreamObserver<UnmatchResponse> responseObserver) {
        try {
            UUID userId = UUID.fromString(request.getUserId());
            UUID matchId = UUID.fromString(request.getMatchId());
            matchService.unmatch(userId, matchId);

            responseObserver.onNext(UnmatchResponse.newBuilder().setSuccess(true).build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription(e.getMessage()).asRuntimeException());
        }
    }
}
