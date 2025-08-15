package com.project.yuklet.reporsitory;

import com.project.yuklet.entities.Message;
import com.project.yuklet.entities.MessageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    
    List<Message> findByConversationIdOrderBySentDateAsc(Long conversationId);
    
    List<Message> findBySenderId(Long senderId);
    
    List<Message> findByMessageType(MessageType messageType);
    
    List<Message> findByConversationIdAndIsReadAndSenderIdNot(Long conversationId, Boolean isRead, Long senderId);
    
    Long countByConversationIdAndIsReadAndSenderIdNot(Long conversationId, Boolean isRead, Long senderId);
}
