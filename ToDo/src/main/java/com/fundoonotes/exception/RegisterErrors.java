package com.fundoonotes.exception;

import java.util.List;

import org.springframework.validation.FieldError;

import com.fundoonotes.userservice.CustomResponseDTO;

public class RegisterErrors extends CustomResponseDTO{
	List<FieldError> errors;
	public List<FieldError> getErrors() {
		return errors;
	}
	public void setErrors(List<FieldError> errors) {
		this.errors = errors;
	}
}
