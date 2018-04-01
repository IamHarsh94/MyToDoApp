package com.fundoonotes.userservice;

public class CustomResponseDTO {
	private String message;
	private int status;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getStatusCode() {
		return status;
	}
	public void setStatusCode(int statusCode) {
		this.status = statusCode;
	}
}
