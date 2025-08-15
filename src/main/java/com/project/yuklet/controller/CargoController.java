package com.project.yuklet.controller;

import com.project.yuklet.dto.ApiResponse;
import com.project.yuklet.dto.CargoRequestDto;
import com.project.yuklet.entities.CargoRequest;
import com.project.yuklet.services.CargoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/cargo")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CargoController {
    
    @Autowired
    private CargoService cargoService;
    
    @PostMapping
    @PreAuthorize("hasRole('SHIPPER')")
    public ResponseEntity<ApiResponse<CargoRequest>> createCargoRequest(@Valid @RequestBody CargoRequestDto cargoRequestDto) {
        CargoRequest cargoRequest = cargoService.createCargoRequest(cargoRequestDto);
        return ResponseEntity.ok(ApiResponse.success("Cargo request created successfully", cargoRequest));
    }
    
    @PutMapping("/{cargoId}")
    @PreAuthorize("hasRole('SHIPPER')")
    public ResponseEntity<ApiResponse<CargoRequest>> updateCargoRequest(@PathVariable Long cargoId,
                                                                       @Valid @RequestBody CargoRequestDto cargoRequestDto) {
        CargoRequest cargoRequest = cargoService.updateCargoRequest(cargoId, cargoRequestDto);
        return ResponseEntity.ok(ApiResponse.success("Cargo request updated successfully", cargoRequest));
    }
    
    @GetMapping("/my")
    @PreAuthorize("hasRole('SHIPPER')")
    public ResponseEntity<ApiResponse<List<CargoRequest>>> getMyCargoRequests() {
        List<CargoRequest> cargoRequests = cargoService.getMyCargoRequests();
        return ResponseEntity.ok(ApiResponse.success(cargoRequests));
    }
    
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<CargoRequest>>> searchCargoRequests(
            @RequestParam(required = false) String pickupCity,
            @RequestParam(required = false) String deliveryCity,
            @RequestParam(required = false) Integer maxWeight,
            @RequestParam(required = false) BigDecimal minBudget,
            @RequestParam(required = false) BigDecimal maxBudget,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate) {
        
        List<CargoRequest> cargoRequests = cargoService.searchCargoRequests(
                pickupCity, deliveryCity, maxWeight, minBudget, maxBudget, fromDate, toDate);
        return ResponseEntity.ok(ApiResponse.success(cargoRequests));
    }
    
    @GetMapping("/{cargoId}")
    public ResponseEntity<ApiResponse<CargoRequest>> getCargoRequest(@PathVariable Long cargoId) {
        CargoRequest cargoRequest = cargoService.getCargoRequest(cargoId);
        return ResponseEntity.ok(ApiResponse.success(cargoRequest));
    }
    
    @PutMapping("/{cargoId}/cancel")
    @PreAuthorize("hasRole('SHIPPER')")
    public ResponseEntity<ApiResponse<String>> cancelCargoRequest(@PathVariable Long cargoId) {
        cargoService.cancelCargoRequest(cargoId);
        return ResponseEntity.ok(ApiResponse.success("Cargo request cancelled successfully", null));
    }
    
    @PutMapping("/{cargoId}/complete")
    @PreAuthorize("hasRole('SHIPPER')")
    public ResponseEntity<ApiResponse<String>> completeCargoRequest(@PathVariable Long cargoId) {
        cargoService.completeCargoRequest(cargoId);
        return ResponseEntity.ok(ApiResponse.success("Cargo request completed successfully", null));
    }
}
