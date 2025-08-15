package com.project.yuklet.controller;

import com.project.yuklet.dto.ApiResponse;
import com.project.yuklet.dto.MessageDto;
import com.project.yuklet.entities.Conversation;
import com.project.yuklet.entities.Message;
import com.project.yuklet.services.MessageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "*", maxAge = 3600)
public class MessageController {
    
    @Autowired
    private MessageService messageService;
    
    @PostMapping("/conversation")
    public ResponseEntity<ApiResponse<Conversation>> createConversation(@RequestParam Long otherUserId,
                                                                       @RequestParam Long cargoRequestId) {
        Conversation conversation = messageService.createConversation(otherUserId, cargoRequestId);
        return ResponseEntity.ok(ApiResponse.success("Conversation created successfully", conversation));
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<Message>> sendMessage(@Valid @RequestBody MessageDto messageDto) {
        Message message = messageService.sendMessage(messageDto);
        return ResponseEntity.ok(ApiResponse.success("Message sent successfully", message));
    }
    
    @GetMapping("/conversation/{conversationId}")
    public ResponseEntity<ApiResponse<List<Message>>> getConversationMessages(@PathVariable Long conversationId) {
        List<Message> messages = messageService.getConversationMessages(conversationId);
        return ResponseEntity.ok(ApiResponse.success(messages));
    }
    
    @GetMapping("/conversations")
    public ResponseEntity<ApiResponse<List<Conversation>>> getUserConversations() {
        List<Conversation> conversations = messageService.getUserConversations();
        return ResponseEntity.ok(ApiResponse.success(conversations));
    }
    
    @PutMapping("/conversation/{conversationId}/mark-read")
    public ResponseEntity<ApiResponse<String>> markMessagesAsRead(@PathVariable Long conversationId) {
        messageService.markMessagesAsRead(conversationId);
        return ResponseEntity.ok(ApiResponse.success("Messages marked as read", null));
    }
    
    @GetMapping("/conversation/{conversationId}/unread-count")
    public ResponseEntity<ApiResponse<Long>> getUnreadMessageCount(@PathVariable Long conversationId) {
        Long count = messageService.getUnreadMessageCount(conversationId);
        return ResponseEntity.ok(ApiResponse.success(count));
    }
}
