package com.project.yuklet.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "favorites")
public class Favorite {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id")
    private Long userId;
    
    @Column(name = "entity_id")
    private Long entityId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "entity_type")
    private FavoriteType entityType;
    
    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();
    
    public Favorite() {}
    
    public Favorite(Long userId, Long entityId, FavoriteType entityType) {
        this.userId = userId;
        this.entityId = entityId;
        this.entityType = entityType;
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
    
    public Long getEntityId() {
        return entityId;
    }
    
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }
    
    public FavoriteType getEntityType() {
        return entityType;
    }
    
    public void setEntityType(FavoriteType entityType) {
        this.entityType = entityType;
    }
    
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
