package com.fundoonotes.sociallogin;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fundoonotes.noteservice.NoteService;
import com.fundoonotes.userservice.IUserService;

@RestController
public class FacebookLoginController
{
   
   @Autowired
   private facebookConnection fbConnection;

   @Autowired
   private IUserService userService;

   @Autowired 
   private NoteService noteService;
   
   // This api hit from user 
   @RequestMapping(value = "facebookwithlogin", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
   public void loginwithFB(HttpServletRequest req, HttpServletResponse res)
   {  
      System.out.println("in fb controller");
      // get the fb login url
      String FBURL=fbConnection.getFBURL();
      
      try {
         // redirecting to fb url
         res.sendRedirect(FBURL);
         
      } catch (IOException e) {
         
         e.printStackTrace();
      }
      
   }
   // fb hiting this api 
   @RequestMapping(value = "facebookconnection", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
   public void connectwithFB(HttpServletRequest req, HttpServletResponse res)
   {  
      System.out.println("Response come from fb");
      
      
   }
}
