package com.example.websocketdemo.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptionUtil {

	/**
	 * encrypt_SHA_256_SecurePassword
	 */
	public static String encrypt(String passwordToHash, String encryptionKey) {

		String generatedPassword = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(encryptionKey.getBytes(StandardCharsets.UTF_8));
			byte[] bytes = md.digest(passwordToHash.getBytes(StandardCharsets.UTF_8));
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			generatedPassword = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return generatedPassword;

	}

}