package com.project.yuklet.services;

import com.project.yuklet.dto.RatingDto;
import com.project.yuklet.entities.Rating;
import com.project.yuklet.entities.User;
import com.project.yuklet.entities.UserProfile;
import com.project.yuklet.exception.BadRequestException;
import com.project.yuklet.exception.ResourceNotFoundException;
import com.project.yuklet.reporsitory.RatingRepository;
import com.project.yuklet.reporsitory.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RatingService {
    
    @Autowired
    private RatingRepository ratingRepository;
    
    @Autowired
    private UserProfileRepository userProfileRepository;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private NotificationService notificationService;
    
    @Transactional
    public Rating submitRating(RatingDto ratingDto) {
        User currentUser = userService.getCurrentUser();
        
        if (ratingRepository.existsByRaterIdAndCargoRequestId(currentUser.getId(), ratingDto.getCargoRequestId())) {
            throw new BadRequestException("You have already rated this cargo request");
        }
        
        if (currentUser.getId().equals(ratingDto.getRatedUserId())) {
            throw new BadRequestException("You cannot rate yourself");
        }
        
        Rating rating = new Rating(currentUser.getId(),
                ratingDto.getRatedUserId(),
                ratingDto.getCargoRequestId(),
                ratingDto.getRating(),
                ratingDto.getComment());
        
        rating = ratingRepository.save(rating);
        
        updateUserRating(ratingDto.getRatedUserId());
        
        notificationService.notifyNewRating(ratingDto.getRatedUserId(), currentUser.getId(), ratingDto.getRating());
        
        return rating;
    }
    
    private void updateUserRating(Long userId) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("UserProfile", "userId", userId));
        
        List<Rating> ratings = ratingRepository.findByRatedUserId(userId);
        Double averageRating = ratings.stream()
                .mapToDouble(Rating::getRating)
                .average()
                .orElse(0.0);
        Long reviewCount = ratingRepository.countByRatedUserId(userId);
        
        userProfile.setRating(averageRating);
        userProfile.setReviewCount(reviewCount.intValue());
        
        userProfileRepository.save(userProfile);
    }
    
    public List<Rating> getUserRatings(Long userId) {
        return ratingRepository.findByRatedUserId(userId);
    }
    
    public List<Rating> getMyRatings() {
        User currentUser = userService.getCurrentUser();
        return ratingRepository.findByRatedUserId(currentUser.getId());
    }
    
    public List<Rating> getRatingsGivenByMe() {
        User currentUser = userService.getCurrentUser();
        return ratingRepository.findByRaterId(currentUser.getId());
    }
    
    public Double getUserAverageRating(Long userId) {
        List<Rating> ratings = ratingRepository.findByRatedUserId(userId);
        return ratings.stream()
                .mapToDouble(Rating::getRating)
                .average()
                .orElse(0.0);
    }
    
    public Long getUserRatingCount(Long userId) {
        return ratingRepository.countByRatedUserId(userId);
    }
}
