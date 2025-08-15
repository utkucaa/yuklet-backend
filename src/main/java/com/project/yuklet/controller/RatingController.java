package com.project.yuklet.controller;

import com.project.yuklet.dto.ApiResponse;
import com.project.yuklet.dto.RatingDto;
import com.project.yuklet.entities.Rating;
import com.project.yuklet.services.RatingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
@CrossOrigin(origins = "*", maxAge = 3600)
public class RatingController {
    
    @Autowired
    private RatingService ratingService;
    
    @PostMapping
    public ResponseEntity<ApiResponse<Rating>> submitRating(@Valid @RequestBody RatingDto ratingDto) {
        Rating rating = ratingService.submitRating(ratingDto);
        return ResponseEntity.ok(ApiResponse.success("Rating submitted successfully", rating));
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<Rating>>> getUserRatings(@PathVariable Long userId) {
        List<Rating> ratings = ratingService.getUserRatings(userId);
        return ResponseEntity.ok(ApiResponse.success(ratings));
    }
    
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<Rating>>> getMyRatings() {
        List<Rating> ratings = ratingService.getMyRatings();
        return ResponseEntity.ok(ApiResponse.success(ratings));
    }
    
    @GetMapping("/given")
    public ResponseEntity<ApiResponse<List<Rating>>> getRatingsGivenByMe() {
        List<Rating> ratings = ratingService.getRatingsGivenByMe();
        return ResponseEntity.ok(ApiResponse.success(ratings));
    }
    
    @GetMapping("/user/{userId}/average")
    public ResponseEntity<ApiResponse<Double>> getUserAverageRating(@PathVariable Long userId) {
        Double averageRating = ratingService.getUserAverageRating(userId);
        return ResponseEntity.ok(ApiResponse.success(averageRating));
    }
    
    @GetMapping("/user/{userId}/count")
    public ResponseEntity<ApiResponse<Long>> getUserRatingCount(@PathVariable Long userId) {
        Long count = ratingService.getUserRatingCount(userId);
        return ResponseEntity.ok(ApiResponse.success(count));
    }
}
