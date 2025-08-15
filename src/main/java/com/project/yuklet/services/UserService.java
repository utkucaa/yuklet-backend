package com.project.yuklet.services;

import com.project.yuklet.entities.User;
import com.project.yuklet.entities.UserProfile;
import com.project.yuklet.exception.ResourceNotFoundException;
import com.project.yuklet.reporsitory.UserProfileRepository;
import com.project.yuklet.reporsitory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserProfileRepository userProfileRepository;
    
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }
    
    public UserProfile getCurrentUserProfile() {
        User currentUser = getCurrentUser();
        return userProfileRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("UserProfile", "userId", currentUser.getId()));
    }
    
    public UserProfile updateProfile(UserProfile updatedProfile) {
        User currentUser = getCurrentUser();
        UserProfile existingProfile = userProfileRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("UserProfile", "userId", currentUser.getId()));
        
        if (updatedProfile.getFirstName() != null) {
            existingProfile.setFirstName(updatedProfile.getFirstName());
        }
        if (updatedProfile.getLastName() != null) {
            existingProfile.setLastName(updatedProfile.getLastName());
        }
        if (updatedProfile.getCompanyName() != null) {
            existingProfile.setCompanyName(updatedProfile.getCompanyName());
        }
        if (updatedProfile.getCity() != null) {
            existingProfile.setCity(updatedProfile.getCity());
        }
        if (updatedProfile.getAddress() != null) {
            existingProfile.setAddress(updatedProfile.getAddress());
        }
        if (updatedProfile.getAbout() != null) {
            existingProfile.setAbout(updatedProfile.getAbout());
        }
        
        return userProfileRepository.save(existingProfile);
    }
    
    public UserProfile getUserProfile(Long userId) {
        return userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("UserProfile", "userId", userId));
    }
    
    public List<UserProfile> searchUsersByCity(String city) {
        return userProfileRepository.findByCity(city);
    }
    
    public List<UserProfile> searchUsersByCompany(String companyName) {
        return userProfileRepository.findByCompanyNameContainingIgnoreCase(companyName);
    }
}
