package com.fundoonotes.exception;

import com.fundoonotes.userservice.CustomResponseDTO;

public class DatabaseException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public DatabaseException () {
		super("database exception:row not updated");
	}
	
	public CustomResponseDTO getResponse() {
		CustomResponseDTO response = new CustomResponseDTO();
		response.setMessage(this.getMessage());
		response.setStatusCode(-1);
		return response;
	}
}
