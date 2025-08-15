package com.project.yuklet.services;

import com.project.yuklet.dto.VehicleDto;
import com.project.yuklet.entities.User;
import com.project.yuklet.entities.UserType;
import com.project.yuklet.entities.Vehicle;
import com.project.yuklet.entities.VehicleStatus;
import com.project.yuklet.exception.BadRequestException;
import com.project.yuklet.exception.ResourceNotFoundException;
import com.project.yuklet.exception.UnauthorizedException;
import com.project.yuklet.reporsitory.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {
    
    @Autowired
    private VehicleRepository vehicleRepository;
    
    @Autowired
    private UserService userService;
    
    public Vehicle addVehicle(VehicleDto vehicleDto) {
        User currentUser = userService.getCurrentUser();
        
        if (currentUser.getUserType() != UserType.CARRIER) {
            throw new UnauthorizedException("Only carriers can add vehicles");
        }
        
        if (vehicleRepository.existsByPlateNumber(vehicleDto.getPlateNumber())) {
            throw new BadRequestException("Vehicle with this plate number already exists");
        }
        
        Vehicle vehicle = new Vehicle(currentUser.getId(),
                vehicleDto.getVehicleType(),
                vehicleDto.getPlateNumber(),
                vehicleDto.getMaxWeightKg());
        
        vehicle.setHasCrane(vehicleDto.getHasCrane());
        vehicle.setHasTemperatureControl(vehicleDto.getHasTemperatureControl());
        
        return vehicleRepository.save(vehicle);
    }
    
    public Vehicle updateVehicle(Long vehicleId, VehicleDto vehicleDto) {
        User currentUser = userService.getCurrentUser();
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle", "id", vehicleId));
        
        if (!vehicle.getCarrierId().equals(currentUser.getId())) {
            throw new UnauthorizedException("You can only update your own vehicles");
        }
                    
        if (!vehicle.getPlateNumber().equals(vehicleDto.getPlateNumber()) &&
                vehicleRepository.existsByPlateNumber(vehicleDto.getPlateNumber())) {
            throw new BadRequestException("Vehicle with this plate number already exists");
        }
        
        vehicle.setVehicleType(vehicleDto.getVehicleType());
        vehicle.setPlateNumber(vehicleDto.getPlateNumber());
        vehicle.setMaxWeightKg(vehicleDto.getMaxWeightKg());
        vehicle.setHasCrane(vehicleDto.getHasCrane());
        vehicle.setHasTemperatureControl(vehicleDto.getHasTemperatureControl());
        
        return vehicleRepository.save(vehicle);
    }
    
    public List<Vehicle> getMyVehicles() {
        User currentUser = userService.getCurrentUser();
        return vehicleRepository.findByCarrierIdAndStatus(currentUser.getId(), VehicleStatus.ACTIVE);
    }
    
    public List<Vehicle> getCarrierVehicles(Long carrierId) {
        return vehicleRepository.findByCarrierIdAndStatus(carrierId, VehicleStatus.ACTIVE);
    }
    
    public Vehicle getVehicle(Long vehicleId) {
        return vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle", "id", vehicleId));
    }
    
    public void deleteVehicle(Long vehicleId) {
        User currentUser = userService.getCurrentUser();
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle", "id", vehicleId));
        
        if (!vehicle.getCarrierId().equals(currentUser.getId())) {
            throw new UnauthorizedException("You can only delete your own vehicles");
        }
        
        vehicle.setStatus(VehicleStatus.INACTIVE);
        vehicleRepository.save(vehicle);
    }
}
