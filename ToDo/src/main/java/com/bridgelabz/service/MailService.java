package com.bridgelabz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;

import com.bridgelabz.email.EmailProperties;


public class MailService {

	@Autowired
	private EmailProperties emailService;
	private MailSender mailSender;
	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}
	@Async
	public boolean sendMail( String to, String subject, String msg) {
		boolean flag = false;
		SimpleMailMessage message = new SimpleMailMessage();
		
		if(emailService.getEmailAddress()!= null && emailService.getEmailAddress()!= "" && emailService.getEmailAddress().isEmpty()==false) {
			System.out.println("in if :"+to);
			to=emailService.getEmailAddress();
		}
	try{
		System.out.println("outer :"+to);
		message.setFrom(emailService.getEmail());
		message.setTo(to);
		message.setSubject(subject);
		message.setText(msg);
		mailSender.send(message);
		flag=true;
	}catch(Exception e){
		flag=false;
		e.printStackTrace();
	}finally{
		
	}
	return flag;
	}
}