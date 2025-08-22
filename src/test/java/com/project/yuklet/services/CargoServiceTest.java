package com.project.yuklet.services;

import com.project.yuklet.dto.CargoRequestDto;
import com.project.yuklet.dto.CargoRequestWithShipperDto;
import com.project.yuklet.entities.*;
import com.project.yuklet.exception.ResourceNotFoundException;
import com.project.yuklet.exception.UnauthorizedException;
import com.project.yuklet.reporsitory.CargoRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CargoService Tests")
class CargoServiceTest {
    
    @Mock
    private CargoRequestRepository cargoRequestRepository;
    
    @Mock
    private UserService userService;
    
    @Mock
    private NotificationService notificationService;
    
    @InjectMocks
    private CargoService cargoService;
    
    private User shipperUser;
    private User carrierUser;
    private CargoRequestDto cargoRequestDto;
    private CargoRequest cargoRequest;
    private UserProfile shipperProfile;
    
    @BeforeEach
    void setUp() {
        shipperUser = createShipperUser();
        carrierUser = createCarrierUser();
        cargoRequestDto = createCargoRequestDto();
        cargoRequest = createCargoRequest();
        shipperProfile = createUserProfile();
    }
    
    @Nested
    @DisplayName("Create Cargo Request Tests")
    class CreateCargoRequestTests {
        
        @Test
        @DisplayName("Should create cargo request successfully when user is shipper")
        void shouldCreateCargoRequestSuccessfully() {
            // Given
            when(userService.getCurrentUser()).thenReturn(shipperUser);
            when(cargoRequestRepository.save(any(CargoRequest.class))).thenReturn(cargoRequest);
            
            // When
            CargoRequest result = cargoService.createCargoRequest(cargoRequestDto);
            
            // Then
            assertThat(result).isNotNull();
            assertThat(result.getTitle()).isEqualTo(cargoRequestDto.getTitle());
            assertThat(result.getDescription()).isEqualTo(cargoRequestDto.getDescription());
            assertThat(result.getCargoType()).isEqualTo(cargoRequestDto.getCargoType());
            assertThat(result.getWeightKg()).isEqualTo(cargoRequestDto.getWeightKg());
            
            verify(cargoRequestRepository).save(any(CargoRequest.class));
            verify(notificationService).notifyCarriersAboutNewCargo(result);
        }
        
        @Test
        @DisplayName("Should throw UnauthorizedException when user is not shipper")
        void shouldThrowUnauthorizedExceptionWhenUserIsNotShipper() {
            // Given
            when(userService.getCurrentUser()).thenReturn(carrierUser);
            
            // When & Then
            assertThatThrownBy(() -> cargoService.createCargoRequest(cargoRequestDto))
                    .isInstanceOf(UnauthorizedException.class)
                    .hasMessage("Only shippers can create cargo requests");
            
            verify(cargoRequestRepository, never()).save(any(CargoRequest.class));
            verify(notificationService, never()).notifyCarriersAboutNewCargo(any());
        }
    }
    
    @Nested
    @DisplayName("Update Cargo Request Tests")
    class UpdateCargoRequestTests {
        
        @Test
        @DisplayName("Should update cargo request successfully when user is owner")
        void shouldUpdateCargoRequestSuccessfully() {
            // Given
            Long cargoId = 1L;
            CargoRequestDto updateDto = createUpdatedCargoRequestDto();
            
            when(userService.getCurrentUser()).thenReturn(shipperUser);
            when(cargoRequestRepository.findById(cargoId)).thenReturn(Optional.of(cargoRequest));
            when(cargoRequestRepository.save(any(CargoRequest.class))).thenReturn(cargoRequest);
            
            // When
            CargoRequest result = cargoService.updateCargoRequest(cargoId, updateDto);
            
            // Then
            assertThat(result).isNotNull();
            verify(cargoRequestRepository).save(cargoRequest);
        }
        
