package com.project.yuklet.reporsitory;

import com.project.yuklet.entities.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    
    Optional<UserProfile> findByUserId(Long userId);
    
    List<UserProfile> findByCity(String city);
    
    List<UserProfile> findByCompanyNameContainingIgnoreCase(String companyName);
    
    List<UserProfile> findByRatingGreaterThanEqualOrderByRatingDesc(Double minRating);
}
