package com.project.yuklet.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transport_offers")
public class TransportOffer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "carrier_id")
    private Long carrierId;
    
    @Column(name = "vehicle_id")
    private Long vehicleId;
    
    @Column(name = "from_city")
    private String fromCity;
    
    @Column(name = "to_city")
    private String toCity;
    
    @Column(name = "available_date")
    private LocalDateTime availableDate;
    
    @Column(name = "available_weight_kg")
    private Integer availableWeightKg;
    
    @Column(name = "suggested_price", precision = 10, scale = 2)
    private BigDecimal suggestedPrice;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.ACTIVE;
    
    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();
    
    public TransportOffer() {}
    
    public TransportOffer(Long carrierId, Long vehicleId, String fromCity, String toCity, 
                         LocalDateTime availableDate, Integer availableWeightKg) {
        this.carrierId = carrierId;
        this.vehicleId = vehicleId;
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.availableDate = availableDate;
        this.availableWeightKg = availableWeightKg;
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
