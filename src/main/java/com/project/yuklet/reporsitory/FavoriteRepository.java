package com.project.yuklet.reporsitory;

import com.project.yuklet.entities.Favorite;
import com.project.yuklet.entities.FavoriteType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    
    List<Favorite> findByUserIdOrderByCreatedDateDesc(Long userId);
    
    List<Favorite> findByUserIdAndEntityType(Long userId, FavoriteType entityType);
    
    Optional<Favorite> findByUserIdAndEntityIdAndEntityType(Long userId, Long entityId, FavoriteType entityType);
    
    boolean existsByUserIdAndEntityIdAndEntityType(Long userId, Long entityId, FavoriteType entityType);
    
    void deleteByUserIdAndEntityIdAndEntityType(Long userId, Long entityId, FavoriteType entityType);
}