        @Test
        @DisplayName("Should throw ResourceNotFoundException when cargo request not found")
        void shouldThrowResourceNotFoundExceptionWhenCargoRequestNotFound() {
            // Given
            Long cargoId = 999L;
            
            when(userService.getCurrentUser()).thenReturn(shipperUser);
            when(cargoRequestRepository.findById(cargoId)).thenReturn(Optional.empty());
            
            // When & Then
            assertThatThrownBy(() -> cargoService.updateCargoRequest(cargoId, cargoRequestDto))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("CargoRequest")
                    .hasMessageContaining("id")
                    .hasMessageContaining("999");
        }
        
        @Test
        @DisplayName("Should throw UnauthorizedException when user is not owner")
        void shouldThrowUnauthorizedExceptionWhenUserIsNotOwner() {
            // Given
            Long cargoId = 1L;
            User anotherUser = createAnotherShipperUser();
            
            when(userService.getCurrentUser()).thenReturn(anotherUser);
            when(cargoRequestRepository.findById(cargoId)).thenReturn(Optional.of(cargoRequest));
            
            // When & Then
            assertThatThrownBy(() -> cargoService.updateCargoRequest(cargoId, cargoRequestDto))
                    .isInstanceOf(UnauthorizedException.class)
                    .hasMessage("You can only update your own cargo requests");
        }
        
        @Test
        @DisplayName("Should throw UnauthorizedException when cargo request status is not ACTIVE")
        void shouldThrowUnauthorizedExceptionWhenStatusIsNotActive() {
            // Given
            Long cargoId = 1L;
            cargoRequest.setStatus(RequestStatus.COMPLETED);
            
            when(userService.getCurrentUser()).thenReturn(shipperUser);
            when(cargoRequestRepository.findById(cargoId)).thenReturn(Optional.of(cargoRequest));
            
            // When & Then
            assertThatThrownBy(() -> cargoService.updateCargoRequest(cargoId, cargoRequestDto))
                    .isInstanceOf(UnauthorizedException.class)
                    .hasMessage("Cannot update completed or cancelled cargo requests");
        }
    }
    
    @Nested
    @DisplayName("Get My Cargo Requests Tests")
    class GetMyCargoRequestsTests {
        
        @Test
        @DisplayName("Should return user's cargo requests successfully")
        void shouldReturnUserCargoRequestsSuccessfully() {
            // Given
            List<CargoRequest> expectedRequests = Arrays.asList(cargoRequest, createAnotherCargoRequest());
            
            when(userService.getCurrentUser()).thenReturn(shipperUser);
            when(cargoRequestRepository.findByShipperId(shipperUser.getId())).thenReturn(expectedRequests);
            
            // When
            List<CargoRequest> result = cargoService.getMyCargoRequests();
            
            // Then
            assertThat(result).hasSize(2);
            assertThat(result).containsExactlyElementsOf(expectedRequests);
        }
        
        @Test
        @DisplayName("Should return empty list when user has no cargo requests")
        void shouldReturnEmptyListWhenUserHasNoCargoRequests() {
            // Given
            when(userService.getCurrentUser()).thenReturn(shipperUser);
            when(cargoRequestRepository.findByShipperId(shipperUser.getId())).thenReturn(Collections.emptyList());
            
            // When
            List<CargoRequest> result = cargoService.getMyCargoRequests();
            
            // Then
            assertThat(result).isEmpty();
        }
    }
    
    @Nested
    @DisplayName("Search Cargo Requests Tests")
    class SearchCargoRequestsTests {
        
        @Test
        @DisplayName("Should search by pickup and delivery cities")
        void shouldSearchByPickupAndDeliveryCities() {
            // Given
            String pickupCity = "Istanbul";
            String deliveryCity = "Ankara";
            List<CargoRequest> expectedResults = Arrays.asList(cargoRequest);
            
            when(cargoRequestRepository.findByPickupCityAndDeliveryCityAndStatus(pickupCity, deliveryCity, RequestStatus.ACTIVE))
                    .thenReturn(expectedResults);
            
            // When
            List<CargoRequest> result = cargoService.searchCargoRequests(pickupCity, deliveryCity, null, null, null, null, null);
            
            // Then
            assertThat(result).hasSize(1);
            assertThat(result).containsExactlyElementsOf(expectedResults);
        }
        
