package com.project.yuklet.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;

@Entity
@Table(name = "ratings")
public class Rating {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "rater_id")
    private Long raterId;
    
    @Column(name = "rated_user_id")
    private Long ratedUserId;
    
    @Column(name = "cargo_request_id")
    private Long cargoRequestId;
    
    @Min(1)
    @Max(5)
    private Integer rating;
    
    @Column(columnDefinition = "TEXT")
    private String comment;
    
    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();
    
    public Rating() {}
    
    public Rating(Long raterId, Long ratedUserId, Long cargoRequestId, Integer rating, String comment) {
        this.raterId = raterId;
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
