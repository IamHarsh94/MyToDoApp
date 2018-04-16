package com.fundoonotes.noteservice;

public class ResCollaboratorDTO
{

   private int id;
   private int noteId;
   private int sharedUserId;
   private int userId;
  
   public int getId()
   {
      return id;
   }
   public void setId(int id)
   {
      this.id = id;
   }
   public int getNoteId()
   {
      return noteId;
   }
   public void setNoteId(int noteId)
   {
      this.noteId = noteId;
   }
   public int getSharedUserId()
   {
      return sharedUserId;
   }
   public void setSharedUserId(int sharedUserId)
   {
      this.sharedUserId = sharedUserId;
   }
   public int getUserId()
   {
      return userId;
   }
   public void setUserId(int userId)
   {
      this.userId = userId;
   }
   
   
}
