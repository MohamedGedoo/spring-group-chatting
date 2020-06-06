package com.example.websocketdemo.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.example.websocketdemo.config.WebSocketEventListener;
import com.example.websocketdemo.exceptions.ApplicationException;
import com.example.websocketdemo.exceptions.StatusResponse;
import com.example.websocketdemo.model.ChatInfo;
import com.example.websocketdemo.util.Utils;

/**
 * Service to implement all file operation
 * 
 * 
 * @author mkarim@ntgclarity.com
 * 
 */

@Service
public class FileOperationService {

	@Autowired
	Utils utils;

	private final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

	/**
	 * Authenticate user credentials from authenticatation.txt file
	 *
	 * @param chatMessage A ChatInfo
	 * @exception IOException          if file not found
	 * @exception ApplicationException If user not found
	 * 
	 */
	public void authenticateUser(ChatInfo chatMessage) {

		File file;
		String username = "";
		String password = "";
		String content = "";
		boolean isFound = false;
		try {
			file = ResourceUtils.getFile("classpath:config/authenticatation.txt");

			// Read File Content
			content = new String(Files.readAllBytes(file.toPath()));
		} catch (IOException e) {
			logger.info("Check authenticatation.txt " + e.getMessage());
			e.printStackTrace();
		}
		String[] users = content.trim().split("\\r?\\n");

		for (int i = 0; i < users.length; i++) {
			username = users[i].substring(0, users[i].indexOf(' '));
			password = users[i].substring(users[i].indexOf(' ') + 1, users[i].length());

			if (chatMessage.getSender().equals(username)
					&& encryptPassword(chatMessage.getPassword()).equals(password)) {
				logger.info("User credentials founded in file");
				isFound = true;
				return;
			}

		}
		if (!isFound) {
			logger.info("User credentials not founded in file");
			throw new ApplicationException(
					new StatusResponse("400", "NO_USER_FOUND", "Username or Password is Not Valid "));
		}

	}

	/**
	 * Ending Chat when any user typing bye bye Dump Conversations into file for
	 * each user Dump Conversation Statistics into (Accumulated Statistics file.txt)
	 * Update the chatContent StringBuffer with the new chat message
	 *
	 * @param chatMessage A ChatInfo
	 */
	public void endingChat(ChatInfo chatMessage) {
		Utils.chatContent.append(chatMessage.getSender() + " --> " + chatMessage.getContent());
		Utils.chatContent.append(System.getProperty("line.separator"));
		accumulateStatistics(chatMessage.getContent());
		System.out.println(Utils.chatContent);

		if (chatMessage.getContent().toLowerCase().contains("bye bye")) {

			dumpConversations();
			dumpConversationStatistics();
			Utils.chatContent = new StringBuffer();
			Utils.wordsFrequency = new HashMap<String, Integer>();
		}
	}

	/**
	 * Dump Conversations into file with username for each user
	 *
	 */
	public void dumpConversations() {
		List<String> users = new ArrayList<String>(Utils.chatUsers);

		for (int i = 0; i < users.size(); i++) {
			String fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm' " + users.get(i) + ".txt'").format(new Date());

			try {
				BufferedWriter writer = new BufferedWriter(
						new FileWriter(new File((utils.getFilesStaorage() + fileName))));

				writer.write(Utils.chatContent.toString());
				writer.flush();
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	/**
	 * Dump Conversation Statistics into (Accumulated Statistics file.txt)
	 *
	 */
	public void dumpConversationStatistics() {

		try {
			BufferedWriter writer;
			StringBuilder frequencies = new StringBuilder();
			List<String> frequency = new ArrayList<String>(Utils.wordsFrequency.keySet());

			String fileName = utils.getFilesStaorage() + "Accumulated Statistics file.txt";

			File file = new File(fileName);
			for (String key : frequency) {
				frequencies.append(System.getProperty("line.separator"));
				frequencies.append(key + " --> " + Utils.wordsFrequency.get(key));
			}

			/*
			 * Path pathOfLog = Paths.get(fileName); Charset charSetOfLog =
			 * Charset.forName("US-ASCII"); BufferedWriter bwOfLog =
			 * Files.newBufferedWriter(pathOfLog, charSetOfLog);
			 * 
			 * bwOfLog.newLine(); // Add new line bwOfLog.write(new
			 * SimpleDateFormat("yyyy-MM-dd HH:mm' Conversation '").format(new Date()));
			 * bwOfLog.newLine(); bwOfLog.write( frequencies.toString()); bwOfLog.flush();
			 * bwOfLog.close();
			 */

			if (file.exists()) {
				// Set true for append mode
				writer = new BufferedWriter(new FileWriter(fileName, true));
			} else {
				writer = new BufferedWriter(new FileWriter(new File(fileName)));
			}
			writer.newLine();
			writer.write(new SimpleDateFormat("yyyy-MM-dd HH:mm' Conversation '").format(new Date()));
			writer.newLine();
			writer.write(frequencies.toString());
			writer.flush();
			writer.close();

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/**
	 * Update wordsFrequency map with every word in each message to make Statistics
	 * for each word in the chat
	 *
	 * @param message A String
	 */
	public void accumulateStatistics(String message) {

		// Split on Zero or more whitespaces (\\s*)
		// or New Line, or comma, or whitespace (\\r?\\n|,|\\s) Zero
		// or more whitespaces (\\s*)
		String[] words = message.trim().toLowerCase().split("\\s*(\\r?\\n|,|\\s)\\s*");

		for (String word : words) {

			if (Utils.wordsFrequency.containsKey(word)) {
				Utils.wordsFrequency.put(word, Utils.wordsFrequency.get(word) + 1);
			} else {
				Utils.wordsFrequency.put(word, 1);
			}

		}
	}

	/**
	 * Use hashed passwords instead of storing plain text passwords. Use SHA-256 for
	 * hashing
	 *
	 * @param password A String
	 */
	public String encryptPassword(String pass) {
		try {

			String generatedPassword = null;
			try {
				MessageDigest md = MessageDigest.getInstance("SHA-256");
				md.update(utils.getEncryptionKey().getBytes(StandardCharsets.UTF_8));
				byte[] bytes = md.digest(pass.getBytes(StandardCharsets.UTF_8));

				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < bytes.length; i++) {
					sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
				}
				generatedPassword = sb.toString();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			return generatedPassword;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
