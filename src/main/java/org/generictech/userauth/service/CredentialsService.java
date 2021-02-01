package org.generictech.userauth.service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;

import org.generictech.userauth.exception.CredentialsNotFoundException;
import org.generictech.userauth.exception.InsertFailedException;
import org.generictech.userauth.model.Credentials;
import org.generictech.userauth.model.SystemUser;
import org.generictech.userauth.repo.CredentialsRepo;
import org.generictech.userauth.util.PasswordHashingUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Class to handle business logic pertaining to credentials
 * @author Jaden Wilson
 * @since 1.0
 */
@Service
public class CredentialsService {

	@Autowired
	private CredentialsRepo credentialsRepo;
	@Autowired
	private PasswordHashingUtility hashingUtility;
	
	/**
	 * Method to find user credentials by the credentials id.
	 * @param id of the credentials record
	 * @return Credentials object with the necessary credentials data.
	 * @throws CredentialsNotFoundException
	 * @since 1.0
	 */
	public Credentials findById(int id) throws CredentialsNotFoundException {
		Optional<Credentials> creds = credentialsRepo.findById(id);
		if (creds.isPresent()) {
			return creds.get();
		} else {
			throw new CredentialsNotFoundException();
		}
	}
	
	/**
	 * Method to find credentials for a specific user by user id
	 * @param id of the user for whom you are trying to find the credentials
	 * @return Credentials object
	 * @throws CredentialsNotFoundException
	 * @since 1.0
	 */
	public Credentials findByUserId(int id) throws CredentialsNotFoundException {
		Optional<Credentials> creds = credentialsRepo.findCredentials(id);
		if (creds.isPresent()) {
			return creds.get();
		} else {
			throw new CredentialsNotFoundException();
		}
	}
	
	/**
	 * Method to save a new set of credentials to the database.Can only have a single set of credentials per user
	 * so an Exception will occur if a set of credentials already exists for the specified user. 
	 * @param password
	 * @param user
	 * @return boolean stating whether the credentials were inserted correctly
	 * @throws InsertFailedException 
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchAlgorithmException 
	 * @throws Exception
	 * @since 1.0
	 */
	public boolean save(String password, SystemUser user) throws InsertFailedException, NoSuchAlgorithmException, InvalidKeySpecException  {
		String salt = hashingUtility.getSalt();
		String hashedPasswd = hashingUtility.generateHash(password, salt.getBytes());
		Credentials creds = new Credentials(hashedPasswd, salt, user);
		try {
			Credentials c = credentialsRepo.save(creds);
			if (c != null) {
				return true;
			} else {
				return false;
			}			
		} catch (Exception e) {
			throw new InsertFailedException("Credentials insert failed");
		}
	}
	
	/**
	 * Method to handle updating credentials.
	 * @param password new password value
	 * @param int id of user whos credentials you are trying to alter
	 * @return boolean
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws CredentialsNotFoundException
	 * @since 1.0
	 */
	public boolean update(String password, int id) throws NoSuchAlgorithmException, InvalidKeySpecException, CredentialsNotFoundException {
		String salt = hashingUtility.getSalt();
		String hashedPasswd = hashingUtility.generateHash(password, salt.getBytes());
		
		Optional<Credentials> loadedCreds = credentialsRepo.findCredentials(id);
		if (loadedCreds.isPresent()) {
			loadedCreds.get().setPassword(hashedPasswd);
			loadedCreds.get().setSalt(salt);
			credentialsRepo.save(loadedCreds.get());
			return true;
		} else {
			throw new CredentialsNotFoundException();
		}
	}
	
	/**
	 * Method to handle deleting credentials records
	 * @param id of the user to be deleted
	 * @return boolean
	 * @throws CredentialsNotFoundException
	 * @since 1.0
	 */
	public boolean delete(int id) throws CredentialsNotFoundException {
		Optional<Credentials> creds = credentialsRepo.findById(id);
		if (creds.isPresent()) {
			credentialsRepo.delete(creds.get());
			return true;
		} else {
			throw new CredentialsNotFoundException();
		}
	}
}
