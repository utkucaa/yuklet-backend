package com.project.yuklet.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "conversations")
public class Conversation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user1_id")
    private Long user1Id;
    
    @Column(name = "user2_id")
    private Long user2Id;
    
    @Column(name = "cargo_request_id")
    private Long cargoRequestId;
    
    @Column(name = "last_message_date")
    private LocalDateTime lastMessageDate;
    
    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();
    
    
    public Conversation() {}
    
    public Conversation(Long user1Id, Long user2Id, Long cargoRequestId) {
        this.user1Id = user1Id;
        this.user2Id = user2Id;
        this.cargoRequestId = cargoRequestId;
        this.lastMessageDate = LocalDateTime.now();
    }
    
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getUser1Id() {
        return user1Id;
    }
    
    public void setUser1Id(Long user1Id) {
        this.user1Id = user1Id;
    }
    
    public Long getUser2Id() {
        return user2Id;
    }
    
    public void setUser2Id(Long user2Id) {
        this.user2Id = user2Id;
    }
    
    public Long getCargoRequestId() {
        return cargoRequestId;
    }
    
    public void setCargoRequestId(Long cargoRequestId) {
        this.cargoRequestId = cargoRequestId;
    }
    
    public LocalDateTime getLastMessageDate() {
        return lastMessageDate;
    }
    
    public void setLastMessageDate(LocalDateTime lastMessageDate) {
        this.lastMessageDate = lastMessageDate;
    }
    
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
