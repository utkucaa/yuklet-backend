package com.project.yuklet.services;

import com.project.yuklet.entities.*;
import com.project.yuklet.exception.BadRequestException;
import com.project.yuklet.exception.ResourceNotFoundException;
import com.project.yuklet.exception.UnauthorizedException;
import com.project.yuklet.reporsitory.UserVerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VerificationService {
    
    @Autowired
    private UserVerificationRepository userVerificationRepository;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private FileUploadService fileUploadService;
    
    @Autowired
    private NotificationService notificationService;
    
    @Transactional
    public UserVerification submitVerification(VerificationType verificationType, MultipartFile document) {
        User currentUser = userService.getCurrentUser();
        
        if (userVerificationRepository.existsByUserIdAndVerificationTypeAndStatus(
                currentUser.getId(), verificationType, VerificationStatus.PENDING)) {
            throw new BadRequestException("You already have a pending verification for this document type");
        }
        
        if (userVerificationRepository.existsByUserIdAndVerificationTypeAndStatus(
                currentUser.getId(), verificationType, VerificationStatus.APPROVED)) {
            throw new BadRequestException("You already have an approved verification for this document type");
        }
        
        FileUpload uploadedFile = fileUploadService.uploadFile(document, FileType.DOCUMENT, currentUser.getId());
        
        UserVerification verification = new UserVerification(currentUser.getId(), verificationType, uploadedFile.getFilePath());
        verification = userVerificationRepository.save(verification);
        
        notificationService.notifyAdminsAboutNewVerification(verification);
        
        return verification;
    }
    
    @Transactional
    public UserVerification approveVerification(Long verificationId) {
        User currentUser = userService.getCurrentUser();
        
        if (currentUser.getUserType() != UserType.ADMIN) {
            throw new UnauthorizedException("Only admins can approve verifications");
        }
        
        UserVerification verification = userVerificationRepository.findById(verificationId)
                .orElseThrow(() -> new ResourceNotFoundException("UserVerification", "id", verificationId));
        
        if (verification.getStatus() != VerificationStatus.PENDING) {
            throw new BadRequestException("Verification is not in pending status");
        }
        
        verification.setStatus(VerificationStatus.APPROVED);
        verification.setVerifiedBy(currentUser.getId());
        verification.setVerificationDate(LocalDateTime.now());
        
        verification = userVerificationRepository.save(verification);
        
        notificationService.notifyVerificationStatusChange(verification.getUserId(), "approved");
        
        return verification;
    }
    
    @Transactional
    public UserVerification rejectVerification(Long verificationId, String rejectionReason) {
        User currentUser = userService.getCurrentUser();
        
        if (currentUser.getUserType() != UserType.ADMIN) {
            throw new UnauthorizedException("Only admins can reject verifications");
        }
        
        UserVerification verification = userVerificationRepository.findById(verificationId)
                .orElseThrow(() -> new ResourceNotFoundException("UserVerification", "id", verificationId));
        
        if (verification.getStatus() != VerificationStatus.PENDING) {
            throw new BadRequestException("Verification is not in pending status");
        }
        
        verification.setStatus(VerificationStatus.REJECTED);
        verification.setVerifiedBy(currentUser.getId());
        verification.setVerificationDate(LocalDateTime.now());
        verification.setRejectionReason(rejectionReason);
        
        verification = userVerificationRepository.save(verification);
                
        notificationService.notifyVerificationStatusChange(verification.getUserId(), "rejected");
        
        return verification;
    }
    
    public List<UserVerification> getUserVerifications() {
        User currentUser = userService.getCurrentUser();
        return userVerificationRepository.findByUserId(currentUser.getId());
    }
    
    public List<UserVerification> getPendingVerifications() {
        return userVerificationRepository.findByStatus(VerificationStatus.PENDING);
    }
    
    public boolean isUserVerified(Long userId, VerificationType verificationType) {
        return userVerificationRepository.existsByUserIdAndVerificationTypeAndStatus(
                userId, verificationType, VerificationStatus.APPROVED);
    }
}
