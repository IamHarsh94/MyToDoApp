package com.fundoonotes.utilservice;

import java.io.IOException;
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

		 response.addHeader("Access-Control-Allow-Origin", "*");
		 response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
		 response.addHeader("Access-Control-Allow-Headers", "Authorization,Content-Type,Accept, X-Requested-With");//get token
		 response.addHeader("Access-Control-Expose-Headers", "Authorization, Content-Type");
		 response.addHeader("Access-Control-Max-Age", "480000");
		 response.setStatus(HttpServletResponse.SC_OK);
	
		 filterChain.doFilter(request, response);
	}
		
}
	
