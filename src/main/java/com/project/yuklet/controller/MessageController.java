package com.project.yuklet.controller;

import com.project.yuklet.dto.ApiResponse;
import com.project.yuklet.dto.ConversationSummaryDto;
import com.project.yuklet.entities.Conversation;
import com.project.yuklet.entities.Message;
import com.project.yuklet.entities.UserProfile;
import com.project.yuklet.reporsitory.ConversationRepository;
import com.project.yuklet.reporsitory.MessageRepository;
import com.project.yuklet.reporsitory.UserProfileRepository;
import com.project.yuklet.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "*", maxAge = 3600)
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @GetMapping("/conversations")
    public ResponseEntity<ApiResponse<List<ConversationSummaryDto>>> getUserConversations() {
        Long currentUserId = userService.getCurrentUser().getId();
        List<Conversation> conversations = conversationRepository
                .findByUser1IdOrUser2IdOrderByLastMessageDateDesc(currentUserId, currentUserId);

        List<ConversationSummaryDto> summaries = conversations.stream().map(conv -> {
            Long otherUserId = conv.getUser1Id().equals(currentUserId) ? conv.getUser2Id() : conv.getUser1Id();
            UserProfile profile = userProfileRepository.findByUserId(otherUserId).orElse(null);
            String firstName = profile != null ? profile.getFirstName() : null;
            String lastName = profile != null ? profile.getLastName() : null;
            String companyName = profile != null ? profile.getCompanyName() : null;
            Long unreadCount = messageRepository.countByConversationIdAndSenderIdNotAndIsReadFalse(conv.getId(), currentUserId);
            return new ConversationSummaryDto(conv.getId(), otherUserId, firstName, lastName, companyName,
                    conv.getLastMessageDate(), unreadCount);
        }).toList();

        return ResponseEntity.ok(ApiResponse.success(summaries));
    }

    @GetMapping("/conversation/{conversationId}")
    public ResponseEntity<ApiResponse<List<com.project.yuklet.dto.MessageDto>>> getConversationMessages(@PathVariable Long conversationId) {
        // Conversation üyeliğini kontrol et
        Long currentUserId = userService.getCurrentUser().getId();
        Optional<Conversation> conversation = conversationRepository.findById(conversationId);
        
        if (conversation.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Conversation not found"));
        }
        
        Conversation conv = conversation.get();
        if (!conv.getUser1Id().equals(currentUserId) && !conv.getUser2Id().equals(currentUserId)) {
            return ResponseEntity.badRequest().body(ApiResponse.error("User is not a participant of this conversation"));
        }
        
        List<Message> messages = messageRepository.findByConversationIdOrderBySentDateAsc(conversationId);

        List<com.project.yuklet.dto.MessageDto> dtos = messages.stream().map(m -> {
            UserProfile sp = userProfileRepository.findByUserId(m.getSenderId()).orElse(null);
            String firstName = sp != null ? sp.getFirstName() : null;
            String lastName = sp != null ? sp.getLastName() : null;
            String email = userService.getUserById(m.getSenderId()).getEmail();
            return new com.project.yuklet.dto.MessageDto(
                    m.getId(),
                    m.getConversationId(),
                    m.getSenderId(),
                    firstName,
                    lastName,
                    email,
                    m.getContent(),
                    m.getMessageType(),
                    m.getIsRead(),
                    m.getSentDate()
            );
        }).collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success(dtos));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Message>> sendMessage(@RequestBody Message message) {
        // Conversation üyeliğini kontrol et
        Long currentUserId = userService.getCurrentUser().getId();
        Optional<Conversation> conversation = conversationRepository.findById(message.getConversationId());
        
        if (conversation.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Conversation not found"));
        }
        
        Conversation conv = conversation.get();
        if (!conv.getUser1Id().equals(currentUserId) && !conv.getUser2Id().equals(currentUserId)) {
            return ResponseEntity.badRequest().body(ApiResponse.error("User is not a participant of this conversation"));
        }
        
        message.setSenderId(currentUserId);
        message.setIsRead(false);
        Message savedMessage = messageRepository.save(message);
        
        // Conversation'ın last message date'ini güncelle
        conv.setLastMessageDate(java.time.LocalDateTime.now());
        conversationRepository.save(conv);
        
        return ResponseEntity.ok(ApiResponse.success("Message sent successfully", savedMessage));
    }

    @PutMapping("/conversation/{conversationId}/mark-read")
    public ResponseEntity<ApiResponse<String>> markConversationAsRead(@PathVariable Long conversationId) {
        try {
            // Conversation ID'sinin geçerli olup olmadığını kontrol et
            if (conversationId == null || conversationId <= 0) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Invalid conversation ID"));
            }
            
            // Conversation üyeliğini kontrol et
            Long currentUserId = userService.getCurrentUser().getId();
            Optional<Conversation> conversation = conversationRepository.findById(conversationId);
            
            if (conversation.isEmpty()) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Conversation not found with ID: " + conversationId));
            }
            
            Conversation conv = conversation.get();
            if (!conv.getUser1Id().equals(currentUserId) && !conv.getUser2Id().equals(currentUserId)) {
                return ResponseEntity.badRequest().body(ApiResponse.error("User is not a participant of this conversation"));
            }
            
            // Bu conversation'daki diğer kullanıcının mesajlarını okundu işaretle
            List<Message> unreadMessages = messageRepository.findByConversationIdAndSenderIdNotAndIsReadFalse(conversationId, currentUserId);
            
            for (Message message : unreadMessages) {
                message.setIsRead(true);
            }
            
            messageRepository.saveAll(unreadMessages);
            
            return ResponseEntity.ok(ApiResponse.success("Messages marked as read"));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Invalid conversation ID format: " + conversationId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error marking messages as read: " + e.getMessage()));
        }
    }

    @GetMapping("/conversation/{conversationId}/unread-count")
    public ResponseEntity<ApiResponse<Long>> getUnreadMessageCount(@PathVariable Long conversationId) {
        // Conversation üyeliğini kontrol et
        Long currentUserId = userService.getCurrentUser().getId();
        Optional<Conversation> conversation = conversationRepository.findById(conversationId);
        
        if (conversation.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Conversation not found"));
        }
        
        Conversation conv = conversation.get();
        if (!conv.getUser1Id().equals(currentUserId) && !conv.getUser2Id().equals(currentUserId)) {
            return ResponseEntity.badRequest().body(ApiResponse.error("User is not a participant of this conversation"));
        }
        
        Long unreadCount = messageRepository.countByConversationIdAndSenderIdNotAndIsReadFalse(conversationId, currentUserId);
        return ResponseEntity.ok(ApiResponse.success(unreadCount));
    }

    @PostMapping("/conversation")
    public ResponseEntity<ApiResponse<Conversation>> createConversation(
            @RequestParam Long otherUserId,
            @RequestParam(required = false) Long cargoRequestId) {
        
        Long currentUserId = userService.getCurrentUser().getId();
        
        // Aynı conversation'ın zaten var olup olmadığını kontrol et
        Optional<Conversation> existingConversation = conversationRepository.findByUser1IdAndUser2IdAndCargoRequestId(currentUserId, otherUserId, cargoRequestId);
        if (existingConversation.isEmpty()) {
            existingConversation = conversationRepository.findByUser2IdAndUser1IdAndCargoRequestId(currentUserId, otherUserId, cargoRequestId);
        }
        
        if (existingConversation.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success("Conversation already exists", existingConversation.get()));
        }
        
        // Yeni conversation oluştur
        Conversation newConversation = new Conversation(currentUserId, otherUserId, cargoRequestId);
        Conversation savedConversation = conversationRepository.save(newConversation);
        
        return ResponseEntity.ok(ApiResponse.success("Conversation created successfully", savedConversation));
    }
}
