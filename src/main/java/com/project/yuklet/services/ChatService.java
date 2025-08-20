package com.project.yuklet.services;

import com.project.yuklet.dto.ChatMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

@Service
public class ChatService {

    private final Deque<ChatMessage> lastMessages = new ArrayDeque<>();
    private static final int MAX_MESSAGES = 100;

    public synchronized void store(ChatMessage message) {
        if (lastMessages.size() >= MAX_MESSAGES) {
            lastMessages.removeFirst();
        }
        lastMessages.addLast(message);
    }

    public synchronized List<ChatMessage> getRecent() {
        return new ArrayList<>(lastMessages);
    }
}