        @Test
        @DisplayName("Should search by max weight")
        void shouldSearchByMaxWeight() {
            // Given
            Integer maxWeight = 100;
            List<CargoRequest> expectedResults = Arrays.asList(cargoRequest);
            
            when(cargoRequestRepository.findByWeightKgLessThanEqualAndStatus(maxWeight, RequestStatus.ACTIVE))
                    .thenReturn(expectedResults);
            
            // When
            List<CargoRequest> result = cargoService.searchCargoRequests(null, null, maxWeight, null, null, null, null);
            
            // Then
            assertThat(result).hasSize(1);
            assertThat(result).containsExactlyElementsOf(expectedResults);
        }
        
        @Test
        @DisplayName("Should search by budget range")
        void shouldSearchByBudgetRange() {
            // Given
            BigDecimal minBudget = new BigDecimal("500");
            BigDecimal maxBudget = new BigDecimal("1000");
            List<CargoRequest> expectedResults = Arrays.asList(cargoRequest);
            
            when(cargoRequestRepository.findByBudgetMaxGreaterThanEqualAndBudgetMinLessThanEqualAndStatus(minBudget, maxBudget, RequestStatus.ACTIVE))
                    .thenReturn(expectedResults);
            
            // When
            List<CargoRequest> result = cargoService.searchCargoRequests(null, null, null, minBudget, maxBudget, null, null);
            
            // Then
            assertThat(result).hasSize(1);
            assertThat(result).containsExactlyElementsOf(expectedResults);
        }
        
        @Test
        @DisplayName("Should search by date range")
        void shouldSearchByDateRange() {
            // Given
            LocalDateTime fromDate = LocalDateTime.now();
            LocalDateTime toDate = LocalDateTime.now().plusDays(7);
            List<CargoRequest> expectedResults = Arrays.asList(cargoRequest);
            
            when(cargoRequestRepository.findByPickupDateBetweenAndStatus(fromDate, toDate, RequestStatus.ACTIVE))
                    .thenReturn(expectedResults);
            
            // When
            List<CargoRequest> result = cargoService.searchCargoRequests(null, null, null, null, null, fromDate, toDate);
            
            // Then
            assertThat(result).hasSize(1);
            assertThat(result).containsExactlyElementsOf(expectedResults);
        }
        
        @Test
        @DisplayName("Should return all active cargo requests when no filters provided")
        void shouldReturnAllActiveCargoRequestsWhenNoFiltersProvided() {
            // Given
            List<CargoRequest> expectedResults = Arrays.asList(cargoRequest);
            
            when(cargoRequestRepository.findByStatus(RequestStatus.ACTIVE)).thenReturn(expectedResults);
            
            // When
            List<CargoRequest> result = cargoService.searchCargoRequests(null, null, null, null, null, null, null);
            
            // Then
            assertThat(result).hasSize(1);
            assertThat(result).containsExactlyElementsOf(expectedResults);
        }
    }
    
    @Nested
    @DisplayName("Get Cargo Request Tests")
    class GetCargoRequestTests {
        
        @Test
        @DisplayName("Should return cargo request successfully")
        void shouldReturnCargoRequestSuccessfully() {
            // Given
            Long cargoId = 1L;
            
            when(cargoRequestRepository.findById(cargoId)).thenReturn(Optional.of(cargoRequest));
            
            // When
            CargoRequest result = cargoService.getCargoRequest(cargoId);
            
            // Then
            assertThat(result).isNotNull();
            assertThat(result).isEqualTo(cargoRequest);
        }
        
        @Test
        @DisplayName("Should throw ResourceNotFoundException when cargo request not found")
        void shouldThrowResourceNotFoundExceptionWhenCargoRequestNotFound() {
            // Given
            Long cargoId = 999L;
            
            when(cargoRequestRepository.findById(cargoId)).thenReturn(Optional.empty());
            
            // When & Then
            assertThatThrownBy(() -> cargoService.getCargoRequest(cargoId))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("CargoRequest")
                    .hasMessageContaining("id")
                    .hasMessageContaining("999");
        }
    }
    
