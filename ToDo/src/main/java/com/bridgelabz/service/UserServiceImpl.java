package com.bridgelabz.service;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.bridgelabz.Dao.userDao;
import com.bridgelabz.model.DTO;
import com.bridgelabz.model.User;
import com.bridgelabz.util.Token;
import com.bridgelabz.validation.UserValidation;

@Service
public class UserServiceImpl implements UserService{
	@Autowired 
	private userDao userDao;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	private MailService userMail;
	@Autowired
	private Token userToken;
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
			boolean res =userDao.save(user);
			if(res) {
				String URL=url+randomUUID;
				String to = user.getUserEmail();
				String subject = "Registration confirmation link";
				String message = URL;
				return (userMail.sendMail(to, subject, message)==true)?true:false;
			}
		}
		return false;
	}
	@Override
	public String userLogin(DTO DTOuser){
		User user = new User(DTOuser.getUserEmail(), DTOuser.getPassWord());
		User userObj =userDao.getUserByEmailId(user.getUserEmail());
		if(userObj!=null && userObj.isActive() && BCrypt.checkpw(user.getPassWord(), userObj.getPassWord())) {
			int id=userObj.getId();
			return userToken.generateToken(id);
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
				String email = userEmail;
				String randomUUID = UUID.randomUUID().toString();
				if(userDao.updateUUID(email,randomUUID)){
					System.out.println("username and random id store");
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
		User user = new User();
		user.setUserEmail(DTOuser.getUserEmail());
		user.setPassWord(DTOuser.getPassWord());
		String hashCode=passwordEncoder.encode(user.getPassWord());
		user.setPassWord(hashCode);
		if(!userDao.updatePassword(user.getPassWord(),user.getUserEmail())) {
			return false;
		}
		return true;
	}
	@Override
	public boolean userActivation(String userUUID) {
		User user = userDao.getUserByUUID(userUUID);
		user.setActive(true);
		return (userDao.activateUser(user)==true)?true:false; 
	}
}