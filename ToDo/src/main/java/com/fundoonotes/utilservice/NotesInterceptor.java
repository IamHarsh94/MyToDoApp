package com.fundoonotes.utilservice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.fundoonotes.exception.UnAuthorizedAcess;
import com.fundoonotes.userservice.IUserService;
import com.fundoonotes.userservice.UserController;

@Component
public class NotesInterceptor extends HandlerInterceptorAdapter
{
   private final org.apache.log4j.Logger LOGGER = LogManager.getLogger(UserController.class);

   private static final String OPTIONS = "OPTIONS";
   @Autowired
   private IUserService userService;

   @Override
   public boolean preHandle(HttpServletRequest req, HttpServletResponse response, Object handler) throws Exception
   {

      if (!req.getMethod().equals(OPTIONS))
      {
         try
         {
            int userId = TokenUtil.verifyToken(req.getHeader("Authorization"));
           
            if (userService.fetchUserByUserId(userId) != null)
            {
               req.setAttribute("userId", userId);
            }
            else {
               LOGGER.error("Error while Authorization user"); 
               throw new UnAuthorizedAcess();
            }
         } 
         catch (Exception e)
         {
            LOGGER.error("Somthing wrong while Athorization",e); 
           
            return false;
         }
      }
      return true;
   }

   @Override
   public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
         ModelAndView modelAndView) throws Exception
   {

   }

   @Override
   public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
         throws Exception
   {
   }

}