package com.project.yuklet.dto;

import com.project.yuklet.entities.UserType;

public class AuthResponse {
    
    private String token;
    private String type = "Bearer";
    private Long userId;
    private String email;
    private UserType userType;
    
    public AuthResponse() {}
    
    public AuthResponse(String token, Long userId, String email, UserType userType) {
        this.token = token;
        this.userId = userId;
        this.email = email;
        this.userType = userType;
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public UserType getUserType() {
        return userType;
    }
    
    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}
