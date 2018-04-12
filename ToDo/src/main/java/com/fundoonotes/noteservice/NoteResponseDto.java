package com.fundoonotes.noteservice;

import java.util.Date;
import java.util.List;

public class NoteResponseDto {

	private int noteId;

	private String title;

	private String description;

	private Date createDate;

	private Date lastUpdateDate;
	
	private String color;
	
	private int status;
	
	private Date reminder;
	
	private List<LabelResDTO> labelList;
	
	public NoteResponseDto() {

	}
	
	public NoteResponseDto(NoteModel note) {
		this.noteId = note.getId();
		this.title = note.getTitle();
		this.description =note.getDescription();
		this.createDate = note.getCreateDate();
		this.lastUpdateDate= note.getLastUpdateDate();
		this.color = note.getColor();
		this.status=note.getStatus();
		this.reminder =note.getReminder();
	}


   public List<LabelResDTO> getLabels()
   {
      return labelList;
   }

   public void setLabels(List<LabelResDTO> labels)
   {
      this.labelList = labels;
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
