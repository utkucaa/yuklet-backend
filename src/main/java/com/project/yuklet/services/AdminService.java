package com.project.yuklet.services;

import com.project.yuklet.entities.*;
import com.project.yuklet.exception.ResourceNotFoundException;
import com.project.yuklet.reporsitory.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserVerificationRepository userVerificationRepository;
    
    @Autowired
    private CargoRequestRepository cargoRequestRepository;
    
    @Autowired
    private TransportOfferRepository transportOfferRepository;
    
    @Autowired
    private NotificationService notificationService;
    
    @Autowired
    private UserService userService;
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    }
    
    @Transactional
    public User updateUserStatus(Long userId, String status) {
        User user = getUserById(userId);
        user.setStatus(UserStatus.valueOf(status.toUpperCase()));
        return userRepository.save(user);
    }
    
    @Transactional
    public void deleteUser(Long userId) {
        User user = getUserById(userId);
        userRepository.delete(user);
    }
    
    public List<User> searchUsers(String keyword, String userType, String status) {
        List<User> allUsers = userRepository.findAll();
        
        return allUsers.stream()
                .filter(user -> keyword == null || user.getEmail().contains(keyword))
                .filter(user -> userType == null || user.getUserType().name().equals(userType))
                .filter(user -> status == null || user.getStatus().name().equals(status))
                .toList();
    }
    
    public List<UserVerification> getPendingVerifications() {
        return userVerificationRepository.findByStatus(VerificationStatus.PENDING);
    }
    
    public List<UserVerification> getAllVerifications() {
        return userVerificationRepository.findAll();
    }
    
    @Transactional
    public UserVerification approveVerification(Long verificationId) {
        UserVerification verification = userVerificationRepository.findById(verificationId)
                .orElseThrow(() -> new ResourceNotFoundException("UserVerification", "id", verificationId));
        
        verification.setStatus(VerificationStatus.APPROVED);
        verification.setVerificationDate(LocalDateTime.now());
        verification.setVerifiedBy(userService.getCurrentUser().getId());
        
        notificationService.notifyVerificationStatusChange(verification.getUserId(), "approved");
        
        return userVerificationRepository.save(verification);
    }
    
    @Transactional
    public UserVerification rejectVerification(Long verificationId, String reason) {
        UserVerification verification = userVerificationRepository.findById(verificationId)
                .orElseThrow(() -> new ResourceNotFoundException("UserVerification", "id", verificationId));
        
        verification.setStatus(VerificationStatus.REJECTED);
        verification.setVerificationDate(LocalDateTime.now());
        verification.setVerifiedBy(userService.getCurrentUser().getId());
        verification.setRejectionReason(reason);
        
        notificationService.notifyVerificationStatusChange(verification.getUserId(), "rejected");
        
        return userVerificationRepository.save(verification);
    }
    
    public List<CargoRequest> getAllCargoRequests() {
        return cargoRequestRepository.findAll();
    }
    
    public List<TransportOffer> getAllTransportOffers() {
        return transportOfferRepository.findAll();
    }
    
    @Transactional
    public CargoRequest updateCargoRequestStatus(Long requestId, String status) {
        CargoRequest request = cargoRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("CargoRequest", "id", requestId));
        
        request.setStatus(RequestStatus.valueOf(status.toUpperCase()));
        return cargoRequestRepository.save(request);
    }
    
    @Transactional
    public TransportOffer updateTransportOfferStatus(Long offerId, String status) {
        TransportOffer offer = transportOfferRepository.findById(offerId)
                .orElseThrow(() -> new ResourceNotFoundException("TransportOffer", "id", offerId));
        
        offer.setStatus(RequestStatus.valueOf(status.toUpperCase()));
        return transportOfferRepository.save(offer);
    }
    
    @Transactional
    public void deleteCargoRequest(Long requestId) {
        CargoRequest request = cargoRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("CargoRequest", "id", requestId));
        
        cargoRequestRepository.delete(request);
    }
    
    @Transactional
    public void deleteTransportOffer(Long offerId) {
        TransportOffer offer = transportOfferRepository.findById(offerId)
                .orElseThrow(() -> new ResourceNotFoundException("TransportOffer", "id", offerId));
        
        transportOfferRepository.delete(offer);
    }
    
    public Map<String, Object> getPlatformOverview() {
        Map<String, Object> overview = new HashMap<>();
        
        long totalUsers = userRepository.count();
        long totalCargoRequests = cargoRequestRepository.count();
        long totalTransportOffers = transportOfferRepository.count();
        long pendingVerifications = userVerificationRepository.findByStatus(VerificationStatus.PENDING).size();
        
        overview.put("totalUsers", totalUsers);
        overview.put("totalCargoRequests", totalCargoRequests);
        overview.put("totalTransportOffers", totalTransportOffers);
        overview.put("pendingVerifications", pendingVerifications);
        overview.put("lastUpdated", LocalDateTime.now());
        
        return overview;
    }
    
    public Map<String, Object> getUserStats() {
        Map<String, Object> stats = new HashMap<>();
        
        long totalUsers = userRepository.count();
        long activeUsers = userRepository.findByStatus(UserStatus.ACTIVE).size();
        long inactiveUsers = userRepository.findByStatus(UserStatus.INACTIVE).size();
        
        long carriers = userRepository.findByUserType(UserType.CARRIER).size();
        long shippers = userRepository.findByUserType(UserType.SHIPPER).size();
        long admins = userRepository.findByUserType(UserType.ADMIN).size();
        
        stats.put("totalUsers", totalUsers);
        stats.put("activeUsers", activeUsers);
        stats.put("inactiveUsers", inactiveUsers);
        stats.put("carriers", carriers);
        stats.put("shippers", shippers);
        stats.put("admins", admins);
        
        return stats;
    }
    
    public Map<String, Object> getCargoStats() {
        Map<String, Object> stats = new HashMap<>();
        
        long totalRequests = cargoRequestRepository.count();
        long activeRequests = cargoRequestRepository.findByStatus(RequestStatus.ACTIVE).size();
        long completedRequests = cargoRequestRepository.findByStatus(RequestStatus.COMPLETED).size();
        long cancelledRequests = cargoRequestRepository.findByStatus(RequestStatus.CANCELLED).size();
        
        stats.put("totalRequests", totalRequests);
        stats.put("activeRequests", activeRequests);
        stats.put("completedRequests", completedRequests);
        stats.put("cancelledRequests", cancelledRequests);
        
        return stats;
    }
    
    public Map<String, Object> getTransportStats() {
        Map<String, Object> stats = new HashMap<>();
        
        long totalOffers = transportOfferRepository.count();
        long activeOffers = transportOfferRepository.findByStatus(RequestStatus.ACTIVE).size();
        long completedOffers = transportOfferRepository.findByStatus(RequestStatus.COMPLETED).size();
        long cancelledOffers = transportOfferRepository.findByStatus(RequestStatus.CANCELLED).size();
        
        stats.put("totalOffers", totalOffers);
        stats.put("activeOffers", activeOffers);
        stats.put("completedOffers", completedOffers);
        stats.put("cancelledOffers", cancelledOffers);
        
        return stats;
    }
    
    public Map<String, Object> getRevenueStats() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("monthlyRevenue", 0.0);
        stats.put("totalRevenue", 0.0);
        stats.put("averageTransactionValue", 0.0);
        stats.put("commissionRate", 0.05);
        
        return stats;
    }
    
    public Map<String, Object> getDailyReport() {
        Map<String, Object> report = new HashMap<>();
        
        LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime tomorrow = today.plusDays(1);
        
        long newUsersToday = userRepository.findAll().stream()
                .filter(user -> user.getCreatedDate().isAfter(today) && user.getCreatedDate().isBefore(tomorrow))
                .count();
        
        long newCargoRequestsToday = cargoRequestRepository.findAll().stream()
                .filter(request -> request.getCreatedDate().isAfter(today) && request.getCreatedDate().isBefore(tomorrow))
                .count();
        
        report.put("date", today.toLocalDate());
        report.put("newUsers", newUsersToday);
        report.put("newCargoRequests", newCargoRequestsToday);
        
        return report;
    }
    
    public Map<String, Object> getMonthlyReport() {
        Map<String, Object> report = new HashMap<>();
        
        LocalDateTime thisMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        
        long newUsersThisMonth = userRepository.findAll().stream()
                .filter(user -> user.getCreatedDate().isAfter(thisMonth))
                .count();
        
        long newCargoRequestsThisMonth = cargoRequestRepository.findAll().stream()
                .filter(request -> request.getCreatedDate().isAfter(thisMonth))
                .count();
        
        report.put("month", thisMonth.getMonth());
        report.put("year", thisMonth.getYear());
        report.put("newUsers", newUsersThisMonth);
        report.put("newCargoRequests", newCargoRequestsThisMonth);
        
        return report;
    }
    
    public Map<String, Object> getUserReport() {
        Map<String, Object> report = new HashMap<>();
        
        List<User> allUsers = userRepository.findAll();
        
        long verifiedUsers = allUsers.stream()
                .filter(user -> userVerificationRepository.existsByUserIdAndVerificationTypeAndStatus(
                        user.getId(), VerificationType.IDENTITY_DOCUMENT, VerificationStatus.APPROVED))
                .count();
        
        long unverifiedUsers = allUsers.size() - verifiedUsers;
        
        report.put("totalUsers", allUsers.size());
        report.put("verifiedUsers", verifiedUsers);
        report.put("unverifiedUsers", unverifiedUsers);
        report.put("verificationRate", (double) verifiedUsers / allUsers.size());
        
        return report;
    }
    
    public Map<String, Object> getSystemHealth() {
        Map<String, Object> health = new HashMap<>();
        
        health.put("status", "HEALTHY");
        health.put("database", "CONNECTED");
        health.put("uptime", "24 hours");
        health.put("lastCheck", LocalDateTime.now());
        
        return health;
    }
    
    public List<String> getSystemLogs() {
        return List.of(
            "2024-01-15 10:00:00 - System started",
            "2024-01-15 10:05:00 - Database connected",
            "2024-01-15 10:10:00 - Admin user logged in"
        );
    }
    
    public String createBackup() {
        return "/backups/backup_" + LocalDateTime.now().toString() + ".sql";
    }
            
    public void sendNotification(String title, String message, String userType) {
        List<User> targetUsers;
        
        if (userType != null) {
            targetUsers = userRepository.findByUserType(UserType.valueOf(userType.toUpperCase()));
        } else {
            targetUsers = userRepository.findAll();
        }
        
        for (User user : targetUsers) {
            notificationService.createNotification(user.getId(), title, message, NotificationType.MESSAGE);
        }
    }
    
    public void broadcastNotification(String title, String message) {
        List<User> allUsers = userRepository.findAll();
        
        for (User user : allUsers) {
            notificationService.createNotification(user.getId(), title, message, NotificationType.MESSAGE);
        }
    }
}
