package com.bridgelabz.todo.user.Dao;

import com.bridgelabz.todo.user.model.User;

public interface userDao {
	boolean save(User user);
	User getUserByEmailId(String UserEmail);
	boolean updateUUID(String userName, String randomUUID);
	String fetchEmailByUUID(String userUUID);
	boolean updatePassword(String passWord, String email);
	User getUserByUUID(String userUUID);
	boolean activateUser(User user);
	User getUserById(int userId);
}
