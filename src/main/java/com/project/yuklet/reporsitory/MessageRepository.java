package com.project.yuklet.reporsitory;

import com.project.yuklet.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    
    List<Message> findByConversationIdOrderBySentDateAsc(Long conversationId);
    
    List<Message> findByConversationIdAndSenderIdNotAndIsReadFalse(Long conversationId, Long senderId);
    
    Long countByConversationIdAndSenderIdNotAndIsReadFalse(Long conversationId, Long senderId);
    
    List<Message> findByConversationIdAndIsReadFalse(Long conversationId);
}
