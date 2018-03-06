package com.bridgelabz.todo.notes.Exception;

import com.bridgelabz.todo.user.ResponseDTO.CustomResponse;

public class UnAuthorizedAcess extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public UnAuthorizedAcess () {
		super("UnAuthorizedAcess access exception");
	}

	public CustomResponse getResponse() {
		CustomResponse response = new CustomResponse();
		response.setMessage(this.getMessage());
		response.setStatusCode(-1);
		return response;
	}
}
