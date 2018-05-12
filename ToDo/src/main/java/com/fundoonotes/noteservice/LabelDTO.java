package com.fundoonotes.noteservice;

import com.fundoonotes.userservice.UserModel;

public class LabelDTO {
	
	public int labelId;
	public String labelTitle;
	public UserModel user;
	public boolean deleteLabel;
	
	public boolean isDeleteLabel()
   {
      return deleteLabel;
   }
   public void setDeleteLabel(boolean deleteLabel)
   {
      this.deleteLabel = deleteLabel;
   }
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
	public UserModel getUser() {
		return user;
	}
	public void setUser(UserModel user) {
		this.user = user;
	}
	
	
}
