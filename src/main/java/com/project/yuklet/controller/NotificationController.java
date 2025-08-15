package com.project.yuklet.controller;

import com.project.yuklet.dto.ApiResponse;
import com.project.yuklet.entities.Notification;
import com.project.yuklet.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*", maxAge = 3600)
public class NotificationController {
    
    @Autowired
    private NotificationService notificationService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<Notification>>> getUserNotifications() {
        List<Notification> notifications = notificationService.getUserNotifications();
        return ResponseEntity.ok(ApiResponse.success(notifications));
    }
    
    @GetMapping("/unread")
    public ResponseEntity<ApiResponse<List<Notification>>> getUnreadNotifications() {
        List<Notification> notifications = notificationService.getUnreadNotifications();
        return ResponseEntity.ok(ApiResponse.success(notifications));
    }
    
    @GetMapping("/unread/count")
    public ResponseEntity<ApiResponse<Long>> getUnreadNotificationCount() {
        Long count = notificationService.getUnreadNotificationCount();
        return ResponseEntity.ok(ApiResponse.success(count));
    }
    
    @PutMapping("/{notificationId}/mark-read")
    public ResponseEntity<ApiResponse<String>> markNotificationAsRead(@PathVariable Long notificationId) {
        notificationService.markNotificationAsRead(notificationId);
        return ResponseEntity.ok(ApiResponse.success("Notification marked as read", null));
    }
    
    @PutMapping("/mark-all-read")
    public ResponseEntity<ApiResponse<String>> markAllNotificationsAsRead() {
        notificationService.markAllNotificationsAsRead();
        return ResponseEntity.ok(ApiResponse.success("All notifications marked as read", null));
    }
}
