package com.bridgelabz.todo.user.ResponseDTO;

public class CustomResponse {
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
