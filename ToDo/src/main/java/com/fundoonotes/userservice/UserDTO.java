package com.fundoonotes.userservice;

public class UserDTO {
	private String fullName;
	private String userEmail;
	private String passWord;
	private String confirmpassword;
	private String mobileNum;
	private String address;
	
	
	public UserDTO()
   {
     
   }
   public UserDTO(UserModel object)
   {
	   this.fullName = object.getFullName();
	   this.userEmail = object.getUserEmail();
   }
   public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	public String getConfirmpassword() {
		return confirmpassword;
	}
	public void setConfirmpassword(String confirmpassword) {
		this.confirmpassword = confirmpassword;
	}
	public String getMobileNum() {
		return mobileNum;
	}
	public void setMobileNum(String mobileNum) {
		this.mobileNum = mobileNum;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
}
