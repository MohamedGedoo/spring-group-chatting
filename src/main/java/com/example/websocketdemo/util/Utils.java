package com.example.websocketdemo.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Utils {

	public static StringBuffer chatContent = new StringBuffer();

	// Use a Set cause all users names should be unique
	public static Set<String> chatUsers = new HashSet<String>();

	public static Map<String, Integer> wordsFrequency = new HashMap<String, Integer>();

	@Value("${resource.files.storage}")
	private String filesStaorage;
	@Value("${resource.encryptionKey}")
	private String encryptionKey;
	
	

	public String getEncryptionKey() {
		return encryptionKey;
	}

	public void setEncryptionKey(String encryptionKey) {
		this.encryptionKey = encryptionKey;
	}

	public void setFilesStaorage(String filesStaorage) {
		this.filesStaorage = filesStaorage;
	}

	public String getFilesStaorage() {
		return filesStaorage;
	}
}
