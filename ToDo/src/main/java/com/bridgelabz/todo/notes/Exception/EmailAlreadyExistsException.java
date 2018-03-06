package com.bridgelabz.todo.notes.Exception;

import com.bridgelabz.todo.user.ResponseDTO.CustomResponse;

public class EmailAlreadyExistsException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public EmailAlreadyExistsException () {
		super("Email already registered");
	}

	public CustomResponse getResponse() {
		CustomResponse response = new CustomResponse();
		response.setMessage(this.getMessage());
		response.setStatusCode(-1);
		return response;
	}
}
