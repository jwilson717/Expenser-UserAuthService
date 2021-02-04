package org.generictech.userauth.controller;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.servlet.http.HttpServletRequest;

import org.generictech.userauth.dto.SystemUserDataDTO;
import org.generictech.userauth.dto.SystemUserParams;
import org.generictech.userauth.exception.BadParameterException;
import org.generictech.userauth.exception.CredentialsNotFoundException;
import org.generictech.userauth.exception.InsertFailedException;
import org.generictech.userauth.exception.SystemUserNotFoundException;
import org.generictech.userauth.model.SystemUser;
import org.generictech.userauth.service.SystemUserService;
import org.generictech.userauth.util.TokenUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Claims;

/**
 * Class to handle HTTP requests pertaining to system users. 
 * @author Jaden Wilson
 * @since 1.0
 */
@RestController 
@RequestMapping("/systemuser")
public class SystemUserController {

	@Autowired
	private SystemUserService systemUserService;
	@Autowired
	private TokenUtility tokenUtility;
	
	/**
	 * Method to handle HTTP requests to GET users by specific criteria. This method calls the findByCriteria 
	 * method of the {@link SystemUserService} class
	 * @param params
	 * @return ResponseEntity<SystemUser>
	 * @throws BadParameterException 
	 * @throws NumberFormatException 
	 * @throws SystemUserNotFoundException 
	 * @since 1.0
	 */
	@GetMapping("")
	public ResponseEntity<SystemUser> findByCriteria(SystemUserParams params, HttpServletRequest req) 
			throws NumberFormatException, BadParameterException, SystemUserNotFoundException {
		Claims claims = tokenUtility.decodeJWT(req.getHeader("tokenId"));
		if (req.getQueryString() != null && params.empty()) {
			throw new BadParameterException();
		}
		return new ResponseEntity<SystemUser>(systemUserService.findByCriteria(params, Integer.valueOf(claims.getId())), HttpStatus.OK);
	}
	
	/**
	 * Method to handle HTTP request to POST a new user to the database. 
	 * @param userData mapped JSON data for the user
	 * @return ResponseEntity<SystemUser>
	 * @throws InsertFailedException 
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchAlgorithmException 
	 * @throws Exception 
	 * @since 1.0
	 */
	@PostMapping("")
	public ResponseEntity<SystemUser> save(@RequestBody SystemUserDataDTO userData) 
			throws NoSuchAlgorithmException, InvalidKeySpecException, InsertFailedException {
		return new ResponseEntity<SystemUser>(systemUserService.save(userData), HttpStatus.CREATED);
	}
	
	/**
	 * Method to handle HTTP request to PUT the data for a specific user
	 * @param id value of the user
	 * @param userData data to be altered
	 * @return ResponseEntity<SystemUser>
	 * @throws CredentialsNotFoundException 
	 * @throws SystemUserNotFoundException 
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchAlgorithmException 
	 * @since 1.0
	 */
	@PutMapping("/{id}")
	public ResponseEntity<SystemUser> update(@PathVariable int id, @RequestBody SystemUserDataDTO userData) 
			throws NoSuchAlgorithmException, InvalidKeySpecException, SystemUserNotFoundException, CredentialsNotFoundException {
		userData.setId(id);
		return new ResponseEntity<SystemUser>(systemUserService.update(userData), HttpStatus.OK);
	}
	
	/**
	 * Method to handle HTTP request to DELETE a specific user.
	 * @param id of the user to be deleted
	 * @return ResponseEntity<Object> (No_Content)
	 * @throws CredentialsNotFoundException 
	 * @throws SystemUserNotFoundException 
	 * @since 1.0
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> delete(@PathVariable int id) throws SystemUserNotFoundException, CredentialsNotFoundException {
		systemUserService.delete(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
	}
	
}
