package com.fundoonotes.noteservice;

import java.util.List;

public class JsoupUrlDTO
{
   private int noteId;
   private List<String> urls;

   public int getNoteId()
   {
      return noteId;
   }

   public void setNoteId(int noteId)
   {
      this.noteId = noteId;
   }

   public JsoupUrlDTO()
   {
    
   }

   public List<String> getUrls()
   {
      return urls;
   }

   public void setUrls(List<String> urls)
   {
      this.urls = urls;
   }


   
}
