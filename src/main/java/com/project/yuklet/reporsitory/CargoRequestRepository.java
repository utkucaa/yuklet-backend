package com.project.yuklet.reporsitory;

import com.project.yuklet.entities.CargoRequest;
import com.project.yuklet.entities.CargoType;
import com.project.yuklet.entities.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CargoRequestRepository extends JpaRepository<CargoRequest, Long> {
    
    List<CargoRequest> findByShipperId(Long shipperId);
    
    List<CargoRequest> findByStatus(RequestStatus status);
    
    List<CargoRequest> findByPickupCity(String pickupCity);
    
    List<CargoRequest> findByDeliveryCity(String deliveryCity);
    
    List<CargoRequest> findByCargoType(CargoType cargoType);
    
    List<CargoRequest> findByPickupCityAndDeliveryCityAndStatus(String pickupCity, String deliveryCity, RequestStatus status);
    
    List<CargoRequest> findByWeightKgLessThanEqualAndStatus(Integer maxWeight, RequestStatus status);
    
    List<CargoRequest> findByBudgetMaxGreaterThanEqualAndBudgetMinLessThanEqualAndStatus(BigDecimal minBudget, BigDecimal maxBudget, RequestStatus status);
    
    List<CargoRequest> findByPickupDateBetweenAndStatus(LocalDateTime fromDate, LocalDateTime toDate, RequestStatus status);
}
