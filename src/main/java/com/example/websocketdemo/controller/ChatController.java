package com.example.websocketdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.example.websocketdemo.exceptions.ApplicationException;
import com.example.websocketdemo.exceptions.StatusResponse;
import com.example.websocketdemo.model.ChatInfo;
import com.example.websocketdemo.service.FileOperationService;
import com.example.websocketdemo.util.Utils;

@Controller
public class ChatController {

	@Autowired
	private FileOperationService fileOperationService;

	@MessageMapping("/chat.sendMessage")
	@SendTo("/topic/public")
	public ChatInfo sendMessage(@Payload ChatInfo chatMessage) {
		fileOperationService.endingChat(chatMessage);
		return chatMessage;
	}

	@MessageMapping("/chat.addUser")
	@SendTo("/topic/public")
	public ResponseEntity<?> addUser(@Payload ChatInfo chatMessage, SimpMessageHeaderAccessor headerAccessor) {

		try {
			fileOperationService.authenticateUser(chatMessage);

			// Add username in web socket session
			headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
			Utils.chatUsers.add(chatMessage.getSender());
			return ResponseEntity.ok(chatMessage);
		} catch (ApplicationException e) {
			return new ResponseEntity<StatusResponse>(e.getStatus(), HttpStatus.BAD_REQUEST);
		} catch (Exception ex) {
			return new ResponseEntity<StatusResponse>(
					new StatusResponse("500", "INTERNAL_SERVER_ERROR", ex.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
}