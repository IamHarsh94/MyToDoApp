package com.fundoonotes.utilservice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.fundoonotes.userservice.IUserService;

@Component
public class UserInterceptor  extends HandlerInterceptorAdapter{

	@Autowired 
	private IUserService userService;
	
	@Override
	public boolean preHandle(HttpServletRequest req,
			HttpServletResponse response, Object handler) throws Exception {
			try {
				int userId = TokenUtil.verifyToken(req.getHeader("Authorization"));
				 if(userService.fetchUserByUserId(userId)!=null) {
					 req.setAttribute("userId",userId);
				 }
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		
		return true;
	}
 
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
 
	}
 
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}
	
}
