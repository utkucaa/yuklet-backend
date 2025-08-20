package com.project.yuklet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class ConversationSummaryDto {

    private Long conversationId;
    private Long otherUserId;
    private String otherFirstName;
    private String otherLastName;
    private String companyName;
    private LocalDateTime lastMessageDate;
    private Long unreadCount;

    public ConversationSummaryDto() {}

    public ConversationSummaryDto(Long conversationId,
                                  Long otherUserId,
                                  String otherFirstName,
                                  String otherLastName,
                                  String companyName,
                                  LocalDateTime lastMessageDate,
                                  Long unreadCount) {
        this.conversationId = conversationId;
        this.otherUserId = otherUserId;
        this.otherFirstName = otherFirstName;
        this.otherLastName = otherLastName;
        this.companyName = companyName;
        this.lastMessageDate = lastMessageDate;
        this.unreadCount = unreadCount;
    }

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    public Long getOtherUserId() {
        return otherUserId;
    }

    public void setOtherUserId(Long otherUserId) {
        this.otherUserId = otherUserId;
    }

    public String getOtherFirstName() {
        return otherFirstName;
    }

    public void setOtherFirstName(String otherFirstName) {
        this.otherFirstName = otherFirstName;
    }

    public String getOtherLastName() {
        return otherLastName;
    }

    public void setOtherLastName(String otherLastName) {
        this.otherLastName = otherLastName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public LocalDateTime getLastMessageDate() {
        return lastMessageDate;
    }

    public void setLastMessageDate(LocalDateTime lastMessageDate) {
        this.lastMessageDate = lastMessageDate;
    }

    public Long getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(Long unreadCount) {
        this.unreadCount = unreadCount;
    }

    // Backward compatibility for FE expecting `conversation.id`
    @JsonProperty("id")
    public Long getId() {
        return conversationId;
    }
}


