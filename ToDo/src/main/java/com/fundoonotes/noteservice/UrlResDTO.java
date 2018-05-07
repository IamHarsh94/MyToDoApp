package com.fundoonotes.noteservice;

import java.util.List;

public class UrlResDTO
{
   private int noteId;
   private List<String> urllist;

   public int getNoteId()
   {
      return noteId;
   }

   public void setNoteId(int noteId)
   {
      this.noteId = noteId;
   }

   public List<String> getUrllist()
   {
      return urllist;
   }

   public void setUrllist(List<String> urllist)
   {
      this.urllist = urllist;
   }

   
}
