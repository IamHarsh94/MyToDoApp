package com.fundoonotes.userservice;


public interface IuserDao 
{
	boolean save(UserModel user);
	
	UserModel getUserByEmailId(String UserEmail);
	
	boolean updateUUID(String userName, String randomUUID);
	
	String fetchEmailByUUID(String userUUID);
	
	boolean updatePassword(String passWord, String email);
	
	UserModel getUserByUUID(String userUUID);
	
	void activateUser(UserModel user);
	
	UserModel getUserById(int userId);

   void removeCollaborator(int noteId, int id);

}
