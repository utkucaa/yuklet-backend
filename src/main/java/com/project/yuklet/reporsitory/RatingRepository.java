package com.project.yuklet.reporsitory;

import com.project.yuklet.entities.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    
    List<Rating> findByRatedUserId(Long ratedUserId);
    
    List<Rating> findByRaterId(Long raterId);
    
    Optional<Rating> findByRaterIdAndCargoRequestId(Long raterId, Long cargoRequestId);
    
    Long countByRatedUserId(Long ratedUserId);
    
    boolean existsByRaterIdAndCargoRequestId(Long raterId, Long cargoRequestId);
}
