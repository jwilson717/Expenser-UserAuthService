package org.generictech.userauth.aspect;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * Aspect class to handle logging for all controller methods. 
 * @author Jaden Wilson
 * @since 1.0
 */
@Aspect
@Slf4j
@Component
@Scope("request")
public class LoggingAspect {
	
	@Autowired
	private HttpServletRequest req;

	@Pointcut("execution(* org.generictech.userauth.controller.*.*(..))")
	public void controllerMethods() {}
	
	@Before("controllerMethods()")
	public void beforeHttpRequest() {
		log.info(req.getMethod() + " request to " + req.getServletPath() 
		+ ((req.getQueryString() != null) ? "?" + req.getQueryString(): ""));
	}
	
}
