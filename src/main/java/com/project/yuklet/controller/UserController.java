package com.project.yuklet.controller;

import com.project.yuklet.dto.ApiResponse;
import com.project.yuklet.entities.User;
import com.project.yuklet.entities.UserProfile;
import com.project.yuklet.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<User>> getCurrentUser() {
        User user = userService.getCurrentUser();
        return ResponseEntity.ok(ApiResponse.success(user));
    }
    
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserProfile>> getCurrentUserProfile() {
        UserProfile profile = userService.getCurrentUserProfile();
        return ResponseEntity.ok(ApiResponse.success(profile));
    }
    
    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<UserProfile>> updateProfile(@RequestBody UserProfile updatedProfile) {
        UserProfile profile = userService.updateProfile(updatedProfile);
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", profile));
    }
    
    @GetMapping("/profile/{userId}")
    public ResponseEntity<ApiResponse<UserProfile>> getUserProfile(@PathVariable Long userId) {
        UserProfile profile = userService.getUserProfile(userId);
        return ResponseEntity.ok(ApiResponse.success(profile));
    }
    
    @GetMapping("/search/city")
    public ResponseEntity<ApiResponse<List<UserProfile>>> searchUsersByCity(@RequestParam String city) {
        List<UserProfile> users = userService.searchUsersByCity(city);
        return ResponseEntity.ok(ApiResponse.success(users));
    }
    
    @GetMapping("/search/company")
    public ResponseEntity<ApiResponse<List<UserProfile>>> searchUsersByCompany(@RequestParam String companyName) {
        List<UserProfile> users = userService.searchUsersByCompany(companyName);
        return ResponseEntity.ok(ApiResponse.success(users));
    }
}
