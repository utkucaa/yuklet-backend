package com.project.yuklet.reporsitory;

import com.project.yuklet.entities.Vehicle;
import com.project.yuklet.entities.VehicleStatus;
import com.project.yuklet.entities.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    
    List<Vehicle> findByCarrierId(Long carrierId);
    
    List<Vehicle> findByCarrierIdAndStatus(Long carrierId, VehicleStatus status);
    
    Optional<Vehicle> findByPlateNumber(String plateNumber);
    
    List<Vehicle> findByVehicleType(VehicleType vehicleType);
    
    @Query("SELECT v FROM Vehicle v WHERE v.maxWeightKg >= :minWeight AND v.status = :status")
    List<Vehicle> findByMinWeightAndStatus(Integer minWeight, VehicleStatus status);
    
    boolean existsByPlateNumber(String plateNumber);
}
