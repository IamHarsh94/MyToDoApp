package com.bridgelabz.todo.interceptor;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

public class CORSFilter extends OncePerRequestFilter {
	
	private static final String OPTIONS = "OPTIONS";

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

	//if ("OPTIONS".equals(request.getMethod()))
	 //{
		  
		   response.addHeader("Access-Control-Allow-Origin", "*");
		   response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
		   response.addHeader("Access-Control-Allow-Headers", "Authorization,Content-Type,Accept, X-Requested-With");//get token
		   response.addHeader("Access-Control-Expose-Headers", "Authorization, Content-Type");
		   response.addHeader("Access-Control-Max-Age", "480000");
		   response.setStatus(HttpServletResponse.SC_OK);
	 //}else{
		   System.out.println("in cors filter");	
		 filterChain.doFilter(request, response);
	 //}
	
	      

	}
		
}
	
