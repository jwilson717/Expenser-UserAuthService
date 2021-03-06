package org.generictech.userauth.controller;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.generictech.userauth.dto.CredentialsDTO;
import org.generictech.userauth.dto.SystemUserDTO;
import org.generictech.userauth.dto.Token;
import org.generictech.userauth.exception.BadParameterException;
import org.generictech.userauth.exception.CredentialsNotFoundException;
import org.generictech.userauth.exception.InvalidTokenException;
import org.generictech.userauth.exception.SystemUserNotFoundException;
import org.generictech.userauth.service.AuthService;
import org.generictech.userauth.util.TokenUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * Controller class to handle HTTP requests pertaining to authentication. 
 * @author Jaden Wilson
 * @since 1.0
 */
@RestController
@Slf4j
public class AuthController {

	@Autowired
	private AuthService authService;
	@Autowired
	private TokenUtility tokenUtility;
	
	/**
	 * Method to handle HTTP request to login to the application. Checks credentials with the AuthService, and returns minimal user data and
	 * a JWT token in the header. 
	 * @param creds
	 * @return ResponseEntity<SystemUserDTO> 
	 * @throws CredentialsNotFoundException 
	 * @throws SystemUserNotFoundException 
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchAlgorithmException 
	 * @since 1.0
	 */
	@PostMapping("/login")
	public ResponseEntity<SystemUserDTO> login(@RequestBody CredentialsDTO creds) 
			throws NoSuchAlgorithmException, InvalidKeySpecException, SystemUserNotFoundException, CredentialsNotFoundException {
		SystemUserDTO user = authService.login(creds);
		if (user != null) {
			String token = tokenUtility.createJWT(String.valueOf(user.getId()), "org.generictech.Expenser", user.getUsername(), 20000000);
			log.info("User " + user.getUsername() + " succesfully logged in");
			return ResponseEntity.status(HttpStatus.OK).header("tokenId", token).body(user);				
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}
	
	/**
	 * Method to validate JWT tokens to authenticate users
	 * @param token
	 * @return ResponseEntity<SystemUserDTO>
	 * @throws BadParameterException 
	 * @throws InvalidTokenException 
	 * @throws SystemUserNotFoundException 
	 * @throws NumberFormatException 
	 * @since 1.0
	 */
	@PostMapping("/validate")
	public ResponseEntity<SystemUserDTO> validateToken(@RequestBody Token token) 
			throws NumberFormatException, SystemUserNotFoundException, InvalidTokenException, BadParameterException {
		return new ResponseEntity<SystemUserDTO>(authService.validateToken(token.getToken()), HttpStatus.OK);
	}

}
