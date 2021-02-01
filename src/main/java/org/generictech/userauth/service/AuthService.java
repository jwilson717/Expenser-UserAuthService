package org.generictech.userauth.service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.generictech.userauth.dto.CredentialsDTO;
import org.generictech.userauth.dto.SystemUserDTO;
import org.generictech.userauth.exception.BadParameterException;
import org.generictech.userauth.exception.CredentialsNotFoundException;
import org.generictech.userauth.exception.InvalidTokenException;
import org.generictech.userauth.exception.SystemUserNotFoundException;
import org.generictech.userauth.model.Credentials;
import org.generictech.userauth.model.SystemUser;
import org.generictech.userauth.util.PasswordHashingUtility;
import org.generictech.userauth.util.TokenUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureException;

/**
 * Service class to handle authentication logic
 * @author Jaden Wilson
 * @since 1.0
 */
@Service
public class AuthService {

	@Autowired
	private CredentialsService credentialsService;
	@Autowired
	private PasswordHashingUtility hashingUtility;
	@Autowired
	private SystemUserService systemUserService;
	@Autowired 
	private TokenUtility tokenUtility;
	
	/**
	 * Method to handle login processes. 
	 * @param creds object with authentication credentials
	 * @return SystemUserDTO containing minimal user data.
	 * @throws SystemUserNotFoundException
	 * @throws CredentialsNotFoundException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @since 1.0
	 */
	public SystemUserDTO login(CredentialsDTO creds) throws SystemUserNotFoundException
	, CredentialsNotFoundException, NoSuchAlgorithmException, InvalidKeySpecException {
		SystemUser user = systemUserService.findByUsername(creds.getUsername());
		Credentials loadedCreds = credentialsService.findByUserId(user.getId());
		
		if (hashingUtility.validatePassword(creds.getPassword(), loadedCreds.getPassword(), loadedCreds.getSalt())) {
			return new SystemUserDTO(user.getId(), user.getUsername(), user.getEmail());
		} else {
			return null;
		}
	}
	
	/**
	 * Method to validate a token. To be used by the AuthController so tokens can be validated in other services. 
	 * @param token Object containing a string token
	 * @return SystemUserDTO object with minimal user data. 
	 * @throws NumberFormatException
	 * @throws SystemUserNotFoundException
	 * @throws InvalidTokenException
	 * @throws BadParameterException 
	 * @since 1.0
	 */
	public SystemUserDTO validateToken(String token) throws NumberFormatException, SystemUserNotFoundException, InvalidTokenException, BadParameterException {
		if (token == null) {
			throw new BadParameterException("Token cannot be null");
		}
		try {
			Claims claim = tokenUtility.decodeJWT(token);
			SystemUser user = systemUserService.findById(Integer.valueOf(claim.getId()));
			SystemUserDTO userData = new SystemUserDTO(user.getId(), user.getUsername(), user.getEmail());
			return userData;			
		} catch (SignatureException e) {
			throw new InvalidTokenException();
		}
	}
}
