package com.project.yuklet.services;

import com.project.yuklet.entities.Favorite;
import com.project.yuklet.entities.FavoriteType;
import com.project.yuklet.entities.User;
import com.project.yuklet.exception.BadRequestException;
import com.project.yuklet.reporsitory.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FavoriteService {
    
    @Autowired
    private FavoriteRepository favoriteRepository;
    
    @Autowired
    private UserService userService;
    
    @Transactional
    public Favorite addToFavorites(Long entityId, FavoriteType entityType) {
        User currentUser = userService.getCurrentUser();
                        
        if (favoriteRepository.existsByUserIdAndEntityIdAndEntityType(currentUser.getId(), entityId, entityType)) {
            throw new BadRequestException("Item is already in favorites");
        }
        
        Favorite favorite = new Favorite(currentUser.getId(), entityId, entityType);
        return favoriteRepository.save(favorite);
    }
    
    @Transactional
    public void removeFromFavorites(Long entityId, FavoriteType entityType) {
        User currentUser = userService.getCurrentUser();
        favoriteRepository.deleteByUserIdAndEntityIdAndEntityType(currentUser.getId(), entityId, entityType);
    }
    
    public List<Favorite> getUserFavorites() {
        User currentUser = userService.getCurrentUser();
        return favoriteRepository.findByUserIdOrderByCreatedDateDesc(currentUser.getId());
    }
    
    public List<Favorite> getUserFavoritesByType(FavoriteType entityType) {
        User currentUser = userService.getCurrentUser();
        return favoriteRepository.findByUserIdAndEntityType(currentUser.getId(), entityType);
    }
    
    public boolean isFavorite(Long entityId, FavoriteType entityType) {
        User currentUser = userService.getCurrentUser();
        return favoriteRepository.existsByUserIdAndEntityIdAndEntityType(currentUser.getId(), entityId, entityType);
    }
}
