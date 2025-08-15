package com.project.yuklet.dto;

import com.project.yuklet.entities.CargoType;
import com.project.yuklet.entities.RequestStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CargoRequestDto {
    
    private Long id;
    private Long shipperId;
    
    @NotBlank
    private String title;
    
    private String description;
    
    @NotNull
    private CargoType cargoType;
    
    @NotNull
    @Positive
    private Integer weightKg;
    
    @NotBlank
    private String pickupCity;
    
    private String pickupAddress;
    
    @NotBlank
    private String deliveryCity;
    
    private String deliveryAddress;
    
    private LocalDateTime pickupDate;
    private LocalDateTime deliveryDate;
    
    private BigDecimal budgetMin;
    private BigDecimal budgetMax;
    
    private RequestStatus status;
    private LocalDateTime createdDate;
    
   public CargoRequestDto() {}
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getShipperId() {
        return shipperId;
    }
    
    public void setShipperId(Long shipperId) {
        this.shipperId = shipperId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public CargoType getCargoType() {
        return cargoType;
    }
    
    public void setCargoType(CargoType cargoType) {
        this.cargoType = cargoType;
    }
    
    public Integer getWeightKg() {
        return weightKg;
    }
    
    public void setWeightKg(Integer weightKg) {
        this.weightKg = weightKg;
    }
    
    public String getPickupCity() {
        return pickupCity;
    }
    
    public void setPickupCity(String pickupCity) {
        this.pickupCity = pickupCity;
    }
    
    public String getPickupAddress() {
        return pickupAddress;
    }
    
    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }
    
    public String getDeliveryCity() {
        return deliveryCity;
    }
    
    public void setDeliveryCity(String deliveryCity) {
        this.deliveryCity = deliveryCity;
    }
    
    public String getDeliveryAddress() {
        return deliveryAddress;
    }
    
    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }
    
    public LocalDateTime getPickupDate() {
        return pickupDate;
    }
    
    public void setPickupDate(LocalDateTime pickupDate) {
        this.pickupDate = pickupDate;
    }
    
    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }
    
    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }
    
    public BigDecimal getBudgetMin() {
        return budgetMin;
    }
    
    public void setBudgetMin(BigDecimal budgetMin) {
        this.budgetMin = budgetMin;
    }
    
    public BigDecimal getBudgetMax() {
        return budgetMax;
    }
    
    public void setBudgetMax(BigDecimal budgetMax) {
        this.budgetMax = budgetMax;
    }
    
    public RequestStatus getStatus() {
        return status;
    }
    
    public void setStatus(RequestStatus status) {
        this.status = status;
    }
    
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
