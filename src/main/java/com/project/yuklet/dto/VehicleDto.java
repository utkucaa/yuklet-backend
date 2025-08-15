package com.project.yuklet.dto;

import com.project.yuklet.entities.VehicleStatus;
import com.project.yuklet.entities.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class VehicleDto {
    
    private Long id;
    private Long carrierId;
    
    @NotNull
    private VehicleType vehicleType;
    
    @NotBlank
    private String plateNumber;
    
    @NotNull
    @Positive
    private Integer maxWeightKg;
    
    private Boolean hasCrane = false;
    private Boolean hasTemperatureControl = false;
    
    private VehicleStatus status;
    
    public VehicleDto() {}
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getCarrierId() {
        return carrierId;
    }
    
    public void setCarrierId(Long carrierId) {
        this.carrierId = carrierId;
    }
    
    public VehicleType getVehicleType() {
        return vehicleType;
    }
    
    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }
    
    public String getPlateNumber() {
        return plateNumber;
    }
    
    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }
    
    public Integer getMaxWeightKg() {
        return maxWeightKg;
    }
    
    public void setMaxWeightKg(Integer maxWeightKg) {
        this.maxWeightKg = maxWeightKg;
    }
    
    public Boolean getHasCrane() {
        return hasCrane;
    }
    
    public void setHasCrane(Boolean hasCrane) {
        this.hasCrane = hasCrane;
    }
    
    public Boolean getHasTemperatureControl() {
        return hasTemperatureControl;
    }
    
    public void setHasTemperatureControl(Boolean hasTemperatureControl) {
        this.hasTemperatureControl = hasTemperatureControl;
    }
    
    public VehicleStatus getStatus() {
        return status;
    }
    
    public void setStatus(VehicleStatus status) {
        this.status = status;
    }
}
