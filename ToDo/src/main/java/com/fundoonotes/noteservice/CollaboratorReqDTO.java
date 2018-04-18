package com.fundoonotes.noteservice;

public class CollaboratorReqDTO
{
   private int id;
   private String personEmail;
   private String removeUserMail;
   private int noteId;
   
   public String getRemoveUserMail()
   {
      return removeUserMail;
   }

   public void setRemoveUserMail(String removeUserMail)
   {
      this.removeUserMail = removeUserMail;
   }

   public int getId()
   {
      return id;
   }

   public void setId(int id)
   {
      this.id = id;
   }

   public String getPersonEmail()
   {
      return personEmail;
   }

   public void setPersonEmail(String personEmail)
   {
      this.personEmail = personEmail;
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
