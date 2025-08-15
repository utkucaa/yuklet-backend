package com.project.yuklet.services;

import com.project.yuklet.dto.MessageDto;
import com.project.yuklet.entities.Conversation;
import com.project.yuklet.entities.Message;
import com.project.yuklet.entities.User;
import com.project.yuklet.exception.ResourceNotFoundException;
import com.project.yuklet.exception.UnauthorizedException;
import com.project.yuklet.reporsitory.ConversationRepository;
import com.project.yuklet.reporsitory.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    
    @Autowired
    private MessageRepository messageRepository;
    
    @Autowired
    private ConversationRepository conversationRepository;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private NotificationService notificationService;
    
    public Conversation createConversation(Long otherUserId, Long cargoRequestId) {
        User currentUser = userService.getCurrentUser();
        
        Optional<Conversation> existingConversation = conversationRepository.findByUser1IdAndUser2IdAndCargoRequestId(currentUser.getId(), otherUserId, cargoRequestId);
        if (existingConversation.isEmpty()) {
            existingConversation = conversationRepository.findByUser2IdAndUser1IdAndCargoRequestId(currentUser.getId(), otherUserId, cargoRequestId);
        }
        
        return existingConversation.orElseGet(() -> {
            Conversation newConversation = new Conversation(currentUser.getId(), otherUserId, cargoRequestId);
            return conversationRepository.save(newConversation);
        });
    }
    
    @Transactional
    public Message sendMessage(MessageDto messageDto) {
        User currentUser = userService.getCurrentUser();
        
        Conversation conversation = conversationRepository.findById(messageDto.getConversationId())
                .orElseThrow(() -> new ResourceNotFoundException("Conversation", "id", messageDto.getConversationId()));
        
        if (!conversation.getUser1Id().equals(currentUser.getId()) && 
            !conversation.getUser2Id().equals(currentUser.getId())) {
            throw new UnauthorizedException("You are not part of this conversation");
        }
        
        Message message = new Message(messageDto.getConversationId(),
                currentUser.getId(),
                messageDto.getContent(),
                messageDto.getMessageType());
        
        message = messageRepository.save(message);
        
        conversation.setLastMessageDate(LocalDateTime.now());
        conversationRepository.save(conversation);
        
        Long receiverId = conversation.getUser1Id().equals(currentUser.getId()) ? 
                conversation.getUser2Id() : conversation.getUser1Id();
        notificationService.notifyNewMessage(receiverId, currentUser.getId(), message.getContent());
        
        return message;
    }
    
    public List<Message> getConversationMessages(Long conversationId) {
        User currentUser = userService.getCurrentUser();
        
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation", "id", conversationId));
        
        if (!conversation.getUser1Id().equals(currentUser.getId()) && 
            !conversation.getUser2Id().equals(currentUser.getId())) {
            throw new UnauthorizedException("You are not part of this conversation");
        }
        
        return messageRepository.findByConversationIdOrderBySentDateAsc(conversationId);
    }
    
    public List<Conversation> getUserConversations() {
        User currentUser = userService.getCurrentUser();
        return conversationRepository.findByUser1IdOrUser2IdOrderByLastMessageDateDesc(currentUser.getId(), currentUser.getId());
    }
    
    @Transactional
    public void markMessagesAsRead(Long conversationId) {
        User currentUser = userService.getCurrentUser();
        
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation", "id", conversationId));
                
        if (!conversation.getUser1Id().equals(currentUser.getId()) && 
            !conversation.getUser2Id().equals(currentUser.getId())) {
            throw new UnauthorizedException("You are not part of this conversation");
        }
        
        List<Message> unreadMessages = messageRepository.findByConversationIdAndIsReadAndSenderIdNot(conversationId, false, currentUser.getId());
        
        for (Message message : unreadMessages) {
            message.setIsRead(true);
        }
        
        messageRepository.saveAll(unreadMessages);
    }
    
    public Long getUnreadMessageCount(Long conversationId) {
        User currentUser = userService.getCurrentUser();
        return messageRepository.countByConversationIdAndIsReadAndSenderIdNot(conversationId, false, currentUser.getId());
    }
}
