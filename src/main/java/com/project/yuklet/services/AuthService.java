package com.project.yuklet.services;

import com.project.yuklet.dto.AuthResponse;
import com.project.yuklet.dto.LoginRequest;
import com.project.yuklet.dto.RegisterRequest;
import com.project.yuklet.entities.User;
import com.project.yuklet.entities.UserProfile;
import com.project.yuklet.exception.BadRequestException;
import com.project.yuklet.reporsitory.UserProfileRepository;
import com.project.yuklet.reporsitory.UserRepository;
import com.project.yuklet.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserProfileRepository userProfileRepository;
    
    @Autowired
    private PasswordEncoder encoder;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    public AuthResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        
        User user = (User) authentication.getPrincipal();
        
        return new AuthResponse(jwt, user.getId(), user.getEmail(), user.getUserType());
    }
    
    @Transactional
    public AuthResponse register(RegisterRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new BadRequestException("Email is already taken!");
        }
        
        if (userRepository.existsByPhone(signUpRequest.getPhone())) {
            throw new BadRequestException("Phone number is already taken!");
        }
        
        User user = new User(signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getPhone(),
                signUpRequest.getUserType());
        
        user = userRepository.save(user);
        
        UserProfile userProfile = new UserProfile(user.getId(),
                signUpRequest.getFirstName(),
                signUpRequest.getLastName(),
                signUpRequest.getCompanyName(),
                signUpRequest.getCity());
        
        userProfileRepository.save(userProfile);
        
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signUpRequest.getEmail(), signUpRequest.getPassword()));
        
        String jwt = jwtUtils.generateJwtToken(authentication);
        
        return new AuthResponse(jwt, user.getId(), user.getEmail(), user.getUserType());
    }
}
