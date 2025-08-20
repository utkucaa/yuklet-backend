package com.project.yuklet.dto;

import com.project.yuklet.entities.RequestStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransportOfferDto {
    
    private Long id;
    private Long carrierId;
    
    private Long vehicleId;
    
    @NotBlank
    private String fromCity;
    
    @NotBlank
    private String toCity;
    
    @NotNull
    private LocalDateTime availableDate;
    
    @NotNull
    @Positive
    private Integer availableWeightKg;
    
    private BigDecimal suggestedPrice;
    private String description;
    
    private RequestStatus status;
    private LocalDateTime createdDate;
    
    public TransportOfferDto() {}
    
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
    
    public Long getVehicleId() {
        return vehicleId;
    }
    
    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }
    
    public String getFromCity() {
        return fromCity;
    }
    
    public void setFromCity(String fromCity) {
        this.fromCity = fromCity;
    }
    
    public String getToCity() {
        return toCity;
    }
    
    public void setToCity(String toCity) {
        this.toCity = toCity;
    }
    
    public LocalDateTime getAvailableDate() {
        return availableDate;
    }
    
    public void setAvailableDate(LocalDateTime availableDate) {
        this.availableDate = availableDate;
    }
    
    public Integer getAvailableWeightKg() {
        return availableWeightKg;
    }
    
    public void setAvailableWeightKg(Integer availableWeightKg) {
        this.availableWeightKg = availableWeightKg;
    }
    
    public BigDecimal getSuggestedPrice() {
        return suggestedPrice;
    }
    
    public void setSuggestedPrice(BigDecimal suggestedPrice) {
        this.suggestedPrice = suggestedPrice;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
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
