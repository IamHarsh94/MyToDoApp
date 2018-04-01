package com.fundoonotes.userservice;

public interface IUserService {
	
	boolean userRegistration(UserDTO DTOuser,String url) throws Exception;
	String userLogin(UserDTO DTOuser);
	boolean sendEmail(String userEmail, String requestUrl);
	String getEmailByUUID(String userUUID);
	boolean userActivation(String userUUID);
	boolean resetPassword(UserDTO dTOuser);
	UserModel fetchUserByUserId(int userId);
}