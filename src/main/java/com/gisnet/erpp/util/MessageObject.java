package com.gisnet.erpp.util;

public class MessageObject {
	
	private String message;
	
	public MessageObject() {
		
	}

	public MessageObject(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
}
