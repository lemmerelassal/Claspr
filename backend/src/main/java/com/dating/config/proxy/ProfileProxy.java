package com.dating.config.proxy;

import com.dating.service.IGeoService;
import com.dating.service.IProfileService;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Map;

import static com.dating.config.proxy.ProfileMapper.*;

@Path("/grpc/ProfileService")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RunOnVirtualThread
public class ProfileProxy {

    @Inject IProfileService profileService;
    @Inject IGeoService geoService;

    @POST
    @Path("/GetMyProfile")
    public Response getMyProfile(Map<String, Object> body) {
        try {
            var user = profileService.getById(uuid(body, "user_id"));
            return Response.ok(ProfileMapper.toMap(user, 0)).build();
        } catch (IllegalArgumentException e) {
            return Response.status(404).entity(Map.of("message", "Not found")).build();
        }
    }

    @POST
    @Path("/GetProfile")
    public Response getProfile(Map<String, Object> body) {
        try {
            var target = profileService.getById(uuid(body, "target_user_id"));
            var viewer = profileService.getById(uuid(body, "user_id"));
            double dist = geoService.calculateDistance(viewer, target);
            return Response.ok(ProfileMapper.toMap(target, dist)).build();
        } catch (IllegalArgumentException e) {
            return Response.status(404).entity(Map.of("message", "Not found")).build();
        }
    }

    @POST
    @Path("/UpdateProfile")
    public Response updateProfile(Map<String, Object> body) {
        try {
            var user = profileService.update(uuid(body, "user_id"), body);
            return Response.ok(ProfileMapper.toMap(user, 0)).build();
        } catch (IllegalArgumentException e) {
            return Response.status(404).entity(Map.of("message", "Not found")).build();
        }
    }

    @POST
    @Path("/UpdateLocation")
    public Response updateLocation(Map<String, Object> body) {
        try {
            var user = profileService.updateLocation(
                    uuid(body, "user_id"),
                    dblVal(body, "latitude"),
                    dblVal(body, "longitude"),
                    str(body, "city")
            );
            return Response.ok(ProfileMapper.toMap(user, 0)).build();
        } catch (IllegalArgumentException e) {
            return Response.status(404).entity(Map.of("message", "Not found")).build();
        }
    }
}
