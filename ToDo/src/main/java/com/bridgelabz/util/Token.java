package com.bridgelabz.util;

import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class Token {

	private static String key = "QwErTyUiOp";

	public static String generateToken(int id) {
		long currentTime = System.currentTimeMillis();
		Date currentDate = new Date(currentTime);
		Date expireDate = new Date(currentTime + 24 * 60 * 60 * 1000);
		
		JwtBuilder builder = Jwts.builder().setId(Integer.toString(id)).setIssuedAt(currentDate)
				.signWith(SignatureAlgorithm.HS256, key).setExpiration(expireDate);
		String generatedToken = builder.compact();
		return generatedToken;
	}

	public static int getId(String token) {
		int id = 0;
		Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
		System.out.println("ID: " + claims.getId());
		id = Integer.parseInt(claims.getId());
		return id;
	}
}
