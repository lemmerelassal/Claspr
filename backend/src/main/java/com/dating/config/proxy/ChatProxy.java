package com.dating.config.proxy;

import com.dating.service.IChatService;
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

@Path("/grpc/ChatService")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RunOnVirtualThread
public class ChatProxy {

    @Inject
    IChatService chatService;

    @POST
    @Path("/SendMessage")
    public Response sendMessage(Map<String, Object> body) {
        var msg = chatService.sendMessage(
                uuid(body, "match_id"),
                uuid(body, "sender_id"),
                str(body, "content"),
                body.containsKey("type") ? str(body, "type") : "TEXT"
        );
        return Response.ok(Map.of(
                "message_id", msg.id().toString(),
                "sent_at", msg.sentAt(),
                "delivered", true
        )).build();
    }

    @POST
    @Path("/GetConversation")
    public Response getConversation(Map<String, Object> body) {
        int page = body.containsKey("page") ? intVal(body, "page") : 0;
        int size = body.containsKey("size") ? intVal(body, "size") : 50;
        var messages = chatService.getConversation(
                uuid(body, "match_id"),
                uuid(body, "user_id"),
                page, size
        );
        var msgList = messages.stream().map(m -> Map.of(
                "message_id", (Object) m.id().toString(),
                "sender_id", m.senderId().toString(),
                "content", m.content(),
                "type", m.type(),
                "sent_at", m.sentAt(),
                "read", m.read()
        )).toList();
        return Response.ok(Map.of("messages", msgList, "total", msgList.size())).build();
    }

    @POST
    @Path("/MarkRead")
    public Response markRead(Map<String, Object> body) {
        chatService.markAsRead(uuid(body, "match_id"), uuid(body, "user_id"));
        return Response.ok(Map.of("marked_count", 1)).build();
    }
}
