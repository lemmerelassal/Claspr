package com.dating.grpc;

import com.dating.entity.UserProfile;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import io.quarkus.grpc.GrpcService;
import jakarta.transaction.Transactional;
import java.util.UUID;

@GrpcService
public class ProfileGrpcService extends ProfileServiceGrpc.ProfileServiceImplBase {

    @Override
    public void getMyProfile(ProfileRequest request, StreamObserver<ProfileResponse> responseObserver) {
        try {
            UUID userId = UUID.fromString(request.getUserId());
            UserProfile user = UserProfile.findById(userId);
            if (user == null) {
                responseObserver.onError(Status.NOT_FOUND
                        .withDescription("Profile not found").asRuntimeException());
                return;
            }
            responseObserver.onNext(toProfileResponse(user, 0));
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void getProfile(ProfileByIdRequest request, StreamObserver<ProfileResponse> responseObserver) {
        try {
            UUID targetId = UUID.fromString(request.getTargetUserId());
            UUID viewerId = UUID.fromString(request.getUserId());
            UserProfile target = UserProfile.findById(targetId);
            UserProfile viewer = UserProfile.findById(viewerId);

            if (target == null) {
                responseObserver.onError(Status.NOT_FOUND
                        .withDescription("Profile not found").asRuntimeException());
                return;
            }

            double distance = 0;
            if (viewer != null && viewer.latitude != null && target.latitude != null) {
                distance = haversine(viewer.latitude, viewer.longitude,
                        target.latitude, target.longitude);
            }

            responseObserver.onNext(toProfileResponse(target, distance));
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    @Transactional
    public void updateProfile(UpdateProfileRequest request, StreamObserver<ProfileResponse> responseObserver) {
        try {
            UUID userId = UUID.fromString(request.getUserId());
            UserProfile user = UserProfile.findById(userId);
            if (user == null) {
                responseObserver.onError(Status.NOT_FOUND
                        .withDescription("Profile not found").asRuntimeException());
                return;
            }

            if (!request.getDisplayName().isEmpty()) user.displayName = request.getDisplayName();
            if (!request.getBio().isEmpty()) user.bio = request.getBio();
            if (request.getGenderPreference() != Gender.UNKNOWN) {
                user.genderPreference = mapGender(request.getGenderPreference());
            }
            if (request.getLatitude() != 0) user.latitude = request.getLatitude();
            if (request.getLongitude() != 0) user.longitude = request.getLongitude();
            if (!request.getCity().isEmpty()) user.city = request.getCity();
            if (request.getMaxDistanceKm() > 0) user.maxDistanceKm = request.getMaxDistanceKm();
            if (request.getMinAgePreference() > 0) user.minAgePreference = request.getMinAgePreference();
            if (request.getMaxAgePreference() > 0) user.maxAgePreference = request.getMaxAgePreference();
            if (request.getPhotoUrlsCount() > 0) {
                user.photoUrls.clear();
                user.photoUrls.addAll(request.getPhotoUrlsList());
            }
            if (request.getInterestsCount() > 0) {
                user.interests.clear();
                user.interests.addAll(request.getInterestsList());
            }

            user.persist();
            responseObserver.onNext(toProfileResponse(user, 0));
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    @Transactional
    public void updateLocation(LocationUpdate request, StreamObserver<ProfileResponse> responseObserver) {
        try {
            UUID userId = UUID.fromString(request.getUserId());
            UserProfile user = UserProfile.findById(userId);
            if (user == null) {
                responseObserver.onError(Status.NOT_FOUND
                        .withDescription("Profile not found").asRuntimeException());
                return;
            }

            user.latitude = request.getLatitude();
            user.longitude = request.getLongitude();
            if (!request.getCity().isEmpty()) user.city = request.getCity();
            user.persist();

            responseObserver.onNext(toProfileResponse(user, 0));
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription(e.getMessage()).asRuntimeException());
        }
    }

    private ProfileResponse toProfileResponse(UserProfile user, double distance) {
        ProfileResponse.Builder builder = ProfileResponse.newBuilder()
                .setUserId(user.id.toString())
                .setDisplayName(user.displayName)
                .setAge(user.getAge())
                .setBio(user.bio != null ? user.bio : "")
                .setGender(mapGenderToProto(user.gender))
                .setCity(user.city != null ? user.city : "")
                .setDistanceKm(Math.round(distance * 10.0) / 10.0)
                .setMaxDistanceKm(user.maxDistanceKm != null ? user.maxDistanceKm : 50)
                .setMinAgePreference(user.minAgePreference != null ? user.minAgePreference : 18)
                .setMaxAgePreference(user.maxAgePreference != null ? user.maxAgePreference : 99);

        if (user.latitude != null) builder.setLatitude(user.latitude);
        if (user.longitude != null) builder.setLongitude(user.longitude);
        if (user.genderPreference != null) builder.setGenderPreference(mapGenderToProto(user.genderPreference));
        user.photoUrls.forEach(builder::addPhotoUrls);
        user.interests.forEach(builder::addInterests);

        return builder.build();
    }

    private Gender mapGenderToProto(UserProfile.Gender g) {
        if (g == null) return Gender.UNKNOWN;
        return switch (g) {
            case MALE -> Gender.MALE;
            case FEMALE -> Gender.FEMALE;
            case NON_BINARY -> Gender.NON_BINARY;
            case OTHER -> Gender.OTHER;
        };
    }

    private UserProfile.Gender mapGender(Gender proto) {
        return switch (proto) {
            case MALE -> UserProfile.Gender.MALE;
            case FEMALE -> UserProfile.Gender.FEMALE;
            case NON_BINARY -> UserProfile.Gender.NON_BINARY;
            default -> UserProfile.Gender.OTHER;
        };
    }

    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }
}
