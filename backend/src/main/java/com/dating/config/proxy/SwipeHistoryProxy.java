package com.dating.config.proxy;

import com.dating.service.ISwipeHistoryService;
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

@Path("/grpc/SwipeHistoryService")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RunOnVirtualThread
public class SwipeHistoryProxy {

    @Inject
    ISwipeHistoryService historyService;

    @POST
    @Path("/GetHistory")
    public Response getHistory(Map<String, Object> body) {
        var userId = uuid(body, "user_id");
        String direction  = body.containsKey("direction")   ? str(body, "direction")   : "ALL";
        String namePrefix = body.containsKey("name_prefix") ? str(body, "name_prefix") : "";
        String interest   = body.containsKey("interest")    ? str(body, "interest")    : "";

        var history = historyService.getSwipeHistory(userId, direction, namePrefix, interest);
        return Response.ok(Map.of("swipes", history, "total", history.size())).build();
    }

    @POST
    @Path("/AutocompleteName")
    public Response autocompleteName(Map<String, Object> body) {
        var names = historyService.autocompleteName(uuid(body, "user_id"), str(body, "prefix"));
        return Response.ok(Map.of("names", names)).build();
    }
}
