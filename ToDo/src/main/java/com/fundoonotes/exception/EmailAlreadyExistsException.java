package com.fundoonotes.exception;

import com.fundoonotes.userservice.CustomResponseDTO;

public class EmailAlreadyExistsException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public EmailAlreadyExistsException () {
		super("Email already registered");
	}

	public CustomResponseDTO getResponse() {
		CustomResponseDTO response = new CustomResponseDTO();
		response.setMessage(this.getMessage());
		response.setStatusCode(-1);
		return response;
	}
}
