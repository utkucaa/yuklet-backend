package com.project.yuklet.controller;

import com.project.yuklet.dto.ApiResponse;
import com.project.yuklet.entities.UserVerification;
import com.project.yuklet.entities.VerificationType;
import com.project.yuklet.services.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/verification")
@CrossOrigin(origins = "*", maxAge = 3600)
public class VerificationController {
    
    @Autowired
    private VerificationService verificationService;
    
    @PostMapping("/submit")
    public ResponseEntity<ApiResponse<UserVerification>> submitVerification(
            @RequestParam VerificationType verificationType,
            @RequestParam("document") MultipartFile document) {
        
        UserVerification verification = verificationService.submitVerification(verificationType, document);
        return ResponseEntity.ok(ApiResponse.success("Verification submitted successfully", verification));
    }
    
    @PutMapping("/{verificationId}/approve")
    public ResponseEntity<ApiResponse<UserVerification>> approveVerification(@PathVariable Long verificationId) {
        UserVerification verification = verificationService.approveVerification(verificationId);
        return ResponseEntity.ok(ApiResponse.success("Verification approved successfully", verification));
    }
    
    @PutMapping("/{verificationId}/reject")
    public ResponseEntity<ApiResponse<UserVerification>> rejectVerification(@PathVariable Long verificationId,
                                                                           @RequestParam String rejectionReason) {
        UserVerification verification = verificationService.rejectVerification(verificationId, rejectionReason);
        return ResponseEntity.ok(ApiResponse.success("Verification rejected successfully", verification));
    }
    
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<UserVerification>>> getUserVerifications() {
        List<UserVerification> verifications = verificationService.getUserVerifications();
        return ResponseEntity.ok(ApiResponse.success(verifications));
    }
    
    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<List<UserVerification>>> getPendingVerifications() {
        List<UserVerification> verifications = verificationService.getPendingVerifications();
        return ResponseEntity.ok(ApiResponse.success(verifications));
    }
    
    @GetMapping("/check")
    public ResponseEntity<ApiResponse<Boolean>> isUserVerified(@RequestParam Long userId,
                                                              @RequestParam VerificationType verificationType) {
        boolean isVerified = verificationService.isUserVerified(userId, verificationType);
        return ResponseEntity.ok(ApiResponse.success(isVerified));
    }
}
