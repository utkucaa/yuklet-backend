package com.project.yuklet.dto;

import com.project.yuklet.entities.CargoType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SearchCriteria {
    
    private String pickupCity;
    private String deliveryCity;
    private String fromCity;
    private String toCity;
    
    private Integer minWeight;
    private Integer maxWeight;
    
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    
    private CargoType cargoType;
    
    private String keyword;
    
    private String sortBy = "createdDate";
    private String sortDirection = "DESC";
    
    private int page = 0;
    private int size = 20;
    
    public SearchCriteria() {}
    
    public String getPickupCity() {
        return pickupCity;
    }
    
    public void setPickupCity(String pickupCity) {
        this.pickupCity = pickupCity;
    }
    
    public String getDeliveryCity() {
        return deliveryCity;
    }
    
    public void setDeliveryCity(String deliveryCity) {
        this.deliveryCity = deliveryCity;
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
    
    public Integer getMinWeight() {
        return minWeight;
    }
    
    public void setMinWeight(Integer minWeight) {
        this.minWeight = minWeight;
    }
    
    public Integer getMaxWeight() {
        return maxWeight;
    }
    
    public void setMaxWeight(Integer maxWeight) {
        this.maxWeight = maxWeight;
    }
    
    public BigDecimal getMinPrice() {
        return minPrice;
    }
    
    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }
    
    public BigDecimal getMaxPrice() {
        return maxPrice;
    }
    
    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }
    
    public LocalDateTime getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }
    
    public LocalDateTime getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
    
    public CargoType getCargoType() {
        return cargoType;
    }
    
    public void setCargoType(CargoType cargoType) {
        this.cargoType = cargoType;
    }
    
    public String getKeyword() {
        return keyword;
    }
    
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    
    public String getSortBy() {
        return sortBy;
    }
    
    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }
    
    public String getSortDirection() {
        return sortDirection;
    }
    
    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }
    
    public int getPage() {
        return page;
    }
    
    public void setPage(int page) {
        this.page = page;
    }
    
    public int getSize() {
        return size;
    }
    
    public void setSize(int size) {
        this.size = size;
    }
}