    @Nested
    @DisplayName("Get Cargo Request With Shipper Tests")
    class GetCargoRequestWithShipperTests {
        
        @Test
        @DisplayName("Should return cargo request with shipper details successfully")
        void shouldReturnCargoRequestWithShipperDetailsSuccessfully() {
            // Given
            Long cargoId = 1L;
            
            when(cargoRequestRepository.findById(cargoId)).thenReturn(Optional.of(cargoRequest));
            when(userService.getUserById(shipperUser.getId())).thenReturn(shipperUser);
            when(userService.getUserProfile(shipperUser.getId())).thenReturn(shipperProfile);
            
            // When
            CargoRequestWithShipperDto result = cargoService.getCargoRequestWithShipper(cargoId);
            
            // Then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(cargoRequest.getId());
            assertThat(result.getShipperName()).isEqualTo("John Doe");
            assertThat(result.getShipperEmail()).isEqualTo(shipperUser.getEmail());
            assertThat(result.getShipperCompany()).isEqualTo(shipperProfile.getCompanyName());
        }
        
        @Test
        @DisplayName("Should handle shipper profile not found gracefully")
        void shouldHandleShipperProfileNotFoundGracefully() {
            // Given
            Long cargoId = 1L;
            
            when(cargoRequestRepository.findById(cargoId)).thenReturn(Optional.of(cargoRequest));
            when(userService.getUserById(shipperUser.getId())).thenThrow(new RuntimeException("User not found"));
            
            // When
            CargoRequestWithShipperDto result = cargoService.getCargoRequestWithShipper(cargoId);
            
            // Then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(cargoRequest.getId());
            assertThat(result.getShipperName()).isEqualTo("Bilinmeyen Kullanıcı");
            assertThat(result.getShipperEmail()).isEqualTo("N/A");
            assertThat(result.getShipperCompany()).isEqualTo("N/A");
        }
    }
    
    @Nested
    @DisplayName("Cancel Cargo Request Tests")
    class CancelCargoRequestTests {
        
        @Test
        @DisplayName("Should cancel cargo request successfully when user is owner")
        void shouldCancelCargoRequestSuccessfully() {
            // Given
            Long cargoId = 1L;
            
            when(userService.getCurrentUser()).thenReturn(shipperUser);
            when(cargoRequestRepository.findById(cargoId)).thenReturn(Optional.of(cargoRequest));
            
            // When
            cargoService.cancelCargoRequest(cargoId);
            
            // Then
            assertThat(cargoRequest.getStatus()).isEqualTo(RequestStatus.CANCELLED);
            verify(cargoRequestRepository).save(cargoRequest);
        }
        
        @Test
        @DisplayName("Should throw UnauthorizedException when user is not owner")
        void shouldThrowUnauthorizedExceptionWhenUserIsNotOwner() {
            // Given
            Long cargoId = 1L;
            User anotherUser = createAnotherShipperUser();
            
            when(userService.getCurrentUser()).thenReturn(anotherUser);
            when(cargoRequestRepository.findById(cargoId)).thenReturn(Optional.of(cargoRequest));
            
            // When & Then
            assertThatThrownBy(() -> cargoService.cancelCargoRequest(cargoId))
                    .isInstanceOf(UnauthorizedException.class)
                    .hasMessage("You can only cancel your own cargo requests");
        }
    }
    
    @Nested
    @DisplayName("Complete Cargo Request Tests")
    class CompleteCargoRequestTests {
        
        @Test
        @DisplayName("Should complete cargo request successfully when user is owner")
        void shouldCompleteCargoRequestSuccessfully() {
            // Given
            Long cargoId = 1L;
            
            when(userService.getCurrentUser()).thenReturn(shipperUser);
            when(cargoRequestRepository.findById(cargoId)).thenReturn(Optional.of(cargoRequest));
            
            // When
            cargoService.completeCargoRequest(cargoId);
            
            // Then
            assertThat(cargoRequest.getStatus()).isEqualTo(RequestStatus.COMPLETED);
            verify(cargoRequestRepository).save(cargoRequest);
        }
        
