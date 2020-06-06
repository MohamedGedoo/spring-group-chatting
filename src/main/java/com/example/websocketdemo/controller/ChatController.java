package com.example.websocketdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.example.websocketdemo.model.ChatInfo;
import com.example.websocketdemo.service.FileOperationService;
import com.example.websocketdemo.util.Utils;

/**
 * This controller responsible for receiving messages from one client and then
 * broadcasting it to others.
 */
@Controller
public class ChatController {

	@Autowired
	private FileOperationService fileOperationService;

	
	/**
	 * get message from one user and broadcasting it to others
	 * */
	@MessageMapping("/chat.sendMessage")
	@SendTo("/topic/public")
	public ChatInfo sendMessage(@Payload ChatInfo chatMessage) {
		fileOperationService.endingChat(chatMessage);
		return chatMessage;
	}

	/**
	 * Add new user to the chat group
	 * */
	@MessageMapping("/chat.addUser")
	@SendTo("/topic/public")
	public ResponseEntity<?> addUser(@Payload ChatInfo chatMessage, SimpMessageHeaderAccessor headerAccessor) {

		// Add username in web socket session
		headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
		Utils.chatUsers.add(chatMessage.getSender());
		return ResponseEntity.ok(chatMessage);

	}
}
