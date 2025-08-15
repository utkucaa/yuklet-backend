package com.project.yuklet.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "user_profiles")
public class UserProfile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id")
    private Long userId;
    
    @Column(name = "first_name")
    private String firstName;
    
    @Column(name = "last_name")
    private String lastName;
    
    @Column(name = "company_name")
    private String companyName;
    
    private String city;
    
    @Column(columnDefinition = "TEXT")
    private String address;
    
    @Column(columnDefinition = "TEXT")
    private String about;
    
    private Double rating = 0.0;
    
    @Column(name = "review_count")
    private Integer reviewCount = 0;
    
    public UserProfile() {}
    
    public UserProfile(Long userId, String firstName, String lastName, String companyName, String city) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.companyName = companyName;
        this.city = city;
    }
        
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getCompanyName() {
        return companyName;
    }
    
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getAbout() {
        return about;
    }
    
    public void setAbout(String about) {
        this.about = about;
    }
    
    public Double getRating() {
        return rating;
    }
    
    public void setRating(Double rating) {
        this.rating = rating;
    }
    
    public Integer getReviewCount() {
        return reviewCount;
    }
    
    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }
}
