package com.bridgelabz.todo.user.model;

public class User {
	private int id;
	private String fullName;
	private String userEmail;
	private String passWord;
	private String confirmpassword;
	private String mobileNum;
	private String address;
	private boolean isActive;
	private String UUID;
	private String token;
	public User() {

	}
	public User(String userEmail, String passWord) {
		this.userEmail = userEmail;
		this.passWord = passWord;
	}
	public User(String fullName, String userEmail, String passWord, String confirmpassword, String mobileNum,
			String address) {
		this.fullName = fullName;
		this.userEmail = userEmail;
		this.passWord = passWord;
		this.confirmpassword = confirmpassword;
		this.mobileNum = mobileNum;
		this.address = address;
	}
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUUID() {
		return UUID;
	}
	public void setUUID(String uUID) {
		UUID = uUID;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
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
