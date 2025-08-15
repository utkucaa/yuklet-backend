package com.project.yuklet.controller;

import com.project.yuklet.dto.ApiResponse;
import com.project.yuklet.entities.User;
import com.project.yuklet.entities.UserVerification;
import com.project.yuklet.entities.CargoRequest;
import com.project.yuklet.entities.TransportOffer;
import com.project.yuklet.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*", maxAge = 3600)
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    
    @Autowired
    private AdminService adminService;
    
    
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> users = adminService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success(users));
    }
    
    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable Long userId) {
        User user = adminService.getUserById(userId);
        return ResponseEntity.ok(ApiResponse.success(user));
    }
    
    @PutMapping("/users/{userId}/status")
    public ResponseEntity<ApiResponse<User>> updateUserStatus(
            @PathVariable Long userId, 
            @RequestParam String status) {
        User user = adminService.updateUserStatus(userId, status);
        return ResponseEntity.ok(ApiResponse.success("Kullanıcı durumu güncellendi", user));
    }
    
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Long userId) {
        adminService.deleteUser(userId);
        return ResponseEntity.ok(ApiResponse.success("Kullanıcı silindi"));
    }
    
    @GetMapping("/users/search")
    public ResponseEntity<ApiResponse<List<User>>> searchUsers(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String userType,
            @RequestParam(required = false) String status) {
        List<User> users = adminService.searchUsers(keyword, userType, status);
        return ResponseEntity.ok(ApiResponse.success(users));
    }
    
    
    
    @GetMapping("/verifications/pending")
    public ResponseEntity<ApiResponse<List<UserVerification>>> getPendingVerifications() {
        List<UserVerification> verifications = adminService.getPendingVerifications();
        return ResponseEntity.ok(ApiResponse.success(verifications));
    }
    
    @GetMapping("/verifications/all")
    public ResponseEntity<ApiResponse<List<UserVerification>>> getAllVerifications() {
        List<UserVerification> verifications = adminService.getAllVerifications();
        return ResponseEntity.ok(ApiResponse.success(verifications));
    }
    
    @PutMapping("/verifications/{verificationId}/approve")
    public ResponseEntity<ApiResponse<UserVerification>> approveVerification(@PathVariable Long verificationId) {
        UserVerification verification = adminService.approveVerification(verificationId);
        return ResponseEntity.ok(ApiResponse.success("Doğrulama onaylandı", verification));
    }
    
    @PutMapping("/verifications/{verificationId}/reject")
    public ResponseEntity<ApiResponse<UserVerification>> rejectVerification(
            @PathVariable Long verificationId,
            @RequestParam String reason) {
        UserVerification verification = adminService.rejectVerification(verificationId, reason);
        return ResponseEntity.ok(ApiResponse.success("Doğrulama reddedildi", verification));
    }
    
    
    
    @GetMapping("/cargo-requests")
    public ResponseEntity<ApiResponse<List<CargoRequest>>> getAllCargoRequests() {
        List<CargoRequest> requests = adminService.getAllCargoRequests();
        return ResponseEntity.ok(ApiResponse.success(requests));
    }
    
    @GetMapping("/transport-offers")
    public ResponseEntity<ApiResponse<List<TransportOffer>>> getAllTransportOffers() {
        List<TransportOffer> offers = adminService.getAllTransportOffers();
        return ResponseEntity.ok(ApiResponse.success(offers));
    }
    
    @PutMapping("/cargo-requests/{requestId}/status")
    public ResponseEntity<ApiResponse<CargoRequest>> updateCargoRequestStatus(
            @PathVariable Long requestId,
            @RequestParam String status) {
        CargoRequest request = adminService.updateCargoRequestStatus(requestId, status);
        return ResponseEntity.ok(ApiResponse.success("Yük talebi durumu güncellendi", request));
    }
    
    @PutMapping("/transport-offers/{offerId}/status")
    public ResponseEntity<ApiResponse<TransportOffer>> updateTransportOfferStatus(
            @PathVariable Long offerId,
            @RequestParam String status) {
        TransportOffer offer = adminService.updateTransportOfferStatus(offerId, status);
        return ResponseEntity.ok(ApiResponse.success("Taşıma ilanı durumu güncellendi", offer));
    }
    
    @DeleteMapping("/cargo-requests/{requestId}")
    public ResponseEntity<ApiResponse<String>> deleteCargoRequest(@PathVariable Long requestId) {
        adminService.deleteCargoRequest(requestId);
        return ResponseEntity.ok(ApiResponse.success("Yük talebi silindi"));
    }
    
    @DeleteMapping("/transport-offers/{offerId}")
    public ResponseEntity<ApiResponse<String>> deleteTransportOffer(@PathVariable Long offerId) {
        adminService.deleteTransportOffer(offerId);
        return ResponseEntity.ok(ApiResponse.success("Taşıma ilanı silindi"));
    }
    
    
    
    @GetMapping("/stats/overview")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getPlatformOverview() {
        Map<String, Object> overview = adminService.getPlatformOverview();
        return ResponseEntity.ok(ApiResponse.success(overview));
    }
    
    @GetMapping("/stats/users")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getUserStats() {
        Map<String, Object> userStats = adminService.getUserStats();
        return ResponseEntity.ok(ApiResponse.success(userStats));
    }
    
    @GetMapping("/stats/cargo")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getCargoStats() {
        Map<String, Object> cargoStats = adminService.getCargoStats();
        return ResponseEntity.ok(ApiResponse.success(cargoStats));
    }
    
    @GetMapping("/stats/transport")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getTransportStats() {
        Map<String, Object> transportStats = adminService.getTransportStats();
        return ResponseEntity.ok(ApiResponse.success(transportStats));
    }
    
    @GetMapping("/stats/revenue")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getRevenueStats() {
        Map<String, Object> revenueStats = adminService.getRevenueStats();
        return ResponseEntity.ok(ApiResponse.success(revenueStats));
    }
    
    
    
    @GetMapping("/reports/daily")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDailyReport() {
        Map<String, Object> dailyReport = adminService.getDailyReport();
        return ResponseEntity.ok(ApiResponse.success(dailyReport));
    }
    
    @GetMapping("/reports/monthly")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getMonthlyReport() {
        Map<String, Object> monthlyReport = adminService.getMonthlyReport();
        return ResponseEntity.ok(ApiResponse.success(monthlyReport));
    }
    
    @GetMapping("/reports/users")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getUserReport() {
        Map<String, Object> userReport = adminService.getUserReport();
        return ResponseEntity.ok(ApiResponse.success(userReport));
    }
    
    
    
    @GetMapping("/system/health")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSystemHealth() {
        Map<String, Object> health = adminService.getSystemHealth();
        return ResponseEntity.ok(ApiResponse.success(health));
    }
    
    @GetMapping("/system/logs")
    public ResponseEntity<ApiResponse<List<String>>> getSystemLogs() {
        List<String> logs = adminService.getSystemLogs();
        return ResponseEntity.ok(ApiResponse.success(logs));
    }
    
    @PostMapping("/system/backup")
    public ResponseEntity<ApiResponse<String>> createBackup() {
        String backupPath = adminService.createBackup();
        return ResponseEntity.ok(ApiResponse.success("Yedekleme oluşturuldu: " + backupPath));
    }
    

    @PostMapping("/notifications/send")
    public ResponseEntity<ApiResponse<String>> sendNotification(
            @RequestParam String title,
            @RequestParam String message,
            @RequestParam(required = false) String userType) {
        adminService.sendNotification(title, message, userType);
        return ResponseEntity.ok(ApiResponse.success("Bildirim gönderildi"));
    }
    
    @PostMapping("/notifications/broadcast")
    public ResponseEntity<ApiResponse<String>> broadcastNotification(
            @RequestParam String title,
            @RequestParam String message) {
        adminService.broadcastNotification(title, message);
        return ResponseEntity.ok(ApiResponse.success("Genel bildirim gönderildi"));
    }
}
