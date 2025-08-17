package com.project.yuklet.services;

import com.project.yuklet.dto.AuthResponse;
import com.project.yuklet.dto.LoginRequest;
import com.project.yuklet.dto.RegisterRequest;
import com.project.yuklet.entities.User;
import com.project.yuklet.entities.UserProfile;
import com.project.yuklet.entities.UserType;
import com.project.yuklet.exception.BadRequestException;
import com.project.yuklet.reporsitory.UserProfileRepository;
import com.project.yuklet.reporsitory.UserRepository;
import com.project.yuklet.security.JwtUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager mockAuthenticationManager;

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private UserProfileRepository mockUserProfileRepository;

    @Mock
    private PasswordEncoder mockPasswordEncoder;

    @Mock
    private JwtUtils mockJwtUtils;

    @InjectMocks
    private AuthService underTest;

    private LoginRequest testLoginRequest;
    private RegisterRequest testRegisterRequest;
    private User testUser;
    private UserProfile testUserProfile;
    private Authentication testAuthentication;

    @BeforeEach
    void setUp() {
        testLoginRequest = new LoginRequest();
        testLoginRequest.setEmail("test@example.com");
        testLoginRequest.setPassword("password123");

        testRegisterRequest = new RegisterRequest();
        testRegisterRequest.setEmail("test@example.com");
        testRegisterRequest.setPassword("password123");
        testRegisterRequest.setPhone("1234567890");
        testRegisterRequest.setUserType(UserType.SHIPPER);
        testRegisterRequest.setFirstName("Utku");
        testRegisterRequest.setLastName("Çolak");
        testRegisterRequest.setCompanyName("Yüklet");
        testRegisterRequest.setCity("İstanbul");

        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setPassword("encodedPassword");
        testUser.setPhone("1234567890");
        testUser.setUserType(UserType.SHIPPER);

        testUserProfile = new UserProfile();
        testUserProfile.setId(1L);
        testUserProfile.setUserId(1L);
        testUserProfile.setFirstName("Utku");
        testUserProfile.setLastName("Çolak");
        testUserProfile.setCompanyName("Yüklet");
        testUserProfile.setCity("İstanbul");

        testAuthentication = mock(Authentication.class);
    }

    @Test
    void login_shouldReturnAuthResponse_whenValidCredentials() {
        String expectedJwt = "test-jwt-token";
        
        when(testAuthentication.getPrincipal()).thenReturn(testUser);
        when(mockAuthenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(testAuthentication);
        when(mockJwtUtils.generateJwtToken(testAuthentication))
                .thenReturn(expectedJwt);

        AuthResponse actual = underTest.login(testLoginRequest);

        assertNotNull(actual);
        assertEquals(expectedJwt, actual.getToken());
        assertEquals(testUser.getId(), actual.getUserId());
        assertEquals(testUser.getEmail(), actual.getEmail());
        assertEquals(testUser.getUserType(), actual.getUserType());

        verify(mockAuthenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(mockJwtUtils).generateJwtToken(testAuthentication);
    }

    @Test
    void register_shouldReturnAuthResponse_whenValidRequest() {
        String encodedPassword = "encodedPassword123";
        String expectedJwt = "test-jwt-token";

        when(mockUserRepository.existsByEmail(testRegisterRequest.getEmail())).thenReturn(false);
        when(mockUserRepository.existsByPhone(testRegisterRequest.getPhone())).thenReturn(false);
        when(mockPasswordEncoder.encode(testRegisterRequest.getPassword())).thenReturn(encodedPassword);
        when(mockUserRepository.save(any(User.class))).thenReturn(testUser);
        when(mockAuthenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(testAuthentication);
        when(mockJwtUtils.generateJwtToken(testAuthentication)).thenReturn(expectedJwt);

        AuthResponse actual = underTest.register(testRegisterRequest);

        assertNotNull(actual);
        assertEquals(expectedJwt, actual.getToken());
        assertEquals(testUser.getId(), actual.getUserId());
        assertEquals(testUser.getEmail(), actual.getEmail());
        assertEquals(testUser.getUserType(), actual.getUserType());

        verify(mockUserRepository).existsByEmail(testRegisterRequest.getEmail());
        verify(mockUserRepository).existsByPhone(testRegisterRequest.getPhone());
        verify(mockPasswordEncoder).encode(testRegisterRequest.getPassword());
        verify(mockUserRepository).save(any(User.class));
        verify(mockAuthenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(mockJwtUtils).generateJwtToken(testAuthentication);
    }

    @Test
    void register_shouldThrowException_whenEmailAlreadyExists() {
        when(mockUserRepository.existsByEmail(testRegisterRequest.getEmail())).thenReturn(true);

        BadRequestException exception = assertThrows(BadRequestException.class, 
                () -> underTest.register(testRegisterRequest));

        assertEquals("Email is already taken!", exception.getMessage());

        verify(mockUserRepository).existsByEmail(testRegisterRequest.getEmail());
        verify(mockUserRepository, never()).existsByPhone(anyString());
        verify(mockUserRepository, never()).save(any(User.class));
    }

    @Test
    void register_shouldThrowException_whenPhoneAlreadyExists() {
        when(mockUserRepository.existsByEmail(testRegisterRequest.getEmail())).thenReturn(false);
        when(mockUserRepository.existsByPhone(testRegisterRequest.getPhone())).thenReturn(true);

        BadRequestException exception = assertThrows(BadRequestException.class, 
                () -> underTest.register(testRegisterRequest));

        assertEquals("Phone number is already taken!", exception.getMessage());

        verify(mockUserRepository).existsByEmail(testRegisterRequest.getEmail());
        verify(mockUserRepository).existsByPhone(testRegisterRequest.getPhone());
        verify(mockUserRepository, never()).save(any(User.class));
    }
}