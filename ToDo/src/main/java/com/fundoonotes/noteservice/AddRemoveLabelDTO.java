package com.fundoonotes.noteservice;

public class AddRemoveLabelDTO
{
   private boolean checked;
   private int labelId;
   private int noteId;
   
   
   public boolean isChecked()
   {
      return checked;
   }
   public void setChecked(boolean checked)
   {
      this.checked = checked;
   }
   public int getLabelId()
   {
      return labelId;
   }
   public void setLabelId(int labelId)
   {
      this.labelId = labelId;
   }
   public int getNoteId()
   {
      return noteId;
   }
   public void setNoteId(int noteId)
   {
      this.noteId = noteId;
   }
   
   
}
