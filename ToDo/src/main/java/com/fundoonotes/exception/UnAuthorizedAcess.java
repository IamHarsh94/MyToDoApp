package com.fundoonotes.exception;

import com.fundoonotes.userservice.CustomResponseDTO;

public class UnAuthorizedAcess extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public UnAuthorizedAcess () {
		super("UnAuthorizedAcess access exception");
	}

	public CustomResponseDTO getResponse() {
		CustomResponseDTO response = new CustomResponseDTO();
		response.setMessage(this.getMessage());
		response.setStatusCode(-1);
		return response;
	}
}
