package com.project.yuklet.services;

import com.project.yuklet.entities.*;
import com.project.yuklet.reporsitory.NotificationRepository;
import com.project.yuklet.reporsitory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    
    @Autowired
    private NotificationRepository notificationRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserService userService;
    
    public void notifyCarriersAboutNewCargo(CargoRequest cargoRequest) {
        List<User> carriers = userRepository.findByUserType(UserType.CARRIER);
        
        String title = "Yeni Yük İlanı";
        String message = String.format("%s - %s arası yük ilanı oluşturuldu. Ağırlık: %d kg", 
                cargoRequest.getPickupCity(), 
                cargoRequest.getDeliveryCity(), 
                cargoRequest.getWeightKg());
        
        for (User carrier : carriers) {
            createNotification(carrier.getId(), title, message, NotificationType.INTEREST);
        }
    }
    
    public void notifyShippersAboutNewOffer(TransportOffer transportOffer) {
        List<User> shippers = userRepository.findByUserType(UserType.SHIPPER);
        
        String title = "Yeni Taşıma İlanı";
        String message = String.format("%s - %s arası taşıma hizmeti mevcut. Kapasite: %d kg", 
                transportOffer.getFromCity(), 
                transportOffer.getToCity(), 
                transportOffer.getAvailableWeightKg());
        
        for (User shipper : shippers) {
            createNotification(shipper.getId(), title, message, NotificationType.INTEREST);
        }
    }
    
    public void notifyNewMessage(Long receiverId, Long senderId, String messageContent) {
        String title = "Yeni Mesaj";
        String message = "Yeni bir mesajınız var";
        
        createNotification(receiverId, title, message, NotificationType.MESSAGE);
    }
    
    public void notifyNewRating(Long ratedUserId, Long raterId, Integer rating) {
        String title = "Yeni Değerlendirme";
        String message = String.format("Size %d yıldız değerlendirme yapıldı", rating);
        
        createNotification(ratedUserId, title, message, NotificationType.RATING);
    }
    
    public void createNotification(Long userId, String title, String message, NotificationType type) {
        Notification notification = new Notification(userId, title, message, type);
        notificationRepository.save(notification);
    }
    
    public List<Notification> getUserNotifications() {
        User currentUser = userService.getCurrentUser();
        return notificationRepository.findByUserIdOrderByCreatedDateDesc(currentUser.getId());
    }
    
    public List<Notification> getUnreadNotifications() {
        User currentUser = userService.getCurrentUser();
        return notificationRepository.findByUserIdAndIsReadOrderByCreatedDateDesc(currentUser.getId(), false);
    }
    
    public Long getUnreadNotificationCount() {
        User currentUser = userService.getCurrentUser();
        return notificationRepository.countByUserIdAndIsRead(currentUser.getId(), false);
    }
    
    public void markNotificationAsRead(Long notificationId) {
        User currentUser = userService.getCurrentUser();
        
        notificationRepository.findById(notificationId).ifPresent(notification -> {
            if (notification.getUserId().equals(currentUser.getId())) {
                notification.setIsRead(true);
                notificationRepository.save(notification);
            }
        });
    }
    
    public void markAllNotificationsAsRead() {
        User currentUser = userService.getCurrentUser();
        List<Notification> unreadNotifications = notificationRepository
                .findByUserIdAndIsReadOrderByCreatedDateDesc(currentUser.getId(), false);
        
        for (Notification notification : unreadNotifications) {
            notification.setIsRead(true);
        }
        
        notificationRepository.saveAll(unreadNotifications);
    }
    
    public void notifyAdminsAboutNewVerification(UserVerification verification) {
        String title = "Yeni Doğrulama Talebi";
        String message = "Yeni bir kullanıcı doğrulama talebi gönderildi";
                
        System.out.println("ADMIN NOTIFICATION: " + title + " - " + message);
    }
    
    public void notifyVerificationStatusChange(Long userId, String status) {
        String title = "Doğrulama Durumu Güncellendi";
        String message = "Doğrulama talebiniz " + status + " edildi";
        
        createNotification(userId, title, message, NotificationType.MESSAGE);
    }
}
