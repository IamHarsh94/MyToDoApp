package com.bridgelabz.todo.user.service;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bridgelabz.todo.user.Dao.userDao;
import com.bridgelabz.todo.user.model.DTO;
import com.bridgelabz.todo.user.model.User;
import com.bridgelabz.todo.user.util.TokenUtil;
import com.bridgelabz.todo.user.validation.UserValidation;

@Service
public class UserServiceImpl implements UserService{
	@Autowired 
	private userDao userDao;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	private MailService userMail;
	@Autowired
	private TokenUtil userToken;
	@Transactional(rollbackFor = Exception.class)
	public boolean userRegistration(DTO DTOuser,String url) throws Exception {
		User user = new User(DTOuser.getFullName(), DTOuser.getUserEmail(), DTOuser.getPassWord(), DTOuser.getConfirmpassword(), DTOuser.getMobileNum(),
				DTOuser.getAddress());
		User user1=userDao.getUserByEmailId(user.getUserEmail());
		
		if(user1 == null) {
			String hashCode=passwordEncoder.encode(user.getPassWord());
			user.setPassWord(hashCode);
			String randomUUID = UUID.randomUUID().toString();
			user.setUUID(randomUUID);
			if(userDao.save(user)) {
				String URL=url+randomUUID;
				String to = user.getUserEmail();
				String subject = "Registration confirmation link";
				String message = URL;
				return (userMail.sendMail(to, subject, message)==true)?true:false;
			}
		}
		return false;
	}
	@SuppressWarnings("static-access")
	@Override
	public String userLogin(DTO DTOuser){
		
		User user = new User(DTOuser.getUserEmail(), DTOuser.getPassWord());
		
		User userObj =userDao.getUserByEmailId(user.getUserEmail());
		
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
			User user = userDao.getUserByEmailId(userEmail);
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
	public boolean resetPassword(DTO DTOuser) {
		User user = new User(DTOuser.getUserEmail(),DTOuser.getPassWord());
		String hashCode=passwordEncoder.encode(user.getPassWord());
		user.setPassWord(hashCode);
		return (userDao.updatePassword(user.getPassWord(),user.getUserEmail())==true)?true:false; 
	}
	@Override
	public boolean userActivation(String userUUID) {
		User user = userDao.getUserByUUID(userUUID);
		user.setActive(true);
		return (userDao.activateUser(user)==true)?true:false; 
	}
	public User fetchUserByUserId(int userId) {
		return userDao.getUserById(userId);
	}
	
}