package com.bridgelabz.todo.user.service;

import com.bridgelabz.todo.user.model.DTO;
import com.bridgelabz.todo.user.model.User;

public interface UserService {
	
	boolean userRegistration(DTO DTOuser,String url) throws Exception;
	String userLogin(DTO DTOuser);
	boolean sendEmail(String userEmail, String requestUrl);
	String getEmailByUUID(String userUUID);
	boolean userActivation(String userUUID);
	boolean resetPassword(DTO dTOuser);
	User fetchUserByUserId(int userId);
}