package com.project.yuklet.controller;

import com.project.yuklet.dto.ChatMessage;
import com.project.yuklet.services.ChatService;
import com.project.yuklet.entities.User;
import com.project.yuklet.entities.UserProfile;
import com.project.yuklet.reporsitory.UserRepository;
import com.project.yuklet.reporsitory.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.Instant;
import java.security.Principal;

@Controller
public class ChatController {

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@Autowired
	private ChatService chatService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserProfileRepository userProfileRepository;

	@MessageMapping("/send")
	public void handleBroadcast(@Payload ChatMessage message, Principal principal) {
		if (message.getTimestamp() == null) {
			message.setTimestamp(Instant.now());
		}
		if (principal != null && (message.getSender() == null || message.getSender().isBlank())) {
			message.setSender(resolveDisplayName(principal));
		}
		chatService.store(message);
		messagingTemplate.convertAndSend("/topic/messages", message);
	}

	@MessageMapping("/sendTo/{topic}")
	public void handleToTopic(@DestinationVariable String topic, @Payload ChatMessage message, Principal principal) {
		if (message.getTimestamp() == null) {
			message.setTimestamp(Instant.now());
		}
		if (principal != null && (message.getSender() == null || message.getSender().isBlank())) {
			message.setSender(resolveDisplayName(principal));
		}
		messagingTemplate.convertAndSend("/topic/" + topic, message);
	}

	private String resolveDisplayName(Principal principal) {
		String email = principal.getName();
		User user = userRepository.findByEmail(email).orElse(null);
		if (user == null) {
			return email;
		}
		UserProfile profile = userProfileRepository.findByUserId(user.getId()).orElse(null);
		if (profile == null) {
			return email;
		}
		String first = profile.getFirstName() != null ? profile.getFirstName() : "";
		String last = profile.getLastName() != null ? profile.getLastName() : "";
		String full = (first + " " + last).trim();
		return full.isEmpty() ? email : full;
	}
}


