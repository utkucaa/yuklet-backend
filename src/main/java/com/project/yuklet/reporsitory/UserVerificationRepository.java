package com.project.yuklet.reporsitory;

import com.project.yuklet.entities.UserVerification;
import com.project.yuklet.entities.VerificationStatus;
import com.project.yuklet.entities.VerificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserVerificationRepository extends JpaRepository<UserVerification, Long> {
    
    List<UserVerification> findByUserId(Long userId);
    
    List<UserVerification> findByStatus(VerificationStatus status);
    
    Optional<UserVerification> findByUserIdAndVerificationType(Long userId, VerificationType verificationType);
    
    boolean existsByUserIdAndVerificationTypeAndStatus(Long userId, VerificationType verificationType, VerificationStatus status);
}
