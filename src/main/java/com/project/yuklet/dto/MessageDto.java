package com.project.yuklet.dto;

import com.project.yuklet.entities.MessageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class MessageDto {
    
    private Long id;
    
    @NotNull
    private Long conversationId;
    
    private Long senderId;
    
    @NotBlank
    private String content;
    
    private MessageType messageType = MessageType.TEXT;
    
    private Boolean isRead = false;
    private LocalDateTime sentDate;
    
    public MessageDto() {}
    
    public MessageDto(Long conversationId, String content) {
        this.conversationId = conversationId;
        this.content = content;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getConversationId() {
        return conversationId;
    }
    
    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }
    
    public Long getSenderId() {
        return senderId;
    }
    
    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public MessageType getMessageType() {
        return messageType;
    }
    
    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }
    
    public Boolean getIsRead() {
        return isRead;
    }
    
    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }
    
    public LocalDateTime getSentDate() {
        return sentDate;
    }
    
    public void setSentDate(LocalDateTime sentDate) {
        this.sentDate = sentDate;
    }
}
