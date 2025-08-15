package com.project.yuklet.services;

import org.springframework.stereotype.Service;

@Service
public class EmailService {
    
    public void sendPasswordResetEmail(String email, String resetToken) {
        
        String resetLink = "http://localhost:8080/reset-password?token=" + resetToken;
        
        System.out.println("=== PASSWORD RESET EMAIL ===");
        System.out.println("To: " + email);
        System.out.println("Subject: Şifre Sıfırlama - Yüklet Platform");
        System.out.println("Reset Link: " + resetLink);
        System.out.println("Token: " + resetToken);
        System.out.println("This link will expire in 24 hours.");
        System.out.println("=============================");
        
        
    }
    
    public void sendWelcomeEmail(String email, String firstName) {
        System.out.println("=== WELCOME EMAIL ===");
        System.out.println("To: " + email);
        System.out.println("Subject: Yüklet Platformuna Hoş Geldiniz!");
        System.out.println("Merhaba " + firstName + ", platformumuza hoş geldiniz!");
        System.out.println("======================");
    }
    
    public void sendNotificationEmail(String email, String subject, String content) {
        System.out.println("=== NOTIFICATION EMAIL ===");
        System.out.println("To: " + email);
        System.out.println("Subject: " + subject);
        System.out.println("Content: " + content);
        System.out.println("===========================");
    }
}
