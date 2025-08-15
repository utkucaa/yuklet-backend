package com.project.yuklet.reporsitory;

import com.project.yuklet.entities.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    
    List<Conversation> findByUser1IdOrUser2IdOrderByLastMessageDateDesc(Long user1Id, Long user2Id);
    
    Optional<Conversation> findByUser1IdAndUser2IdAndCargoRequestId(Long user1Id, Long user2Id, Long cargoRequestId);
    
    Optional<Conversation> findByUser2IdAndUser1IdAndCargoRequestId(Long user2Id, Long user1Id, Long cargoRequestId);
    
    List<Conversation> findByCargoRequestId(Long cargoRequestId);
}
