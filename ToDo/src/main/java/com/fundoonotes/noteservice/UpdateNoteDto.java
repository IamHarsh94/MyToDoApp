package com.fundoonotes.noteservice;

import java.util.Date;

import com.mysql.jdbc.Blob;

public class UpdateNoteDto {

	private int noteId;
	private String title;
	private String description;
	private String color;
	private int status; 
	private Date reminder;
	private String image;

	  public String getImage()
   {
      return image;
   }

   public void setImage(String image)
   {
      this.image = image;
   }

   public String getTitle() {
		return title;
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



	public int getNoteId() {
		return noteId;
	}



	public void setNoteId(int noteId) {
		this.noteId = noteId;
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

}
