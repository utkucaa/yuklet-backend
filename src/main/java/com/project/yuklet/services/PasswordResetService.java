package com.project.yuklet.services;

import com.project.yuklet.dto.ForgotPasswordRequest;
import com.project.yuklet.dto.ResetPasswordRequest;
import com.project.yuklet.entities.PasswordResetToken;
import com.project.yuklet.entities.User;
import com.project.yuklet.exception.BadRequestException;
import com.project.yuklet.exception.ResourceNotFoundException;
import com.project.yuklet.reporsitory.PasswordResetTokenRepository;
import com.project.yuklet.reporsitory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetService {
    
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private EmailService emailService;
    
    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + request.getEmail()));
        
        passwordResetTokenRepository.deleteByUserId(user.getId());
        
        String token = UUID.randomUUID().toString();
        LocalDateTime expiryDate = LocalDateTime.now().plusHours(24);
        
        PasswordResetToken resetToken = new PasswordResetToken(token, user.getId(), expiryDate);
        passwordResetTokenRepository.save(resetToken);
        
        emailService.sendPasswordResetEmail(user.getEmail(), token);
    }
    
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByTokenAndUsedFalse(request.getToken())
                .orElseThrow(() -> new BadRequestException("Invalid or expired reset token"));
        
        if (resetToken.isExpired()) {
            throw new BadRequestException("Reset token has expired");
        }
        
        User user = userRepository.findById(resetToken.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", resetToken.getUserId()));
        
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        
        resetToken.setUsed(true);
        passwordResetTokenRepository.save(resetToken);
    }
    
    public boolean validateResetToken(String token) {
        return passwordResetTokenRepository.findByTokenAndUsedFalse(token)
                .map(resetToken -> !resetToken.isExpired())
                .orElse(false);
    }
        
    @Transactional
    public void cleanupExpiredTokens() {
        passwordResetTokenRepository.deleteByExpiryDateBefore(LocalDateTime.now());
    }
}
