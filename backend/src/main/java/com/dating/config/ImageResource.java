package com.dating.config;

import com.dating.entity.UserProfile;
import com.dating.service.IImageService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import io.smallrye.common.annotation.RunOnVirtualThread;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.jboss.resteasy.reactive.RestForm;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

/**
 * Image Upload & Serving endpoint.
 *
 * File uploads are binary multipart — one of the few things that stays REST
 * even in a gRPC-first architecture. Once uploaded, the photo URL is added
 * to the user's profile via the gRPC ProfileService.UpdateProfile RPC.
 */
@Path("/uploads")
@RunOnVirtualThread
public class ImageResource {

    @Inject
    IImageService imageService;

    /**
     * Upload a photo for a user profile.
     * POST /uploads/photos?userId=<uuid>
     * Content-Type: multipart/form-data
     */
    @POST
    @Path("/photos")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response uploadPhoto(
            @RestForm("file") FileUpload file,
            @RestForm("userId") String userId) {

        if (file == null) {
            return Response.status(400)
                    .entity(Map.of("message", "No file provided")).build();
        }

        if (userId == null || userId.isBlank()) {
            return Response.status(400)
                    .entity(Map.of("message", "userId is required")).build();
        }

        try {
            UUID uid = UUID.fromString(userId);
            UserProfile user = UserProfile.findById(uid);
            if (user == null) {
                return Response.status(404)
                        .entity(Map.of("message", "User not found")).build();
            }

            String contentType = file.contentType();
            String url;
            try (var stream = new FileInputStream(file.uploadedFile().toFile())) {
                url = imageService.saveImage(stream, contentType, userId);
            }

            // Add to user's photo list
            user.photoUrls.add(url);
            user.persist();

            return Response.ok(Map.of(
                    "url", url,
                    "photo_urls", user.photoUrls,
                    "total_photos", user.photoUrls.size()
            )).build();

        } catch (IllegalArgumentException e) {
            return Response.status(400)
                    .entity(Map.of("message", e.getMessage())).build();
        } catch (IOException e) {
            return Response.status(500)
                    .entity(Map.of("message", "Failed to save image")).build();
        }
    }

    /**
     * Delete a photo from a user's profile.
     * DELETE /uploads/photos?userId=<uuid>&photoIndex=<index>
     */
    @DELETE
    @Path("/photos")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response deletePhoto(
            @QueryParam("userId") String userId,
            @QueryParam("photoIndex") int photoIndex) {

        try {
            UUID uid = UUID.fromString(userId);
            UserProfile user = UserProfile.findById(uid);
            if (user == null) {
                return Response.status(404)
                        .entity(Map.of("message", "User not found")).build();
            }

            if (photoIndex < 0 || photoIndex >= user.photoUrls.size()) {
                return Response.status(400)
                        .entity(Map.of("message", "Invalid photo index")).build();
            }

            String removedUrl = user.photoUrls.remove(photoIndex);
            user.persist();

            // Delete file from disk if it's a local upload
            if (removedUrl.startsWith("/uploads/")) {
                imageService.deleteImage(removedUrl);
            }

            return Response.ok(Map.of(
                    "removed", removedUrl,
                    "photo_urls", user.photoUrls
            )).build();

        } catch (Exception e) {
            return Response.status(500)
                    .entity(Map.of("message", e.getMessage())).build();
        }
    }

    /**
     * Serve uploaded photos.
     * GET /uploads/photos/{filename}
     */
    @GET
    @Path("/photos/{filename}")
    public Response servePhoto(@PathParam("filename") String filename) {
        try {
            java.nio.file.Path filePath = imageService.getImagePath(filename);
            if (!Files.exists(filePath)) {
                return Response.status(404).build();
            }

            String contentType = Files.probeContentType(filePath);
            if (contentType == null) contentType = "image/jpeg";

            byte[] data = Files.readAllBytes(filePath);
            return Response.ok(data)
                    .type(contentType)
                    .header("Cache-Control", "public, max-age=86400")
                    .build();

        } catch (IOException e) {
            return Response.status(500).build();
        }
    }

    /**
     * Reorder photos for a user profile.
     * POST /uploads/photos/reorder
     */
    @POST
    @Path("/photos/reorder")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response reorderPhotos(Map<String, Object> body) {
        try {
            String userId = body.get("userId").toString();
            @SuppressWarnings("unchecked")
            List<String> newOrder = (List<String>) body.get("photo_urls");

            UUID uid = UUID.fromString(userId);
            UserProfile user = UserProfile.findById(uid);
            if (user == null) {
                return Response.status(404)
                        .entity(Map.of("message", "User not found")).build();
            }

            user.photoUrls.clear();
            user.photoUrls.addAll(newOrder);
            user.persist();

            return Response.ok(Map.of("photo_urls", user.photoUrls)).build();

        } catch (Exception e) {
            return Response.status(400)
                    .entity(Map.of("message", e.getMessage())).build();
        }
    }
}
