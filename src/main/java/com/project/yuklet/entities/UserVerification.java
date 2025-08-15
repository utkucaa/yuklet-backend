package com.project.yuklet.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_verifications")
public class UserVerification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id")
    private Long userId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "verification_type")
    private VerificationType verificationType;
    
    @Enumerated(EnumType.STRING)
    private VerificationStatus status = VerificationStatus.PENDING;
    
    @Column(name = "document_url")
    private String documentUrl;
    
    @Column(name = "verified_by")
    private Long verifiedBy;
    
    @Column(name = "verification_date")
    private LocalDateTime verificationDate;
    
    @Column(name = "rejection_reason")
    private String rejectionReason;
    
    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();
    
    public UserVerification() {}
    
    public UserVerification(Long userId, VerificationType verificationType, String documentUrl) {
        this.userId = userId;
        this.verificationType = verificationType;
        this.documentUrl = documentUrl;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public VerificationType getVerificationType() {
        return verificationType;
    }
    
    public void setVerificationType(VerificationType verificationType) {
        this.verificationType = verificationType;
    }
    
    public VerificationStatus getStatus() {
        return status;
    }
    
    public void setStatus(VerificationStatus status) {
        this.status = status;
    }
    
    public String getDocumentUrl() {
        return documentUrl;
    }
    
    public void setDocumentUrl(String documentUrl) {
        this.documentUrl = documentUrl;
    }
    
    public Long getVerifiedBy() {
        return verifiedBy;
    }
    
    public void setVerifiedBy(Long verifiedBy) {
        this.verifiedBy = verifiedBy;
    }
    
    public LocalDateTime getVerificationDate() {
        return verificationDate;
    }
    
    public void setVerificationDate(LocalDateTime verificationDate) {
        this.verificationDate = verificationDate;
    }
    
    public String getRejectionReason() {
        return rejectionReason;
    }
    
    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }
    
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
