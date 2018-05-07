package com.fundoonotes.utilservice;

public class EmailProperties {
	String email;
	String password;
	String emailAddress;
	String host;
	
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getHost()
   {
      return host;
   }
   public void setHost(String host)
   {
      this.host = host;
   }
   @Override
	public String toString() {
		return "EmailProperties [host=" + host + ",email=" + email + ", password=" + password + ", emailAddress=" + emailAddress
				+ "]";
	}
}