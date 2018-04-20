package com.fundoonotes.sociallogin;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.springframework.stereotype.Component;

@Component
public class facebookConnection
{

   public static final String applicationId = "216332969128183";
   public static final String secreateId = "8941a273cf262974069a72c5c3037c83";
   // redirect url for fb means fb redirecting to this api
   public static final String redirectUrl="http://localhost:8080/ToDo/facebookconnection";
   

   public String getFBURL() {
      String fbLoginURL = "";
      try {
         // creating the url with passing the app id and redirecting url
         fbLoginURL = "http://www.facebook.com/dialog/oauth?" + "client_id=" +applicationId+ "&redirect_uri="
               + URLEncoder.encode(redirectUrl, "UTF-8") + "&state=123&response_type=code"
               + "&scope=public_profile,email";
         
      } catch (UnsupportedEncodingException e) {
         
         e.printStackTrace();
      }
      return fbLoginURL;
   }
   
}
