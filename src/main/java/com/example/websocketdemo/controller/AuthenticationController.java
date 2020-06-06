package com.example.websocketdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.websocketdemo.exceptions.ApplicationException;
import com.example.websocketdemo.exceptions.StatusResponse;
import com.example.websocketdemo.model.ChatInfo;
import com.example.websocketdemo.service.FileOperationService;

@RestController
@CrossOrigin
public class AuthenticationController {

	@Autowired
	private FileOperationService fileOperationService;

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody ChatInfo chatMessage) {
		try {
			fileOperationService.authenticateUser(chatMessage);
		} catch (ApplicationException e) {
			return new ResponseEntity<StatusResponse>(e.getStatus(), HttpStatus.BAD_REQUEST);
		} catch (Exception ex) {
			return new ResponseEntity<StatusResponse>(
					new StatusResponse("500", "INTERNAL_SERVER_ERROR", ex.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);

		}
		return ResponseEntity.ok().build();
	}
}
