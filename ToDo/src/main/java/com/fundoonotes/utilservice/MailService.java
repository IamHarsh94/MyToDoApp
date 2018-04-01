package com.fundoonotes.utilservice;

import org.apache.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;

import com.fundoonotes.userservice.UserController;
public class MailService {
	
	@Autowired
	private EmailProperties emailService;
	
	@Autowired
	private MailSender mailSender;
	
	private final org.apache.log4j.Logger LOGGER = LogManager.getLogger(UserController.class);
	
	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}
	
	@Async
	public boolean sendMail( String to, String subject, String msg) 
	{
		SimpleMailMessage message = new SimpleMailMessage();
		
		/**
		 * <P>
		 * 	 note: Email propertise mention in the propertise file in resource folder
		 * </P>
		 * 	If the project on develpoment mode then we sending the email on admin email address 
		 * 	if not then sending mail to the registering user email id  
		 * */
		if(emailService.getEmailAddress()!= null && emailService.getEmailAddress()!= "" && emailService.getEmailAddress().isEmpty()==false) 
		{
			to=emailService.getEmailAddress();
		}
		try{
			message.setFrom(emailService.getEmail());
			message.setTo(to);
			message.setSubject(subject);
			message.setText(msg);
			
			mailSender.send(message);
			return true;
		
		}catch(Exception e){
			
			LOGGER.error("Error while sending the mail", e);
			return false;
		}
	}
}