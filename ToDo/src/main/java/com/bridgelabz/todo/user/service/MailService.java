package com.bridgelabz.todo.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;

import com.bridgelabz.todo.user.email.EmailProperties;
public class MailService {
	@Autowired
	private EmailProperties emailService;
	private MailSender mailSender;
	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}
	@Async
	public boolean sendMail( String to, String subject, String msg) {
		SimpleMailMessage message = new SimpleMailMessage();
		if(emailService.getEmailAddress()!= null && emailService.getEmailAddress()!= "" && emailService.getEmailAddress().isEmpty()==false) {
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
			e.printStackTrace();
			return false;
		}
	}
}