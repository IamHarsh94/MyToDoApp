package com.fundoonotes.utilservice;

public class UrlData
{
   private int id;
   
   private int nodeId;
   
   private String title;
   
   private String imageUrl;
  
   private String domain;

   public int getNodeId()
   {
      return nodeId;
   }

   public void setNodeId(int nodeId)
   {
      this.nodeId = nodeId;
   }

  
   
   public UrlData()
   {
     
     
   }

   public  UrlData(String title,String imageUrl, String domain){
      this.title=title;
      this.imageUrl=imageUrl;
      this.domain=domain;
   }
   
   public int getId()
   {
      return id;
   }

   public void setId(int id)
   {
      this.id = id;
   }

   public String getTitle()
   {
      return title;
   }

   public void setTitle(String title)
   {
      this.title = title;
   }

   public String getImageUrl()
   {
      return imageUrl;
   }

   public void setImageUrl(String imageUrl)
   {
      this.imageUrl = imageUrl;
   }

   public String getDomain()
   {
      return domain;
   }

   public void setDomain(String domain)
   {
      this.domain = domain;
   }
   
   
}
