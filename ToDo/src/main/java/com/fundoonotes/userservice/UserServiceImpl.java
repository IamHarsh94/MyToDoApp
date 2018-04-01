package com.fundoonotes.userservice;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fundoonotes.utilservice.MailService;
import com.fundoonotes.utilservice.TokenUtil;
import com.fundoonotes.utilservice.UserValidation;

@Service
public class UserServiceImpl implements IUserService{
	@Autowired 
	private IuserDao userDao;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	private MailService userMail;
	@Autowired
	private TokenUtil userToken;
	@Transactional(rollbackFor = Exception.class)
	public boolean userRegistration(UserDTO DTOuser,String url) throws Exception 
	{
		/**
		 * Setting DTOuser object fields into the user object fields
		 * so only user object canpasswordEncoder communicate with db
		 * 
		 * **/
		UserModel user = new UserModel(DTOuser.getFullName(), DTOuser.getUserEmail(), DTOuser.getPassWord(), DTOuser.getConfirmpassword(), DTOuser.getMobileNum(),
		DTOuser.getAddress());
		
		/**
		 * Get user by user email id from db
		 * 
		 * @param user email id which present in user object 
 		 * @return return the user or null
		 * */
		UserModel user1=userDao.getUserByEmailId(user.getUserEmail());
		
		if(user1 == null)
		{
			/**
			 * Encode the password by passing the encode method of 
			 * passwordEncoder interface
			 * 
			 * @param  user password from user object
			 * @return return the encoded password(in hashcode form) 
			 * */
			String hashCode=passwordEncoder.encode(user.getPassWord());
			
			/**
			 * Setting the incoded password to the user object field
			 * 
			 * */
			user.setPassWord(hashCode);
			
			/**
			 * Create the random UU java.util.UUIDID(which represents a 128-bit value) by using 
			 *  java.util.UUID class
			 * 
			 * */
			String randomUUID = UUID.randomUUID().toString();
			
			/**
			 * Setting the generated UUID(token) in user object field 
			 * */
			user.setUUID(randomUUID);
			
			/**
			 * Save the user object fields in db 
			 * 
			 * @param user object
			 * @return return the true if user save successfully otherwise return false 
			 * */
			boolean isSave=userDao.save(user);
			
			if(isSave) 
			{
				/**
				 * Concatinating the random generated UUID with currrent API URL 
				 *  
				 * */
				String URL=url+randomUUID;
				String to = user.getUserEmail();
				String subject = "Registration confirmation link";
				String message = URL;
				/**
				 * Sending the mail to the registering user  
				 * @param To (from which email id mail want to send)
				 * 		 	message (Sending the complete url in message)  
				 * @return return true if mail sent successully ro controller otherwise return false 
				 * */
				boolean isSent =userMail.sendMail(to, subject, message);
				
				if(isSent) {
					return true;
				}
				return false;
			}
		}
		return false;
	}
	/**
	 * 
	 * 
	 * */
	
	@SuppressWarnings("static-access")
	@Override
	public String userLogin(UserDTO DTOuser){
		
		UserModel user = new UserModel(DTOuser.getUserEmail(), DTOuser.getPassWord());
		
		UserModel userObj =userDao.getUserByEmailId(user.getUserEmail());
		
		if(userObj!=null && userObj.isActive() && BCrypt.checkpw(user.getPassWord(), userObj.getPassWord())) {
			return userToken.generateToken(userObj.getId());
		}
		return  null;
	}
	@Override
	public boolean sendEmail(String userEmail, String requestUrl) {
		UserValidation valid = new UserValidation();
		String msg =valid.emailValidate(userEmail);
		if(msg!=null) { 
			return false; 
		}else {
			UserModel user = userDao.getUserByEmailId(userEmail);
			if(user!=null) {
				String randomUUID = UUID.randomUUID().toString();
				if(userDao.updateUUID(userEmail,randomUUID)){
					String URL= requestUrl+randomUUID;
					String to = userEmail;
					String subject = "Link to reset password";
					String message =URL;
					if(userMail.sendMail(to, subject, message)){
						return true;	
					}
				}
				return false;	
			}
			return false;
		}
	}
	@Override
	public String getEmailByUUID(String userUUID) {
		String userEmail=userDao.fetchEmailByUUID(userUUID);
		return userEmail!=null?userEmail:null;
	}
	@Override
	public boolean resetPassword(UserDTO DTOuser) {
		UserModel user = new UserModel(DTOuser.getUserEmail(),DTOuser.getPassWord());
		String hashCode=passwordEncoder.encode(user.getPassWord());
		user.setPassWord(hashCode);
		return (userDao.updatePassword(user.getPassWord(),user.getUserEmail())==true)?true:false; 
	}
	@Override
	public boolean userActivation(String userUUID) {
		UserModel user = userDao.getUserByUUID(userUUID);
		if(user!=null) {
			user.setActive(true);
			userDao.activateUser(user);
			return true;
		}
		return false; 
	}
	public UserModel fetchUserByUserId(int userId) {
		return userDao.getUserById(userId);
	}
	
}