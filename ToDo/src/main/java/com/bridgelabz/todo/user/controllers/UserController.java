package com.bridgelabz.todo.user.controllers;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.todo.user.ResponseDTO.CustomResponse;
import com.bridgelabz.todo.user.ResponseDTO.RegisterErrors;
import com.bridgelabz.todo.user.model.DTO;
import com.bridgelabz.todo.user.model.User;
import com.bridgelabz.todo.user.service.UserService;
import com.bridgelabz.todo.user.validation.UserValidation;

@RestController
@RequestMapping("/user/")
public class UserController {
	private final org.apache.log4j.Logger LOGGER = LogManager.getLogger(UserController.class);
	private static CustomResponse   response = new CustomResponse ();
	private static RegisterErrors errorRes = new RegisterErrors();
	@Autowired 
	private UserService userService;
	@Autowired 
	private UserValidation validator;
	
	@RequestMapping(value="login",method =RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_VALUE)
	
	public ResponseEntity<?> loginUser(@RequestBody DTO DTOuser, HttpServletResponse res) {
		String token=userService.userLogin(DTOuser);
		if(token!=null) {
			res.setHeader("Authorization",token);
			response.setMessage("User successfully login");
			response.setStatusCode(200);
			return new ResponseEntity<CustomResponse>(response,HttpStatus.OK);
		}
		response.setMessage("User login failed");
		response.setStatusCode(400);
		return new ResponseEntity<CustomResponse>(response,HttpStatus.OK);
	}
	@RequestMapping(value="register",method=RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> registrationUser( HttpServletRequest req,@RequestBody DTO DTOuser, BindingResult BindResult) {
		validator.validate(DTOuser, BindResult);
		List<FieldError> error=BindResult.getFieldErrors();
		if(BindResult.hasErrors()) {
			response.setMessage("Enter field properly");
			response.setStatusCode(400);
			return new ResponseEntity<CustomResponse>(response, HttpStatus.CONFLICT);
		}
		try {
			String url = req.getRequestURL().toString();
			
			String requestUrl = url.substring(0,url.lastIndexOf("/"))+"/registerConfirmation/";
			
			if(userService.userRegistration(DTOuser,requestUrl)) {
				response.setMessage("user register success");
				response.setStatusCode(200);
				return new ResponseEntity<CustomResponse>(response, HttpStatus.OK);
			}else {
				errorRes.setMessage("Enter field properly");
				errorRes.setErrors(error);
				return new ResponseEntity<CustomResponse>(errorRes, HttpStatus.CONFLICT);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Server error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@RequestMapping(value="sendEmail",method=RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> validateEmail(HttpServletRequest req , HttpServletResponse res , @RequestBody DTO DTOuser) {
		String url = req.getRequestURL().toString();
		String requestUrl = url.substring(0,url.lastIndexOf("/"))+"user/resetPassword";
		if(userService.sendEmail(DTOuser.getUserEmail(), requestUrl)) {
			response.setMessage("Mail send successfully");
			response.setStatusCode(200);
			return  new ResponseEntity<CustomResponse>(response,HttpStatus.CREATED);
		}
		response.setMessage("Mail not send");
		response.setStatusCode(409);
		return new ResponseEntity<CustomResponse>(response, HttpStatus.CONFLICT);
	}
	@RequestMapping(value="resetPassword/{randomUUID}" , method = RequestMethod.POST) // take new password from user 
	public ResponseEntity<?> resetPassword(@PathVariable("randomUUID")String userUUID,@RequestBody DTO DTOuser){
		String email=userService.getEmailByUUID(userUUID);
		//DTOuser.getPassWord(); validate this password ex: not less than 5 char
		if(email!=null) {
			DTOuser.setUserEmail(email);
			if(userService.resetPassword(DTOuser)) {
				response.setMessage("password reset successfully");
				response.setStatusCode(200);
				return new ResponseEntity<CustomResponse>(response,HttpStatus.OK);
			}
		}	
		response.setMessage("password not reset try again");
		response.setStatusCode(409);
		return new ResponseEntity<CustomResponse>(response,HttpStatus.CONFLICT);
	}
	@RequestMapping(value="registerConfirmation/{randomUUID}" , method = RequestMethod.POST)
	public ResponseEntity<?> registerConfirmation(@PathVariable("randomUUID")String userUUID) {
		if(userService.userActivation(userUUID)) {
			response.setMessage("user activation success");
			response.setStatusCode(200);
			return new ResponseEntity<CustomResponse>(response,HttpStatus.OK); 
		}
		response.setMessage("user activation fail");
		response.setStatusCode(409);
		return new ResponseEntity<CustomResponse>(response,HttpStatus.CONFLICT); 
	}
	
	@RequestMapping(value="getUser" , method = RequestMethod.GET)
	public ResponseEntity<?> getLogedUser(@RequestAttribute(name="userId") int userId) {
		User user =userService.fetchUserByUserId(userId); 
			if(user!=null) {
				return new ResponseEntity<User>(user,HttpStatus.OK);
			}
		response.setMessage("user not return");
		response.setStatusCode(409);
		return new ResponseEntity<CustomResponse>(response,HttpStatus.CONFLICT); 
	}
	
}