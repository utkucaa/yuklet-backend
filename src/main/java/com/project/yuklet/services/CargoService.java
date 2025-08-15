package com.project.yuklet.services;

import com.project.yuklet.dto.CargoRequestDto;
import com.project.yuklet.entities.CargoRequest;
import com.project.yuklet.entities.RequestStatus;
import com.project.yuklet.entities.User;
import com.project.yuklet.entities.UserType;
import com.project.yuklet.exception.ResourceNotFoundException;
import com.project.yuklet.exception.UnauthorizedException;
import com.project.yuklet.reporsitory.CargoRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CargoService {
    
    @Autowired
    private CargoRequestRepository cargoRequestRepository;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private NotificationService notificationService;
    
    public CargoRequest createCargoRequest(CargoRequestDto cargoRequestDto) {
        User currentUser = userService.getCurrentUser();
        
        if (currentUser.getUserType() != UserType.SHIPPER) {
            throw new UnauthorizedException("Only shippers can create cargo requests");
        }
        
        CargoRequest cargoRequest = new CargoRequest(currentUser.getId(),
                cargoRequestDto.getTitle(),
                cargoRequestDto.getDescription(),
                cargoRequestDto.getCargoType(),
                cargoRequestDto.getWeightKg(),
                cargoRequestDto.getPickupCity(),
                cargoRequestDto.getDeliveryCity());
        
        cargoRequest.setPickupAddress(cargoRequestDto.getPickupAddress());
        cargoRequest.setDeliveryAddress(cargoRequestDto.getDeliveryAddress());
        cargoRequest.setPickupDate(cargoRequestDto.getPickupDate());
        cargoRequest.setDeliveryDate(cargoRequestDto.getDeliveryDate());
        cargoRequest.setBudgetMin(cargoRequestDto.getBudgetMin());
        cargoRequest.setBudgetMax(cargoRequestDto.getBudgetMax());
        
        CargoRequest savedRequest = cargoRequestRepository.save(cargoRequest);
                        
        notificationService.notifyCarriersAboutNewCargo(savedRequest);
        
        return savedRequest;
    }
    
    public CargoRequest updateCargoRequest(Long cargoId, CargoRequestDto cargoRequestDto) {
        User currentUser = userService.getCurrentUser();
        CargoRequest cargoRequest = cargoRequestRepository.findById(cargoId)
                .orElseThrow(() -> new ResourceNotFoundException("CargoRequest", "id", cargoId));
        
        if (!cargoRequest.getShipperId().equals(currentUser.getId())) {
            throw new UnauthorizedException("You can only update your own cargo requests");
        }
        
        if (cargoRequest.getStatus() != RequestStatus.ACTIVE) {
            throw new UnauthorizedException("Cannot update completed or cancelled cargo requests");
        }
        
        cargoRequest.setTitle(cargoRequestDto.getTitle());
        cargoRequest.setDescription(cargoRequestDto.getDescription());
        cargoRequest.setCargoType(cargoRequestDto.getCargoType());
        cargoRequest.setWeightKg(cargoRequestDto.getWeightKg());
        cargoRequest.setPickupCity(cargoRequestDto.getPickupCity());
        cargoRequest.setDeliveryCity(cargoRequestDto.getDeliveryCity());
        cargoRequest.setPickupAddress(cargoRequestDto.getPickupAddress());
        cargoRequest.setDeliveryAddress(cargoRequestDto.getDeliveryAddress());
        cargoRequest.setPickupDate(cargoRequestDto.getPickupDate());
        cargoRequest.setDeliveryDate(cargoRequestDto.getDeliveryDate());
        cargoRequest.setBudgetMin(cargoRequestDto.getBudgetMin());
        cargoRequest.setBudgetMax(cargoRequestDto.getBudgetMax());
        
        return cargoRequestRepository.save(cargoRequest);
    }
    
    public List<CargoRequest> getMyCargoRequests() {
        User currentUser = userService.getCurrentUser();
        return cargoRequestRepository.findByShipperId(currentUser.getId());
    }
    
    public List<CargoRequest> searchCargoRequests(String pickupCity, String deliveryCity, 
                                                 Integer maxWeight, BigDecimal minBudget, 
                                                 BigDecimal maxBudget, LocalDateTime fromDate, 
                                                 LocalDateTime toDate) {
        if (pickupCity != null && deliveryCity != null) {
            return cargoRequestRepository.findByPickupCityAndDeliveryCityAndStatus(pickupCity, deliveryCity, RequestStatus.ACTIVE);
        } else if (maxWeight != null) {
            return cargoRequestRepository.findByWeightKgLessThanEqualAndStatus(maxWeight, RequestStatus.ACTIVE);
        } else if (minBudget != null && maxBudget != null) {
            return cargoRequestRepository.findByBudgetMaxGreaterThanEqualAndBudgetMinLessThanEqualAndStatus(minBudget, maxBudget, RequestStatus.ACTIVE);
        } else if (fromDate != null && toDate != null) {
            return cargoRequestRepository.findByPickupDateBetweenAndStatus(fromDate, toDate, RequestStatus.ACTIVE);
        } else {
            return cargoRequestRepository.findByStatus(RequestStatus.ACTIVE);
        }
    }
    
    public CargoRequest getCargoRequest(Long cargoId) {
        return cargoRequestRepository.findById(cargoId)
                .orElseThrow(() -> new ResourceNotFoundException("CargoRequest", "id", cargoId));
    }
    
    public void cancelCargoRequest(Long cargoId) {
        User currentUser = userService.getCurrentUser();
        CargoRequest cargoRequest = cargoRequestRepository.findById(cargoId)
                .orElseThrow(() -> new ResourceNotFoundException("CargoRequest", "id", cargoId));
        
        if (!cargoRequest.getShipperId().equals(currentUser.getId())) {
            throw new UnauthorizedException("You can only cancel your own cargo requests");
        }
        
        cargoRequest.setStatus(RequestStatus.CANCELLED);
        cargoRequestRepository.save(cargoRequest);
    }
    
    public void completeCargoRequest(Long cargoId) {
        User currentUser = userService.getCurrentUser();
        CargoRequest cargoRequest = cargoRequestRepository.findById(cargoId)
                .orElseThrow(() -> new ResourceNotFoundException("CargoRequest", "id", cargoId));
        
        if (!cargoRequest.getShipperId().equals(currentUser.getId())) {
            throw new UnauthorizedException("You can only complete your own cargo requests");
        }
        
        cargoRequest.setStatus(RequestStatus.COMPLETED);
        cargoRequestRepository.save(cargoRequest);
    }
}
