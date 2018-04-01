package com.fundoonotes.exception;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fundoonotes.userservice.CustomResponseDTO;

public class GlobalExceptionHandler {

	private static final Logger logger = Logger.getLogger(GlobalExceptionHandler.class);
	
	@ExceptionHandler(value = EmailAlreadyExistsException.class)
	public ResponseEntity<CustomResponseDTO> emailAlreadyExistsExceptionHandler(EmailAlreadyExistsException e) {
		CustomResponseDTO response = e.getResponse();
		logger.error(e.getMessage());
		return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
	}
	
	@ExceptionHandler(value = DatabaseException.class)
	public ResponseEntity<CustomResponseDTO> databaseExceptionHandler(DatabaseException e) {
		return new ResponseEntity<>(e.getResponse(), HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(value = UnAuthorizedAcess.class)
	public ResponseEntity<CustomResponseDTO> UnAuthorizedAcessExceptionHandler(UnAuthorizedAcess e) {
		return new ResponseEntity<>(e.getResponse(), HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(value = RuntimeException.class)
	public ResponseEntity<CustomResponseDTO> runtimeHandler(RuntimeException e) {
		CustomResponseDTO response = new CustomResponseDTO();
		response.setMessage("Something went wrong");
		response.setStatusCode(-1);
		return new ResponseEntity<CustomResponseDTO>(response, HttpStatus.CONFLICT);
	}

}
