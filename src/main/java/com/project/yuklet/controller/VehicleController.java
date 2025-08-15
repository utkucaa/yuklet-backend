package com.project.yuklet.controller;

import com.project.yuklet.dto.ApiResponse;
import com.project.yuklet.dto.VehicleDto;
import com.project.yuklet.entities.Vehicle;
import com.project.yuklet.services.VehicleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@CrossOrigin(origins = "*", maxAge = 3600)
public class VehicleController {
    
    @Autowired
    private VehicleService vehicleService;
    
    @PostMapping
    @PreAuthorize("hasRole('CARRIER')")
    public ResponseEntity<ApiResponse<Vehicle>> addVehicle(@Valid @RequestBody VehicleDto vehicleDto) {
        Vehicle vehicle = vehicleService.addVehicle(vehicleDto);
        return ResponseEntity.ok(ApiResponse.success("Vehicle added successfully", vehicle));
    }
    
    @PutMapping("/{vehicleId}")
    @PreAuthorize("hasRole('CARRIER')")
    public ResponseEntity<ApiResponse<Vehicle>> updateVehicle(@PathVariable Long vehicleId,
                                                             @Valid @RequestBody VehicleDto vehicleDto) {
        Vehicle vehicle = vehicleService.updateVehicle(vehicleId, vehicleDto);
        return ResponseEntity.ok(ApiResponse.success("Vehicle updated successfully", vehicle));
    }
    
    @GetMapping("/my")
    @PreAuthorize("hasRole('CARRIER')")
    public ResponseEntity<ApiResponse<List<Vehicle>>> getMyVehicles() {
        List<Vehicle> vehicles = vehicleService.getMyVehicles();
        return ResponseEntity.ok(ApiResponse.success(vehicles));
    }
    
    @GetMapping("/carrier/{carrierId}")
    public ResponseEntity<ApiResponse<List<Vehicle>>> getCarrierVehicles(@PathVariable Long carrierId) {
        List<Vehicle> vehicles = vehicleService.getCarrierVehicles(carrierId);
        return ResponseEntity.ok(ApiResponse.success(vehicles));
    }
    
    @GetMapping("/{vehicleId}")
    public ResponseEntity<ApiResponse<Vehicle>> getVehicle(@PathVariable Long vehicleId) {
        Vehicle vehicle = vehicleService.getVehicle(vehicleId);
        return ResponseEntity.ok(ApiResponse.success(vehicle));
    }
    
    @DeleteMapping("/{vehicleId}")
    @PreAuthorize("hasRole('CARRIER')")
    public ResponseEntity<ApiResponse<String>> deleteVehicle(@PathVariable Long vehicleId) {
        vehicleService.deleteVehicle(vehicleId);
        return ResponseEntity.ok(ApiResponse.success("Vehicle deleted successfully", null));
    }
}
