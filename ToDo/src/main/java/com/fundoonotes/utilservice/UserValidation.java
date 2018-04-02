package com.fundoonotes.utilservice;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.LogManager;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.fundoonotes.userservice.UserController;
import com.fundoonotes.userservice.UserDTO;
import com.fundoonotes.userservice.UserModel;

@Component
public class UserValidation implements Validator {
   
   private final org.apache.log4j.Logger LOGGER = LogManager.getLogger(UserController.class);
	
   private Pattern pattern;
	
   private Matcher matcher;
	
   private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+"[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
   String STRING_PATTERN = "[a-zA-Z ]+";
	String MOBILE_PATTERN = "[0-9]{10}";
	String ADDRESS_PATTERN="^[#0-9a-zA-Z ]+$";
	
	@Override
	public boolean supports(Class<?> clazz) {
		return UserModel.class.equals(clazz);
	}
	@Override
	public void validate(Object obj, Errors error) {
		
	   UserDTO DTOuser = (UserDTO) obj;
		
		/**
		 * Fullname validation (should not blank or white space)
		 * 
		 * */
		ValidationUtils.rejectIfEmptyOrWhitespace(error, "fullName", "fullname is require");
		
		/**
       * Fullname validation with given compile pattern
       * 
       * */
		if (DTOuser.getFullName()!= null && !DTOuser.getFullName().isEmpty())
		{
			
			pattern = Pattern.compile(STRING_PATTERN);
			
			matcher = pattern.matcher(DTOuser.getFullName());
			
			if (!matcher.matches())
			{
			   LOGGER.info("Enter the fullname correct..");
				
			   error.rejectValue("fullName", "fullName", "enter a valid fullname");
			}
		}
		
		/**
       *  email id validation (should not blank or white space)
       * 
       * */
		ValidationUtils.rejectIfEmptyOrWhitespace(error, "userEmail", "email is require");
		
		/**
       * email validation with given compile pattern
       * 
       * */
		if ( DTOuser.getUserEmail()!= null && !DTOuser.getUserEmail().isEmpty()) 
		{
			
			pattern = Pattern.compile(EMAIL_PATTERN);
			
			matcher = pattern.matcher(DTOuser.getUserEmail());
			
			if (!matcher.matches())
			{
			   LOGGER.info("Enter the email id correct..");
				
			   error.rejectValue("userEmail", "userEmail", "enter correct email address");
			}
		}
		
		/**
       *password validation (should not blank or white space)
       * 
       * */
		ValidationUtils.rejectIfEmptyOrWhitespace(error, "passWord", "passWord is require");
		

      /**
       * password validation with given compile pattern
       * 
       * */
		if (!DTOuser.getPassWord().equals(DTOuser.getConfirmpassword()))
		{
		   LOGGER.info("Enter the password correct..");
			error.rejectValue("passWord", "passWord", "passWord does not match");
		}
		
		/**
       * mobile validation (should not blank or white space)
       * 
       * */
		ValidationUtils.rejectIfEmptyOrWhitespace(error, "mobileNum", "Mobile Number is required");
		
		/**
       * mobile validation with given compile pattern
       * 
       * */
		if ( DTOuser.getMobileNum()!= null && !DTOuser.getMobileNum().isEmpty())
		{	
			
			pattern = Pattern.compile(MOBILE_PATTERN);
			
			matcher = pattern.matcher(DTOuser.getMobileNum());
			
			if (!matcher.matches())
			{
			   LOGGER.info("Enter the mobile number correct..");
				error.rejectValue("mobileNum","mobileNum", "Enter a correct Mobile number");
			}
		}

      /**
       * address validation (should not blank or white space)
       * 
       * */
		ValidationUtils.rejectIfEmptyOrWhitespace(error, "address", "address is required");
		
		pattern  = Pattern.compile(ADDRESS_PATTERN);
		
		matcher=pattern.matcher(DTOuser.getAddress());
		
		if(!matcher.matches()) 
		{
		   LOGGER.info("Enter the address properly correct..");
			error.rejectValue("address", "address", "enter a correct address");
		}	
	}
	
	/**
	 * separate Email vaidation for forgot password
	 * */
	public String emailValidate(String userEmail)
	{
		String msg =null;
		
		if ( userEmail!= null && !userEmail.isEmpty()) 
		{	
			pattern = Pattern.compile(EMAIL_PATTERN);
			
			matcher = pattern.matcher(userEmail);
			
			if (!matcher.matches())
			{
			   LOGGER.info("Enter the email properly..");
				msg="Enter proper email id";
				return msg;
			}
			return msg;
		}
		msg="Enter email id";
		return msg;
	}
}
