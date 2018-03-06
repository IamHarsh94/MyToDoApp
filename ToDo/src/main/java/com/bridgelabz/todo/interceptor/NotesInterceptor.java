package com.bridgelabz.todo.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.bridgelabz.todo.user.util.Token;

@Component
public class NotesInterceptor extends HandlerInterceptorAdapter {
	
	@Override
	public boolean preHandle(HttpServletRequest req,
			HttpServletResponse response, Object handler) throws Exception {
			int userId = Token.verifyToken(req.getHeader("authtoken"));
			req.setAttribute("userId",userId);
			return true;
	}
 
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
			
			System.out.println("postHandle :: Request inside postHandle");
 
	}
 
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
			System.out.println("afterCompletion :: Request inside afterCompletion");
 
	}
 
	
	
}