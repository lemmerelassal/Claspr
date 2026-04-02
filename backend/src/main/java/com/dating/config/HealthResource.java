package com.dating.config;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Map;

/**
 * Minimal REST — only health/info. All business logic is served via gRPC.
 */
@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
public class HealthResource {

    @GET
    @Path("/info")
    public Response info() {
        return Response.ok(Map.of(
                "service", "dating-app",
                "transport", "gRPC + gRPC-Web",
                "grpcPort", 9000,
                "status", "running"
        )).build();
    }
}
