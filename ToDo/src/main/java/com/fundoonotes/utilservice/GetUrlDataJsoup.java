package com.fundoonotes.utilservice;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


public class GetUrlDataJsoup {

   public UrlData getMetaData(String url) throws IOException 
   {
      String urlTitle=null;
      String urlImage=null;
      String urlDomain=null;

      try {
         URI uri=new URI(url);
         urlDomain=uri.getHost();
      } catch (URISyntaxException e) {

         e.printStackTrace();
      }
      Document document = Jsoup.connect(url).get();

      Elements metaOgTitle = document.select("meta[property=og:title]");

      if (metaOgTitle != null) 
      {
         urlTitle = metaOgTitle.attr("content");
         
         if (urlTitle == null) 
         {
            urlTitle = document.title();
         }
      }

      Elements metaOgImage = document.select("meta[property=og:image]");

      if (metaOgImage != null) 
      {
         urlImage = metaOgImage.attr("content");   
      }


      return new UrlData(urlTitle, urlImage,urlDomain );

   }
}
