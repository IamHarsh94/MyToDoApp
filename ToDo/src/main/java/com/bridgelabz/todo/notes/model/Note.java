package com.bridgelabz.todo.notes.model;

import java.util.Date;    
import com.bridgelabz.todo.user.model.User;

public class Note {

	private int id;
	private String title;
	private String description;
	private User user;
	private Date createDate;
	private Date lastUpdateDate;
	private int status;
	private String color;
	private Date reminder;
	
	
	public Note() {

	}
	
	public Date getReminder() {
		return reminder;
	}

	public void setReminder(Date reminder) {
		this.reminder = reminder;
	}

	public int getStatus() {
		return status;
	}


	public void setStatus(int status) {
		this.status = status;
	}


	
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		
		this.color = color;
	}
	

	public Note(CreateNoteDto newNoteDto) {
		this.title = newNoteDto.getTitle();
		this.description = newNoteDto.getDescription();
	}

	public Note(UpdateNoteDto updateDTO) {
		this.id= updateDTO.getNoteId();
		this.title = updateDTO.getTitle();
		this.description =updateDTO.getDescription();
		this.color =updateDTO.getColor();
		this.status=updateDTO.getStatus();
		this.reminder = updateDTO.getReminder();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

}