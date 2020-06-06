package com.example.websocketdemo.model;

/**
 * Model payload that will be exchanged between the clients and the server.
 * 
 * 
 * @author mkarim@ntgclarity.com
 * 
 */

public class ChatInfo {
	private MessageType type;
	private String content;
	private String sender;
	private String password;

	public enum MessageType {
		CHAT, JOIN, LEAVE
	}

	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	

	
	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
