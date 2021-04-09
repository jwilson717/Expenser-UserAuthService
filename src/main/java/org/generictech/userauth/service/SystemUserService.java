package org.generictech.userauth.service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;

import org.generictech.userauth.dto.SystemUserDataDTO;
import org.generictech.userauth.dto.SystemUserParams;
import org.generictech.userauth.exception.BadParameterException;
import org.generictech.userauth.exception.CredentialsNotFoundException;
import org.generictech.userauth.exception.InsertFailedException;
import org.generictech.userauth.exception.SystemUserNotFoundException;
import org.generictech.userauth.model.Credentials;
import org.generictech.userauth.model.SystemUser;
import org.generictech.userauth.repo.SystemUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Class to handle business logic concerning the system_user table.
 * @author Jaden Wilson
 * @since 1.0
 */
@Service
public class SystemUserService {

	@Autowired
	private SystemUserRepo systemUserRepo;
	@Autowired
	private CredentialsService credentialsService;
	
	/**
	 * Method to find a user by id value.
	 * @param id of the desired user
	 * @return {@link SystemUser}
	 * @throws SystemUserNotFoundException
	 * @since 1.0
	 */
	public SystemUser findById(int id) throws SystemUserNotFoundException {
		if (systemUserRepo.findById(id).isPresent()) {
			return systemUserRepo.findById(id).get();
		} else {
			throw new SystemUserNotFoundException();
		}
	}
	
	/**
	 * Method to find a user by username
	 * @param username
	 * @return {@link SystemUser}
	 * @throws SystemUserNotFoundException
	 * @since 1.0
	 */
	public SystemUser findByUsername(String username) throws SystemUserNotFoundException {
		if (systemUserRepo.findByUsername(username).isPresent()) {
			return systemUserRepo.findByUsername(username).get();
		} else {
			throw new SystemUserNotFoundException();
		}
	}
	
	/**
	 * Method to handle finding a user by email. 
	 * @param email
	 * @return {@link SystemUser}
	 * @throws SystemUserNotFoundException
	 * @since 1.0
	 */
	public SystemUser findByEmail(String email) throws SystemUserNotFoundException {
		if (systemUserRepo.findByEmail(email).isPresent()) {
			return systemUserRepo.findByEmail(email).get();
		} else {
			throw new SystemUserNotFoundException();
		}
	}
	
	/**
	 * Method to handle finding users by specified criteria parameters. This method will only allow searching by a single
	 * specific value. If multiple values are provided it will search by the most specific first.
	 * @param params Parameters provided to the controller via HTTP request
	 * @return {@link SystemUser}
	 * @throws BadParameterException
	 * @throws SystemUserNotFoundException
	 * @since 1.0
	 */
	public SystemUser findByCriteria(SystemUserParams params, int id) throws BadParameterException, SystemUserNotFoundException {
		Optional<SystemUser> user = Optional.ofNullable(null);
		if (params == null) {
			throw new BadParameterException();
		}
		if (params.getId() != 0) {
			user = systemUserRepo.findById(params.getId());
		} else if (params.getUsername() != null) {
			user = systemUserRepo.findByUsername(params.getUsername());
		} else if (params.getEmail() != null) {
			user = systemUserRepo.findByEmail(params.getEmail());
		} else if (params.empty()) {
			user = systemUserRepo.findById(id);
		} 
		
		if (user.isPresent() && user.get() != null) {
			return user.get();
		} else {
			throw new SystemUserNotFoundException();
		}
	}
	
	/**
	 * Method to handle saving a new user to the database.
	 * @param userData
	 * @return {@link SystemUser}
	 * @throws InsertFailedException 
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchAlgorithmException 
	 * @throws Exception
	 * @since 1.0
	 */
	public SystemUser save(SystemUserDataDTO userData) throws NoSuchAlgorithmException, InvalidKeySpecException, InsertFailedException  {
		SystemUser user = new SystemUser(userData.getFirstName(), userData.getLastName()
				, userData.getEmail(), userData.getUsername());
		SystemUser u = systemUserRepo.save(user);
		if (!credentialsService.save(userData.getPassword(), u)) {
			systemUserRepo.delete(u);
			throw new InsertFailedException("Credentials insert failed");
		}
		return u;
	}
	
	/**
	 * Method to handle updating a users data in the database. This method checks each value to see if it contains new data and
	 * if it does it updates the data and persists the changes. 
	 * @param userData
	 * @return {@link SystemUser}
	 * @throws SystemUserNotFoundException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws CredentialsNotFoundException
	 * @since 1.0
	 */
	public SystemUser update(SystemUserDataDTO userData) throws SystemUserNotFoundException, NoSuchAlgorithmException, InvalidKeySpecException, CredentialsNotFoundException {
		Optional<SystemUser> loadedUser = systemUserRepo.findById(userData.getId());
		if (loadedUser.isPresent()) {
			SystemUser user = loadedUser.get();
			if (userData.getFirstName() != null) {
				user.setFirstName(userData.getFirstName());
			}
			if (userData.getLastName() != null) {
				user.setLastName(userData.getLastName());
			}
			if (userData.getEmail() != null) {
				user.setEmail(userData.getEmail());
			}
			
			SystemUser u = systemUserRepo.save(user);
			
			if (userData.getPassword() != null) {
				credentialsService.update(userData.getPassword(), userData.getId());
			}
			
			return u;
			
		} else {
			throw new SystemUserNotFoundException();
		}
	}
	
	/**
	 * Method to handle deleting a user from the database.
	 * @param id
	 * @return boolean
	 * @throws SystemUserNotFoundException
	 * @throws CredentialsNotFoundException
	 * @since 1.0
	 */
	public boolean delete(int id) throws SystemUserNotFoundException, CredentialsNotFoundException {
		Optional<SystemUser> user = systemUserRepo.findById(id);
		if (user.isPresent()) {
			Credentials creds = credentialsService.findByUserId(user.get().getId());
			credentialsService.delete(creds.getId());
			systemUserRepo.delete(user.get());
			return true;
		} else {
			throw new SystemUserNotFoundException();
		}
	}
}
