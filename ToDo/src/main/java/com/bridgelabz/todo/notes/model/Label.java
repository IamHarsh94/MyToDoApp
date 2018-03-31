package com.bridgelabz.todo.notes.model;

import com.bridgelabz.todo.user.model.User;

public class Label {
	
	public int labelId;
	public String labelTitle;
	public User user;
	
	
	public int getLabelId() {
		return labelId;
	}
	public void setLabelId(int labelId) {
		this.labelId = labelId;
	}
	public String getLabelTitle() {
		return labelTitle;
	}
	public void setLabelTitle(String labelTitle) {
		this.labelTitle = labelTitle;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	
}
