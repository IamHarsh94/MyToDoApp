package com.bridgelabz.todo.notes.model;

import java.util.Date;

public class NoteResponseDto {

	private int nodeId;

	private String title;

	private String description;

	private Date createDate;

	private Date lastUpdateDate;

	public NoteResponseDto() {

	}

	public NoteResponseDto(Note note) {
		this.nodeId = note.getId();
		this.title = note.getTitle();
		this.description =note.getDescription();
		this.createDate = note.getCreateDate();
		this.lastUpdateDate= note.getLastUpdateDate();
	}

	public int getNodeId() {
		return nodeId;
	}

	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
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
