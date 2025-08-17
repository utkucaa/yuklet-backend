package com.project.yuklet.services;

import com.project.yuklet.entities.User;
import com.project.yuklet.entities.UserProfile;
import com.project.yuklet.exception.ResourceNotFoundException;
import com.project.yuklet.reporsitory.UserProfileRepository;
import com.project.yuklet.reporsitory.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private UserProfileRepository mockUserProfileRepository;

    @InjectMocks
    private UserService underTest;

    private User testUser;
    private UserProfile testProfile;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");

        testProfile = new UserProfile();
        testProfile.setId(1L);
        testProfile.setUserId(testUser.getId());
        testProfile.setFirstName("Utku");
        testProfile.setLastName("Çolak");
    }

    @Test
    void getCurrentUser_shouldReturnUser_whenUserExists() {
        // GIVEN
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("test@example.com");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        
        when(mockUserRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(testUser));

        // WHEN
        User actual = underTest.getCurrentUser();

        // THEN
        assertNotNull(actual);
        assertEquals(testUser.getEmail(), actual.getEmail());
    }

    @Test
    void getCurrentUser_shouldThrowException_whenUserNotFound() {
        // GIVEN
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("test@example.com");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        
        when(mockUserRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(ResourceNotFoundException.class, () -> underTest.getCurrentUser());
    }

    @Test
    void getCurrentUserProfile_shouldReturnProfile_whenProfileExists() {
        // GIVEN
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("test@example.com");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        
        when(mockUserRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(testUser));
        when(mockUserProfileRepository.findByUserId(1L))
                .thenReturn(Optional.of(testProfile));

        // WHEN
        UserProfile actual = underTest.getCurrentUserProfile();

        // THEN
        assertEquals("Utku", actual.getFirstName());
    }

    @Test
    void getCurrentUserProfile_shouldThrowException_whenProfileNotFound() {
        // GIVEN
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("test@example.com");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        
        when(mockUserRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(testUser));
        when(mockUserProfileRepository.findByUserId(1L))
                .thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(ResourceNotFoundException.class, () -> underTest.getCurrentUserProfile());
    }

    @Test
    void updateProfile_shouldUpdateFields_whenProfileExists() {
        // GIVEN
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("test@example.com");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        
        when(mockUserRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(testUser));
        when(mockUserProfileRepository.findByUserId(1L))
                .thenReturn(Optional.of(testProfile));
        when(mockUserProfileRepository.save(any(UserProfile.class)))
                .thenReturn(testProfile);

        UserProfile updateRequest = new UserProfile();
        updateRequest.setFirstName("Jane");

        // WHEN
        UserProfile actual = underTest.updateProfile(updateRequest);

        // THEN
        assertEquals("Jane", actual.getFirstName());
        verify(mockUserProfileRepository).save(testProfile);
    }

    @Test
    void getUserProfile_shouldReturnProfile_whenExists() {
        // GIVEN
        when(mockUserProfileRepository.findByUserId(1L))
                .thenReturn(Optional.of(testProfile));

        // WHEN
        UserProfile actual = underTest.getUserProfile(1L);

        // THEN
        assertEquals("Utku", actual.getFirstName());
    }

    @Test
    void getUserProfile_shouldThrowException_whenNotFound() {
        // GIVEN
        when(mockUserProfileRepository.findByUserId(1L))
                .thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(ResourceNotFoundException.class, () -> underTest.getUserProfile(1L));
    }

    @Test
    void searchUsersByCity_shouldReturnList() {
        // GIVEN
        when(mockUserProfileRepository.findByCity("Ankara"))
                .thenReturn(Collections.singletonList(testProfile));

        // WHEN
        var result = underTest.searchUsersByCity("Ankara");

        // THEN
        assertEquals(1, result.size());
    }

    @Test
    void searchUsersByCompany_shouldReturnList() {
        // GIVEN
        when(mockUserProfileRepository.findByCompanyNameContainingIgnoreCase("Tech"))
                .thenReturn(Collections.singletonList(testProfile));

        // WHEN
        var result = underTest.searchUsersByCompany("Tech");

        // THEN
        assertEquals(1, result.size());
    }
}
