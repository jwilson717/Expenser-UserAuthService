package org.generictech.userauth.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.generictech.userauth.util.TokenUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;

/**
 * Class to handle token authentication for spring security
 * @author Jaden Wilson
 *@since 1.0
 */
@Component
@Slf4j
public class TokenAuthFilter extends OncePerRequestFilter {
	
	private final String HEADER = "tokenId";
	@Autowired
	private TokenUtility tokenUtility;
	private final ArrayList<String> PUBLIC_ENDPOINTS = new ArrayList<>(Arrays.asList("/login", "/systemuser", "/validate"));
	
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws 
		IOException, ServletException {
		if (req.getMethod().equals("POST") && PUBLIC_ENDPOINTS.contains(req.getServletPath())) {
			log.info("Unauthenticated endpoint. Ingoring tokenId header if present");
			chain.doFilter(req, res);
			return;
		}
		UsernamePasswordAuthenticationToken auth = getAuthentication(req);
		
		SecurityContextHolder.getContext().setAuthentication(auth);
		chain.doFilter(req, res);
	}
	
	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest req) {
		String token = req.getHeader(HEADER);
		if (token != null) {
			 Claims claims = tokenUtility.decodeJWT(token);
			 String user = claims.getSubject();
			if (user != null) {
				return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
			}
			return null;
		}
	
		return null;
	}

}
