package org.generictech.userauth.aspect;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;

import org.generictech.userauth.exception.BadParameterException;
import org.generictech.userauth.exception.CredentialsNotFoundException;
import org.generictech.userauth.exception.ExceptionResponse;
import org.generictech.userauth.exception.InsertFailedException;
import org.generictech.userauth.exception.InvalidTokenException;
import org.generictech.userauth.exception.SystemUserNotFoundException;
import org.generictech.userauth.util.PasswordHashingUtility;
import org.generictech.userauth.util.TokenUtility;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import lombok.extern.slf4j.Slf4j;

/**
 * Class to handle custsom exceptions across all controllers.
 * @author Jaden Wilson
 * @since 1.0
 */
@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler {
	
	/**
	 * Exception handler method for an InvalidTokenException. 
	 * @param web WebRequest
	 * @param e Exception
	 * @return ResponseEntity<ExceptionResponse>
	 * @since 1.0
	 */
	@ExceptionHandler(InvalidTokenException.class)
	public ResponseEntity<ExceptionResponse> invalidToken(WebRequest web, Exception e) {
		log.error(e.getMessage());
		return new ResponseEntity<ExceptionResponse>(new ExceptionResponse(new Date()
				, 401, e.getClass().getSimpleName() , e.getMessage()), HttpStatus.UNAUTHORIZED);
	}

	/**
	 * Exception handler method for {@link BadParameterExceptions}. 
	 * @param web WebRequest
	 * @param e Exception
	 * @return ResponseEntity<ExceptionResponse>
	 * @since 1.0
	 */
	@ExceptionHandler(BadParameterException.class)
	public ResponseEntity<ExceptionResponse> badParamException(WebRequest web, Exception e) {
		log.error(e.getMessage());
		return new ResponseEntity<ExceptionResponse>(new ExceptionResponse(new Date()
					, 400, e.getClass().getSimpleName() , e.getMessage()), HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * Exception handler method for {@link BadParameterExceptions}. 
	 * @param web WebRequest
	 * @param e Exception
	 * @return ResponseEntity<ExceptionResponse>
	 * @since 1.0
	 */
	@ExceptionHandler(InsertFailedException.class)
	public ResponseEntity<ExceptionResponse> insertFailedException(WebRequest web, Exception e) {
		log.error(e.getMessage());
		return new ResponseEntity<ExceptionResponse>(new ExceptionResponse(new Date()
					, 409, e.getClass().getSimpleName() , e.getMessage()), HttpStatus.CONFLICT);
	}
	
	/**
	 * Exception handler method for not found exceptions, including SystemUserNotFoundException and CredentialsNotFoundException.
	 * @param web WebRequest
	 * @param e Exception
	 * @return ResponseEntity<ExceptionResponse>
	 * @since 1.0
	 */
	@ExceptionHandler({SystemUserNotFoundException.class, CredentialsNotFoundException.class})
	public ResponseEntity<ExceptionResponse> notFoundException(WebRequest web, Exception e) {
		log.error(e.getMessage());
		return new ResponseEntity<ExceptionResponse>(new ExceptionResponse(new Date()
					, 404, e.getClass().getSimpleName() , e.getMessage()), HttpStatus.NOT_FOUND);
	}
	
	/**
	 * Exception handler method for internal server errors. These errors should only be caused by misconfigurations in the 
	 * {@link TokenUtility} and {@link PasswordHashingUtility} classes.
	 * @param e Exception 
	 * @return ResponseEntity<ExceptionResponse>
	 * @since 1.0
	 */
	@ExceptionHandler({NoSuchAlgorithmException.class, InvalidKeySpecException.class})
	public ResponseEntity<ExceptionResponse> internalServerErrors(Exception e){
		log.error(e.getMessage());
		return new ResponseEntity<ExceptionResponse>(new ExceptionResponse(new Date()
					, 500, e.getClass().getSimpleName() , e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
