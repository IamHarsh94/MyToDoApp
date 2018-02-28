package com.bridgelabz.ResponseDTO;

import java.util.List;

import org.springframework.validation.FieldError;

public class RegisterErrors extends CustomResponse{

	List<FieldError> errors;

	public List<FieldError> getErrors() {
		return errors;
	}

	public void setErrors(List<FieldError> errors) {
		this.errors = errors;
	}
	
	
}
