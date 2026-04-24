package com.dating.config.proxy;

import com.dating.entity.UserProfile;
import com.dating.service.IAuthService;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Map;

@Path("/grpc/AuthService")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RunOnVirtualThread
public class AuthProxy {

    @Inject
    IAuthService authService;

    @POST
    @Path("/Login")
    public Response login(Map<String, Object> body) {
        try {
            var result = authService.login(
                    ProfileMapper.str(body, "email"),
                    ProfileMapper.str(body, "password")
            );
            return Response.ok(Map.of(
                    "token", result.token(),
                    "user_id", result.userId().toString(),
                    "display_name", result.displayName()
            )).build();
        } catch (SecurityException e) {
            return Response.status(401).entity(Map.of("message", "Invalid credentials")).build();
        }
    }

    @POST
    @Path("/Register")
    public Response register(Map<String, Object> body) {
        try {
            String genderStr = ProfileMapper.str(body, "gender");
            UserProfile.Gender gender = genderStr.isBlank()
                    ? UserProfile.Gender.OTHER
                    : UserProfile.Gender.valueOf(genderStr);

            var result = authService.register(
                    ProfileMapper.str(body, "email"),
                    ProfileMapper.str(body, "password"),
                    ProfileMapper.str(body, "display_name"),
                    ProfileMapper.str(body, "date_of_birth"),
                    gender
            );
            return Response.ok(Map.of(
                    "token", result.token(),
                    "user_id", result.userId().toString(),
                    "display_name", result.displayName()
            )).build();
        } catch (IllegalArgumentException e) {
            return Response.status(409).entity(Map.of("message", e.getMessage())).build();
        }
    }

    @POST
    @Path("/ValidateToken")
    public Response validateToken(Map<String, Object> body) {
        var result = authService.validateToken(ProfileMapper.str(body, "token"));
        return Response.ok(Map.of(
                "valid", result.valid(),
                "user_id", result.userId() != null ? result.userId() : ""
        )).build();
    }
}
