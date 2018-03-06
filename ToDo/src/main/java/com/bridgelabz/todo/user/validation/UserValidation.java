package com.bridgelabz.todo.user.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.bridgelabz.todo.user.model.DTO;
import com.bridgelabz.todo.user.model.User;

@Component
public class UserValidation implements Validator {
	private Pattern pattern;
	private Matcher matcher;
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+"[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	String STRING_PATTERN = "[a-zA-Z ]+";
	String MOBILE_PATTERN = "[0-9]{10}";
	String ADDRESS_PATTERN="^[#0-9a-zA-Z ]+$";
	@Override
	public boolean supports(Class<?> clazz) {
		return User.class.equals(clazz);
	}
	@Override
	public void validate(Object obj, Errors error) {
		DTO DTOuser = (DTO) obj;
		ValidationUtils.rejectIfEmptyOrWhitespace(error, "fullName", "fullname is require");
		if (DTOuser.getFullName()!= null && !DTOuser.getFullName().isEmpty())
		{
			System.out.println("in fullname");	
			pattern = Pattern.compile(STRING_PATTERN);
			matcher = pattern.matcher(DTOuser.getFullName());
			if (!matcher.matches()) {
				error.rejectValue("fullName", "fullName", "enter a valid fullname");
			}
		}
		ValidationUtils.rejectIfEmptyOrWhitespace(error, "userEmail", "email is require");
		if ( DTOuser.getUserEmail()!= null && !DTOuser.getUserEmail().isEmpty()) 
		{
			System.out.println("in usermail");
			pattern = Pattern.compile(EMAIL_PATTERN);
			matcher = pattern.matcher(DTOuser.getUserEmail());
			if (!matcher.matches()) {
				error.rejectValue("userEmail", "userEmail", "enter correct email address");
			}
		}
		ValidationUtils.rejectIfEmptyOrWhitespace(error, "passWord", "passWord is require");
		if (!DTOuser.getPassWord().equals(DTOuser.getConfirmpassword())){
			error.rejectValue("passWord", "passWord", "passWord does not match");
		}
		ValidationUtils.rejectIfEmptyOrWhitespace(error, "mobileNum", "Mobile Number is required");
		if ( DTOuser.getMobileNum()!= null && !DTOuser.getMobileNum().isEmpty())
		{	
			System.out.println("in mobile num");
			pattern = Pattern.compile(MOBILE_PATTERN);
			matcher = pattern.matcher(DTOuser.getMobileNum());
			if (!matcher.matches()) {
				error.rejectValue("mobileNum","mobileNum", "Enter a correct Mobile number");
			}
		}
		ValidationUtils.rejectIfEmptyOrWhitespace(error, "address", "address is required");
		pattern  = Pattern.compile(ADDRESS_PATTERN);
		matcher=pattern.matcher(DTOuser.getAddress());
		if(!matcher.matches()) {
			error.rejectValue("address", "address", "enter a correct address");
		}	
	}
	public String emailValidate(String userEmail) {
		String msg =null;
		if ( userEmail!= null && !userEmail.isEmpty()) 
		{	
			pattern = Pattern.compile(EMAIL_PATTERN);
			matcher = pattern.matcher(userEmail);
			if (!matcher.matches()) {
				msg="Enter proper email id";
				return msg;
			}
			return msg;
		}
		msg="Enter email id";
		return msg;
	}
}
