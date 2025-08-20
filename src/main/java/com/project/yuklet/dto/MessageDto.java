package com.project.yuklet.dto;

import com.project.yuklet.entities.MessageType;

import java.time.LocalDateTime;

public class MessageDto {

    private Long id;
    private Long conversationId;
    private Long senderId;
    private String senderFirstName;
    private String senderLastName;
    private String senderEmail;
    private String content;
    private MessageType messageType;
    private Boolean isRead;
    private LocalDateTime sentDate;

    public MessageDto() {}

    public MessageDto(Long id,
                      Long conversationId,
                      Long senderId,
                      String senderFirstName,
                      String senderLastName,
                      String senderEmail,
                      String content,
                      MessageType messageType,
                      Boolean isRead,
                      LocalDateTime sentDate) {
        this.id = id;
        this.conversationId = conversationId;
        this.senderId = senderId;
        this.senderFirstName = senderFirstName;
        this.senderLastName = senderLastName;
        this.senderEmail = senderEmail;
        this.content = content;
        this.messageType = messageType;
        this.isRead = isRead;
        this.sentDate = sentDate;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getConversationId() { return conversationId; }
    public void setConversationId(Long conversationId) { this.conversationId = conversationId; }

    public Long getSenderId() { return senderId; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }

    public String getSenderFirstName() { return senderFirstName; }
    public void setSenderFirstName(String senderFirstName) { this.senderFirstName = senderFirstName; }

    public String getSenderLastName() { return senderLastName; }
    public void setSenderLastName(String senderLastName) { this.senderLastName = senderLastName; }

    public String getSenderEmail() { return senderEmail; }
    public void setSenderEmail(String senderEmail) { this.senderEmail = senderEmail; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public MessageType getMessageType() { return messageType; }
    public void setMessageType(MessageType messageType) { this.messageType = messageType; }

    public Boolean getIsRead() { return isRead; }
    public void setIsRead(Boolean isRead) { this.isRead = isRead; }

    public LocalDateTime getSentDate() { return sentDate; }
    public void setSentDate(LocalDateTime sentDate) { this.sentDate = sentDate; }
}


