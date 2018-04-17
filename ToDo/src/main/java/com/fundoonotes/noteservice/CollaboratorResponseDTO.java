package com.fundoonotes.noteservice;

public class CollaboratorResponseDTO
{
   private int noteId;
   private String title;
   private String description;
   private String fullName;
   private String color;
   private int ownerId;
   
   public int getNoteId()
   {
      return noteId;
   }
   public void setNoteId(int noteId)
   {
      this.noteId = noteId;
   }
   public int getOwnerId()
   {
      return ownerId;
   }
   public void setOwnerId(int ownerId)
   {
      this.ownerId = ownerId;
   }
   public String getColor()
   {
      return color;
   }
   public void setColor(String color)
   {
      this.color = color;
   }
   public String getTitle()
   {
      return title;
   }
   public void setTitle(String title)
   {
      this.title = title;
   }
   public String getDescription()
   {
      return description;
   }
   public void setDescription(String description)
   {
      this.description = description;
   }
   public String getFullName()
   {
      return fullName;
   }
   public void setFullName(String fullName)
   {
      this.fullName = fullName;
   }
   
   
}
