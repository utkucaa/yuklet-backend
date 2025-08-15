package com.project.yuklet.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cargo_requests")
public class CargoRequest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "shipper_id")
    private Long shipperId;
    
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "cargo_type")
    private CargoType cargoType;
    
    @Column(name = "weight_kg")
    private Integer weightKg;
    
    @Column(name = "pickup_city")
    private String pickupCity;
    
    @Column(name = "pickup_address", columnDefinition = "TEXT")
    private String pickupAddress;
    
    @Column(name = "delivery_city")
    private String deliveryCity;
    
    @Column(name = "delivery_address", columnDefinition = "TEXT")
    private String deliveryAddress;
    
    @Column(name = "pickup_date")
    private LocalDateTime pickupDate;
    
    @Column(name = "delivery_date")
    private LocalDateTime deliveryDate;
    
    @Column(name = "budget_min", precision = 10, scale = 2)
    private BigDecimal budgetMin;
    
    @Column(name = "budget_max", precision = 10, scale = 2)
    private BigDecimal budgetMax;
    
    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.ACTIVE;
    
    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();
    
    
    public CargoRequest() {}
    
    public CargoRequest(Long shipperId, String title, String description, CargoType cargoType, 
                      Integer weightKg, String pickupCity, String deliveryCity) {
        this.shipperId = shipperId;
        this.title = title;
        this.description = description;
        this.cargoType = cargoType;
        this.weightKg = weightKg;
        this.pickupCity = pickupCity;
        this.deliveryCity = deliveryCity;
    }
    
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
