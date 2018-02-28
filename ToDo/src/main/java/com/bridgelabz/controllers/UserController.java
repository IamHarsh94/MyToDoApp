package com.bridgelabz.controllers;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.http.HeadersBeanDefinitionParser;
import org.springframework.security.web.header.Header;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.bridgelabz.ResponseDTO.CustomResponse;
import com.bridgelabz.ResponseDTO.RegisterErrors;
import com.bridgelabz.model.DTO;
import com.bridgelabz.service.UserService;
import com.bridgelabz.validation.UserValidation;

@RestController
public class UserController {

	private final org.apache.log4j.Logger LOGGER = LogManager.getLogger(UserController.class);
	private static CustomResponse customRes = new CustomResponse();
	@Autowired 
	private UserService userService;

	@Autowired 
	private UserValidation validator;

	@RequestMapping(value="login",method =RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> loginUser(@RequestBody DTO DTOuser, HttpServletResponse res) {
		CustomResponse customRes = new CustomResponse();
		String token=userService.userLogin(DTOuser);
			if(token!=null) {
				/*HttpHeaders header = new HttpHeaders();
				header.add("Authorization", token);*/
				res.setHeader("Authorization",token); //put jwt in Header
				customRes.setMessage("User successfully login");
				customRes.setStatusCode(200);
				return new ResponseEntity<CustomResponse>(customRes,HttpStatus.OK);

			}
					
		customRes.setMessage("User login failed");
		customRes.setStatusCode(409);
		return new ResponseEntity<CustomResponse>(customRes,HttpStatus.NO_CONTENT);
	}
	@RequestMapping(value="register",method=RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> registrationUser( HttpServletRequest req,@RequestBody DTO DTOuser, BindingResult BindResult) {
		validator.validate(DTOuser, BindResult);
		List<FieldError> error=BindResult.getFieldErrors();
		RegisterErrors response = new RegisterErrors();
		if(BindResult.hasErrors()) {
			response.setMessage("Enter field properly");
			response.setErrors(error);
			response.setStatusCode(201);			
			return new ResponseEntity<RegisterErrors>(response, HttpStatus.CONFLICT);
		}
		try {
			String url = req.getRequestURL().toString();
			String requestUrl = url.substring(0,url.lastIndexOf("/"))+"/registerConfirmation/";
			if(userService.userRegistration(DTOuser,requestUrl)) {
				customRes.setMessage("user register success");
				customRes.setStatusCode(100);
				return new ResponseEntity<CustomResponse>(customRes, HttpStatus.CREATED);
			}else {
				customRes.setMessage("registration failed");
				customRes.setStatusCode(409);
				return new ResponseEntity<CustomResponse>(customRes, HttpStatus.CONFLICT);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Server error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@RequestMapping(value="sendEmail",method=RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RegisterErrors> validateEmail(HttpServletRequest req , HttpServletResponse res , @RequestBody DTO DTOuser) {
		RegisterErrors response = new RegisterErrors();
		String url = req.getRequestURL().toString();
		String requestUrl = url.substring(0,url.lastIndexOf("/"))+"/resetPassword/";
		if(userService.sendEmail(DTOuser.getUserEmail(), requestUrl)) {
			response.setMessage("Mail send successfully");
			response.setStatusCode(200);
			return  new ResponseEntity<RegisterErrors>(response,HttpStatus.CREATED);
		}
		response.setMessage("Mail not send");
		response.setStatusCode(409);
		return new ResponseEntity<RegisterErrors>(response, HttpStatus.CONFLICT);
	}
	@RequestMapping(value="/resetPassword/{randomUUID}" , method = RequestMethod.POST) // take new password from user 
	public ResponseEntity<RegisterErrors> resetPassword(@PathVariable("randomUUID")String userUUID,@RequestBody DTO DTOuser){
		RegisterErrors response = new RegisterErrors();
		String email=userService.getEmailByUUID(userUUID); // if the email is null then what??
		//DTOuser.getPassWord(); validate this password ex: not less than 5 char
		DTOuser.setUserEmail(email);
		if(userService.resetPassword(DTOuser)) {
			response.setMessage("password reset successfully");
			response.setStatusCode(200);
			return new ResponseEntity<RegisterErrors>(response,HttpStatus.CREATED);
		}
		response.setMessage("password not reset try again");
		response.setStatusCode(409);
		return new ResponseEntity<>(response,HttpStatus.CONFLICT);
	}
	@RequestMapping(value="/registerConfirmation/{randomUUID}" , method = RequestMethod.POST)
	public ResponseEntity<RegisterErrors> registerConfirmation(@PathVariable("randomUUID")String userUUID) {
		RegisterErrors response = new RegisterErrors();
		if(userService.userActivation(userUUID)) {
			response.setMessage("user activation success");
			response.setStatusCode(200);
			return new ResponseEntity<RegisterErrors>(response,HttpStatus.CREATED); 
		}
		response.setMessage("user activation fail");
		response.setStatusCode(409);
		return new ResponseEntity<RegisterErrors>(response,HttpStatus.CONFLICT); 
	}
}