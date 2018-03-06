package com.bridgelabz.todo.user.service;

import java.util.ArrayList;
import java.util.List;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class MailHelperService 
{
	private JavaMailSender mailSender;
	
	public MailHelperService() {}

	public void setMailSender(JavaMailSender mailSender) 
	{
		this.mailSender = mailSender;
	}
	
	public void send(String subject, String msg, String [] toAddress) throws MessagingException
	{
		send(subject, msg, toAddress, null, null);
	}
	public void send(String subject, String msg, String toAddress) throws MessagingException
	{
		send(subject, msg, toAddress, null);
	}
	public void send(String subject, String msg, String toAddress, String ccAddress) throws MessagingException
	{
		send(subject, msg, new String [] {toAddress}, new String [] {ccAddress}, null);
	}
	
	public void send(String subject, String msg, String [] toAddress, String [] ccAddress, String [] fromAddress) throws MessagingException
	{
		MimeMessage message = mailSender.createMimeMessage();
		
		
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		
		InternetAddress [] addresses = getAddresses(toAddress);
		
		if( addresses.length == 0 )
		{
			return;
		}
		helper.setTo(addresses);
		addresses = getAddresses(ccAddress);
		
		if( addresses.length > 0 )
		{
			helper.setCc(addresses);
		}
		
		addresses = getAddresses(fromAddress);
		
		if( addresses.length > 0 )
		{
			helper.setFrom(addresses[0]);;
		}
		
		helper.setSubject(subject);
		helper.setText(msg, true);// for html content boolean value should be true
		
		mailSender.send(message);
	}

	private InternetAddress[] getAddresses(String[] addresses) throws AddressException 
	{
		List<Address> laddress = new ArrayList<>();
		
		if(addresses != null) 
		{
			for (String address : addresses) 
			{
				if(address != null && !address.trim().isEmpty())
					laddress.add(new InternetAddress(address));
			}
		}
		return laddress.toArray(new InternetAddress [] {});
	}	
}