package com.project.yuklet.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class RatingDto {
    
    private Long id;
    private Long raterId;
    
    @NotNull
    private Long ratedUserId;
    
    @NotNull
    private Long cargoRequestId;
    
    @NotNull
    @Min(1)
    @Max(5)
    private Integer rating;
    
    private String comment;
    private LocalDateTime createdDate;
    
    public RatingDto() {}
    
    public RatingDto(Long ratedUserId, Long cargoRequestId, Integer rating, String comment) {
        this.ratedUserId = ratedUserId;
        this.cargoRequestId = cargoRequestId;
        this.rating = rating;
        this.comment = comment;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getRaterId() {
        return raterId;
    }
    
    public void setRaterId(Long raterId) {
        this.raterId = raterId;
    }
    
    public Long getRatedUserId() {
        return ratedUserId;
    }
    
    public void setRatedUserId(Long ratedUserId) {
        this.ratedUserId = ratedUserId;
    }
    
    public Long getCargoRequestId() {
        return cargoRequestId;
    }
    
    public void setCargoRequestId(Long cargoRequestId) {
        this.cargoRequestId = cargoRequestId;
    }
    
    public Integer getRating() {
        return rating;
    }
    
    public void setRating(Integer rating) {
        this.rating = rating;
    }
    
    public String getComment() {
        return comment;
    }
    
    public void setComment(String comment) {
        this.comment = comment;
    }
    
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
