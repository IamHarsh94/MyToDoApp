package com.bridgelabz.todo.exceptions.handler;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.bridgelabz.todo.notes.Exception.DatabaseException;
import com.bridgelabz.todo.notes.Exception.EmailAlreadyExistsException;
import com.bridgelabz.todo.notes.Exception.UnAuthorizedAcess;
import com.bridgelabz.todo.user.ResponseDTO.CustomResponse;

public class GlobalExceptionHandler {

	private static final Logger logger = Logger.getLogger(GlobalExceptionHandler.class);
	
	@ExceptionHandler(value = EmailAlreadyExistsException.class)
	public ResponseEntity<CustomResponse> emailAlreadyExistsExceptionHandler(EmailAlreadyExistsException e) {
		CustomResponse response = e.getResponse();
		logger.error(e.getMessage());
		return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
	}
	
	@ExceptionHandler(value = DatabaseException.class)
	public ResponseEntity<CustomResponse> databaseExceptionHandler(DatabaseException e) {
		return new ResponseEntity<>(e.getResponse(), HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(value = UnAuthorizedAcess.class)
	public ResponseEntity<CustomResponse> UnAuthorizedAcessExceptionHandler(UnAuthorizedAcess e) {
		return new ResponseEntity<>(e.getResponse(), HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(value = RuntimeException.class)
	public ResponseEntity<CustomResponse> runtimeHandler(RuntimeException e) {
		CustomResponse response = new CustomResponse();
		response.setMessage("Something went wrong");
		response.setStatusCode(-1);
		return new ResponseEntity<CustomResponse>(response, HttpStatus.CONFLICT);
	}

}
