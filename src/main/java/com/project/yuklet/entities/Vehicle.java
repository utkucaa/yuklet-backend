package com.project.yuklet.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "vehicles")
public class Vehicle {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "carrier_id")
    private Long carrierId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type")
    private VehicleType vehicleType;
    
    @Column(name = "plate_number", unique = true)
    private String plateNumber;
    
    @Column(name = "max_weight_kg")
    private Integer maxWeightKg;
    
    @Column(name = "has_crane")
    private Boolean hasCrane = false;
    
    @Column(name = "has_temperature_control")
    private Boolean hasTemperatureControl = false;
    
    @Enumerated(EnumType.STRING)
    private VehicleStatus status = VehicleStatus.ACTIVE;
    
    public Vehicle() {}
    
    public Vehicle(Long carrierId, VehicleType vehicleType, String plateNumber, Integer maxWeightKg) {
        this.carrierId = carrierId;
        this.vehicleType = vehicleType;
        this.plateNumber = plateNumber;
        this.maxWeightKg = maxWeightKg;
    }
                
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
