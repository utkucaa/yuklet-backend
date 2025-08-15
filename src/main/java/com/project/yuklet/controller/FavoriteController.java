package com.project.yuklet.controller;

import com.project.yuklet.dto.ApiResponse;
import com.project.yuklet.entities.Favorite;
import com.project.yuklet.entities.FavoriteType;
import com.project.yuklet.services.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@CrossOrigin(origins = "*", maxAge = 3600)
public class FavoriteController {
    
    @Autowired
    private FavoriteService favoriteService;
    
    @PostMapping
    public ResponseEntity<ApiResponse<Favorite>> addToFavorites(@RequestParam Long entityId,
                                                               @RequestParam FavoriteType entityType) {
        Favorite favorite = favoriteService.addToFavorites(entityId, entityType);
        return ResponseEntity.ok(ApiResponse.success("Added to favorites successfully", favorite));
    }
    
    @DeleteMapping
    public ResponseEntity<ApiResponse<String>> removeFromFavorites(@RequestParam Long entityId,
                                                                  @RequestParam FavoriteType entityType) {
        favoriteService.removeFromFavorites(entityId, entityType);
        return ResponseEntity.ok(ApiResponse.success("Removed from favorites successfully", null));
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<Favorite>>> getUserFavorites() {
        List<Favorite> favorites = favoriteService.getUserFavorites();
        return ResponseEntity.ok(ApiResponse.success(favorites));
    }
    
    @GetMapping("/type/{entityType}")
    public ResponseEntity<ApiResponse<List<Favorite>>> getUserFavoritesByType(@PathVariable FavoriteType entityType) {
        List<Favorite> favorites = favoriteService.getUserFavoritesByType(entityType);
        return ResponseEntity.ok(ApiResponse.success(favorites));
    }
    
    @GetMapping("/check")
    public ResponseEntity<ApiResponse<Boolean>> isFavorite(@RequestParam Long entityId,
                                                          @RequestParam FavoriteType entityType) {
        boolean isFavorite = favoriteService.isFavorite(entityId, entityType);
        return ResponseEntity.ok(ApiResponse.success(isFavorite));
    }
}