        @Test
        @DisplayName("Should throw UnauthorizedException when user is not owner")
        void shouldThrowUnauthorizedExceptionWhenUserIsNotOwner() {
            // Given
            Long cargoId = 1L;
            User anotherUser = createAnotherShipperUser();
            
            when(userService.getCurrentUser()).thenReturn(anotherUser);
            when(cargoRequestRepository.findById(cargoId)).thenReturn(Optional.of(cargoRequest));
            
            // When & Then
            assertThatThrownBy(() -> cargoService.completeCargoRequest(cargoId))
                    .isInstanceOf(UnauthorizedException.class)
                    .hasMessage("You can only complete your own cargo requests");
        }
    }
    
    // Helper methods for test data creation
    private User createShipperUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("shipper@example.com");
        user.setUserType(UserType.SHIPPER);
        return user;
    }
    
    private User createCarrierUser() {
        User user = new User();
        user.setId(2L);
        user.setEmail("carrier@example.com");
        user.setUserType(UserType.CARRIER);
        return user;
    }
    
    private User createAnotherShipperUser() {
        User user = new User();
        user.setId(3L);
        user.setEmail("another@example.com");
        user.setUserType(UserType.SHIPPER);
        return user;
    }
    
    private CargoRequestDto createCargoRequestDto() {
        CargoRequestDto dto = new CargoRequestDto();
        dto.setTitle("Test Cargo");
        dto.setDescription("Test Description");
        dto.setCargoType(CargoType.GENERAL);
        dto.setWeightKg(50);
        dto.setPickupCity("Istanbul");
        dto.setDeliveryCity("Ankara");
        dto.setPickupAddress("Test Pickup Address");
        dto.setDeliveryAddress("Test Delivery Address");
        dto.setPickupDate(LocalDateTime.now().plusDays(1));
        dto.setDeliveryDate(LocalDateTime.now().plusDays(3));
        dto.setBudgetMin(new BigDecimal("500"));
        dto.setBudgetMax(new BigDecimal("1000"));
        return dto;
    }
    
    private CargoRequestDto createUpdatedCargoRequestDto() {
        CargoRequestDto dto = new CargoRequestDto();
        dto.setTitle("Updated Cargo");
        dto.setDescription("Updated Description");
        dto.setCargoType(CargoType.FRAGILE);
        dto.setWeightKg(75);
        dto.setPickupCity("Izmir");
        dto.setDeliveryCity("Bursa");
        dto.setPickupAddress("Updated Pickup Address");
        dto.setDeliveryAddress("Updated Delivery Address");
        dto.setPickupDate(LocalDateTime.now().plusDays(2));
        dto.setDeliveryDate(LocalDateTime.now().plusDays(4));
        dto.setBudgetMin(new BigDecimal("600"));
        dto.setBudgetMax(new BigDecimal("1200"));
        return dto;
    }
    
    private CargoRequest createCargoRequest() {
        CargoRequest request = new CargoRequest(
                shipperUser.getId(),
                "Test Cargo",
                "Test Description",
                CargoType.GENERAL,
                50,
                "Istanbul",
                "Ankara"
        );
        request.setId(1L);
        request.setStatus(RequestStatus.ACTIVE);
        request.setCreatedDate(LocalDateTime.now());
        return request;
    }
    
    private CargoRequest createAnotherCargoRequest() {
        CargoRequest request = new CargoRequest(
                shipperUser.getId(),
                "Another Cargo",
                "Another Description",
                CargoType.GENERAL,
                30,
                "Izmir",
                "Bursa"
        );
        request.setId(2L);
        request.setStatus(RequestStatus.ACTIVE);
        request.setCreatedDate(LocalDateTime.now());
        return request;
    }
    
    private UserProfile createUserProfile() {
        UserProfile profile = new UserProfile();
        profile.setFirstName("John");
        profile.setLastName("Doe");
        profile.setCompanyName("Test Company");
        return profile;
    }
}