package com.project.yuklet.security;

import com.project.yuklet.entities.Conversation;
import com.project.yuklet.entities.User;
import com.project.yuklet.reporsitory.ConversationRepository;
import com.project.yuklet.reporsitory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Principal;
import java.util.Optional;

@Component
public class WebSocketAuthChannelInterceptor implements ChannelInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketAuthChannelInterceptor.class);

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        try {
            StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
            if (accessor == null) {
                return message;
            }

            StompCommand command = accessor.getCommand();
            if (command == null) {
                return message;
            }

            switch (command) {
                case CONNECT -> handleConnect(accessor);
                case SUBSCRIBE -> handleSubscribe(accessor);
                case SEND -> handleSend(accessor);
                default -> {}
            }

            return message;
        } catch (Exception e) {
            logger.error("WebSocket interceptor error: {}", e.getMessage(), e);
            throw e;
        }
    }

    private void handleConnect(StompHeaderAccessor accessor) {
        try {
            String authHeader = getFirstNativeHeader(accessor, "Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                logger.warn("Authorization header missing for WS CONNECT");
                throw new AccessDeniedException("Authorization header missing for WS CONNECT");
            }
            String token = authHeader.substring(7);
            if (!jwtUtils.validateJwtToken(token)) {
                logger.warn("Invalid JWT for WS CONNECT");
                throw new AccessDeniedException("Invalid JWT for WS CONNECT");
            }
            String email = jwtUtils.getUserNameFromJwtToken(token);
            User user = userRepository.findByEmail(email).orElseThrow(() -> {
                logger.warn("User not found for email: {}", email);
                return new AccessDeniedException("User not found");
            });
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            accessor.setUser(authentication);
            logger.info("WebSocket CONNECT successful for user: {}", email);
        } catch (Exception e) {
            logger.error("WebSocket CONNECT failed: {}", e.getMessage());
            throw e;
        }
    }

    private void handleSubscribe(StompHeaderAccessor accessor) {
        try {
            Principal principal = accessor.getUser();
            if (principal == null) {
                logger.warn("Not authenticated for SUBSCRIBE");
                throw new AccessDeniedException("Not authenticated for SUBSCRIBE");
            }
            String destination = accessor.getDestination();
            if (destination == null) {
                return;
            }
            
            // Genel topic'lere izin ver
            if (destination.equals("/topic/messages")) {
                logger.info("General topic subscription allowed for user: {}", principal.getName());
                return;
            }
            
            // Conversation bazlı topic'lerde üyelik kontrolü yapalım
            if (destination.startsWith("/topic/conversation-")) {
                Long conversationId = parseConversationId(destination, "/topic/conversation-");
                assertConversationMembership(principal, conversationId);
                logger.info("Conversation subscription allowed for user: {} to conversation: {}", principal.getName(), conversationId);
            }
        } catch (Exception e) {
            logger.error("WebSocket SUBSCRIBE failed: {}", e.getMessage());
            throw e;
        }
    }

    private void handleSend(StompHeaderAccessor accessor) {
        try {
            Principal principal = accessor.getUser();
            if (principal == null) {
                logger.warn("Not authenticated for SEND");
                throw new AccessDeniedException("Not authenticated for SEND");
            }
            String destination = accessor.getDestination();
            if (destination == null) {
                return;
            }
            
            // Genel mesajlara izin ver
            if (destination.equals("/chat/send")) {
                logger.info("General message send allowed for user: {}", principal.getName());
                return;
            }
            
            // Conversation bazlı mesajlarda üyelik kontrolü yapalım
            if (destination.startsWith("/chat/sendTo/conversation-")) {
                Long conversationId = parseConversationId(destination, "/chat/sendTo/conversation-");
                assertConversationMembership(principal, conversationId);
                logger.info("Conversation message send allowed for user: {} to conversation: {}", principal.getName(), conversationId);
            }
        } catch (Exception e) {
            logger.error("WebSocket SEND failed: {}", e.getMessage());
            throw e;
        }
    }

    private Long parseConversationId(String destination, String prefix) {
        try {
            String idPart = destination.substring(prefix.length());
            // idPart may have trailing path segments, only take number at start
            int slashIdx = idPart.indexOf('/');
            if (slashIdx > -1) {
                idPart = idPart.substring(0, slashIdx);
            }
            return Long.parseLong(idPart);
        } catch (Exception e) {
            logger.error("Invalid conversation destination: {}", destination);
            throw new AccessDeniedException("Invalid conversation destination: " + destination);
        }
    }

    private void assertConversationMembership(Principal principal, Long conversationId) {
        try {
            if (!(principal instanceof UsernamePasswordAuthenticationToken auth) || !(auth.getPrincipal() instanceof User user)) {
                logger.warn("Invalid principal type for conversation membership check");
                throw new AccessDeniedException("Invalid principal");
            }
            
            Optional<Conversation> optional = conversationRepository.findById(conversationId);
            if (optional.isEmpty()) {
                logger.warn("Conversation not found: {}", conversationId);
                throw new AccessDeniedException("Conversation not found: " + conversationId);
            }
            
            Conversation conv = optional.get();
            Long uid = user.getId();
            if (!uid.equals(conv.getUser1Id()) && !uid.equals(conv.getUser2Id())) {
                logger.warn("User {} is not a participant of conversation {}", uid, conversationId);
                throw new AccessDeniedException("User is not a participant of this conversation");
            }
            
            logger.info("Conversation membership verified for user: {} in conversation: {}", uid, conversationId);
        } catch (Exception e) {
            logger.error("Conversation membership check failed: {}", e.getMessage());
            throw e;
        }
    }

    private String getFirstNativeHeader(StompHeaderAccessor accessor, String name) {
        if (accessor.getNativeHeader(name) == null || accessor.getNativeHeader(name).isEmpty()) {
            return null;
        }
        return accessor.getNativeHeader(name).get(0);
    }
}


