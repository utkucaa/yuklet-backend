package com.project.yuklet.reporsitory;

import com.project.yuklet.entities.Notification;
import com.project.yuklet.entities.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    List<Notification> findByUserIdOrderByCreatedDateDesc(Long userId);
    
    List<Notification> findByUserIdAndIsReadOrderByCreatedDateDesc(Long userId, Boolean isRead);
    
    List<Notification> findByType(NotificationType type);
    
    Long countByUserIdAndIsRead(Long userId, Boolean isRead);
}
