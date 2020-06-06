
package com.example.websocketdemo.exceptions;

import java.io.Serializable;

public class StatusResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7725423465837002562L;

	private String code;
	private String key;
	private String message;

	public StatusResponse(String code, String key) {
		super();
		this.code = code;
		this.key = key;

	}

	public StatusResponse(String code, String key, String message) {
		super();
		this.code = code;
		this.key = key;
		this.message = message;

	}

	public StatusResponse() {
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param message the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

}
