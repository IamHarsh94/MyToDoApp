package com.bridgelabz.service;

import com.bridgelabz.model.DTO;

public interface UserService {
	
	boolean userRegistration(DTO DTOuser,String url) throws Exception;
	String userLogin(DTO DTOuser);
	boolean sendEmail(String userEmail, String requestUrl);
	String getEmailByUUID(String userUUID);
	boolean userActivation(String userUUID);
	boolean resetPassword(DTO dTOuser);
}