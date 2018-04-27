package com.fundoonotes.utilservice;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



public class getDatabyJsoup
{
  
   public UrlData getUrlMetaData(String url) throws IOException {

      String title = null;
      String imageUrl = null;
      String domain=null;
      try {
         URI uri=new URI(url);
         domain=uri.getHost();
         
      } catch (URISyntaxException e) {
        
         e.printStackTrace();
      }
      try{
      Document document = Jsoup.connect(url).get();
     
      Elements metaOgTitle = document.select("meta[property=og:title]");
      
      if (metaOgTitle != null) {
         title = metaOgTitle.attr("content");

         if (title == null) {
            title = document.title();

         }
      }
      
         Elements metaOgImage = document.select("img[src~=(?i)\\.(png|jpe?g|gif)]");
         Element image = metaOgImage.get(0);
              
        if (image != null) {
           imageUrl = image.attr("src");         
        }


      }catch(Exception e) {
         e.printStackTrace();
      }finally {
         
      }
      
      

           return new UrlData(title, imageUrl,domain);
   }
   
}
