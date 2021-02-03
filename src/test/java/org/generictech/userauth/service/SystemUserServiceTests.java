package org.generictech.userauth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Test class to test the SystemUserService class methods
 * @author Jaden Wilson
 * @since 1.0
 */
@ExtendWith(MockitoExtension.class)
public class SystemUserServiceTests {

	@Mock
	private SystemUserRepo systemUserRepo;
	
	@Mock
	private CredentialsService credentialsService;
	
	@InjectMocks
	private SystemUserService systemUserService;
	
	private SystemUser user;
	private SystemUser user2;
	private SystemUserParams params;
	private SystemUserDataDTO dto;
	private Credentials creds;
	
	@BeforeEach
	public void setUp() {
		user = new SystemUser(1, "Test", "Tester", "test@test.com", "TTester");
		user2 = new SystemUser(2, "Tested", "Testers", "test2@test.com", "TdTesters");
		params = new SystemUserParams();
		dto = new SystemUserDataDTO("Tested", "Testers", "test2@test.com", "TdTesters", "password2");
		creds = new Credentials("password2", "salt", user2);
	}
	
	/**
	 * Test to verify the successful finding of a specific user by id. 
	 */
	@Test
	public void findByIdSuccessfulTest() {
		doReturn(Optional.of(user)).when(systemUserRepo).findById(1);
		SystemUser testUser = null;
		try {
			testUser = systemUserService.findById(1);
		} catch (SystemUserNotFoundException e) {
			fail();
		}
		
		assertEquals(user, testUser);
	}
	
	/**
	 * Test to verify the throwing of a SystemUserNotFoundException when no
	 * user is found in the repo for the specified id.
	 */
	@Test
	public void findByIdNotFoundTest() {
		doReturn(Optional.ofNullable(null)).when(systemUserRepo).findById(3);
		
		assertThrows(SystemUserNotFoundException.class, () -> {
			systemUserService.findById(3);
		});
	}
	
	/**
	 * Test to verify the successful finding of a specific user by username. 
	 */
	@Test
	public void findByUsernameSuccessfulTest() {
		doReturn(Optional.of(user)).when(systemUserRepo).findByUsername("TTester");
		SystemUser testUser = null;
		try {
			testUser = systemUserService.findByUsername("TTester");
		} catch (SystemUserNotFoundException e) {
			fail();
		}
		
		assertEquals(user, testUser);
	}
	
	/**
	 * Test to verify the throwing of a SystemUserNotFoundException when no
	 * user is found in the repo for the specified username.
	 */
	@Test
	public void findByUsernameNotFoundTest() {
		doReturn(Optional.ofNullable(null)).when(systemUserRepo).findByUsername("Test");
		
		assertThrows(SystemUserNotFoundException.class, () -> {
			systemUserService.findByUsername("Test");
		});
	}
	
	/**
	 * Test to verify the successful finding of a specific user by email. 
	 */
	@Test
	public void findByEmailSuccessfulTest() {
		doReturn(Optional.of(user)).when(systemUserRepo).findByEmail("test@test.com");
		SystemUser testUser = null;
		try {
			testUser = systemUserService.findByEmail("test@test.com");
		} catch (SystemUserNotFoundException e) {
			fail();
		}
		
		assertEquals(user, testUser);
	}
	
	/**
	 * Test to verify the throwing of a SystemUserNotFoundException when no
	 * user is found in the repo for the specified email.
	 */
	@Test
	public void findByEmailNotFoundTest() {
		doReturn(Optional.ofNullable(null)).when(systemUserRepo).findByEmail("t@t.com");
		
		assertThrows(SystemUserNotFoundException.class, () -> {
			systemUserService.findByEmail("t@t.com");
		});
	}
	
	/**
	 * Test to verify a BadParameterException when params are null.
	 */
	@Test
	public void findByCriteriaNullParamsTest() {
		assertThrows(BadParameterException.class, () -> {
			systemUserService.findByCriteria(null, 1);
		});
	}
	
	/**
	 * Test to verify successful return of user when no params are passed. This
	 * should return the user associated with the JWT token in the request. 
	 */
	@Test 
	public void findByCriteriaSuccessNoParamsTest() {
		doReturn(Optional.of(user)).when(systemUserRepo).findById(1);
		
		SystemUser testUser = null;
		try {
			testUser = systemUserService.findByCriteria(params, 1);
		} catch (Exception e) {
			fail();
		} 
		
		assertEquals(user, testUser);
	}
	
	/**
	 * Test to verify successful return of user based upon id of parameters.
	 * 0 is used as the id value to verify that the params value is being used and not the id value.
	 */
	@Test
	public void findByCriteriaByIdSuccessTest() {
		params.setId(1);
		doReturn(Optional.of(user)).when(systemUserRepo).findById(1);
		SystemUser testUser = null;
		try {
			testUser = systemUserService.findByCriteria(params, 0);
		} catch (BadParameterException | SystemUserNotFoundException e) {
			fail();
		}
		
		assertEquals(user, testUser);
	}
	
	/**
	 * Test to verify SystemUserNotFoundException when user not found based 
	 * upon id of parameters. 0 is used as the id value to verify that the params value is being 
	 * used and not the id value.
	 */
	@Test
	public void findByCriteriaByIdNotFoundTest() {
		params.setId(2);
		doReturn(Optional.ofNullable(null)).when(systemUserRepo).findById(2);
		assertThrows(SystemUserNotFoundException.class, () -> {
			systemUserService.findByCriteria(params, 0);
		});
	}
	
	/**
	 * Test to verify successful return of user based upon the username field of params.
	 * 0 is used as the id value to verify that the params value is being used and not the id value.
	 */
	@Test
	public void findByCriteriaByUsernameSuccessTest() {
		params.setUsername("TTester");
		doReturn(Optional.of(user)).when(systemUserRepo).findByUsername("TTester");
		SystemUser testUser = null;
		try {
			testUser = systemUserService.findByCriteria(params, 0);
		} catch (BadParameterException | SystemUserNotFoundException e) {
			fail();
		}
		
		assertEquals(user, testUser);
	}
	
	/**
	 * Test to verify SystemUserNotFoundException when user not found based 
	 * upon the username field of params. 0 is used as the id value to verify that the params 
	 * value is being used and not the id value.
	 */
	@Test
	public void findByCriteriaByUsernameNotFoundTest() {
		params.setUsername("Test");
		doReturn(Optional.ofNullable(null)).when(systemUserRepo).findByUsername("Test");
		assertThrows(SystemUserNotFoundException.class, () -> {
			systemUserService.findByCriteria(params, 0);
		});
	}
	
	/**
	 * Test to verify successful return of user based upon the email field of params.
	 * 0 is used as the id value to verify that the params value is being used and not the id value.
	 */
	@Test
	public void findByCriteriaByEmailSuccessTest() {
		params.setEmail("test@test.com");
		doReturn(Optional.of(user)).when(systemUserRepo).findByEmail("test@test.com");
		SystemUser testUser = null;
		try {
			testUser = systemUserService.findByCriteria(params, 0);
		} catch (BadParameterException | SystemUserNotFoundException e) {
			fail();
		}
		
		assertEquals(user, testUser);
	}
	
	/**
	 * Test to verify SystemUserNotFoundException when user not found based 
	 * upon the email field of params. 0 is used as the id value to verify that the params 
	 * value is being used and not the id value.
	 */
	@Test
	public void findByCriteriaByEmailNotFoundTest() {
		params.setEmail("t@t.com");
		doReturn(Optional.ofNullable(null)).when(systemUserRepo).findByEmail("t@t.com");
		assertThrows(SystemUserNotFoundException.class, () -> {
			systemUserService.findByCriteria(params, 0);
		});
	}
	
	/**
	 * This test is verifying that the findByCriteria method is searching by a single
	 * parameter, and using a specified order of id, username, email, userid of the user 
	 * sending the request. All values other than the id field of params have been set to 
	 * invalid values to prove this order. 
	 */
	@Test
	public void findByCriteriaOrderIdSuccessTest() {
		params.setId(1);
		params.setUsername("test");
		params.setEmail("t@t.com");
		
		doReturn(Optional.of(user)).when(systemUserRepo).findById(1);
		
		SystemUser testUser = null;
		
		try {
			testUser = systemUserService.findByCriteria(params, 0);
		} catch (BadParameterException | SystemUserNotFoundException e) {
			fail();
		}
		
		assertEquals(user, testUser);
	}
	
	/**
	 * Test to verify the order of precedence of the search parameters. The id field
	 * is omitted to promt the use of username, and all other values have been given
	 * invalid arguments to verify the username is being used. 
	 */
	@Test
	public void findByCriteriaOrderUsernameSuccessTest() {
		params.setUsername("TTester");
		params.setEmail("t@t.com");
		
		doReturn(Optional.of(user)).when(systemUserRepo).findByUsername("TTester");
		
		SystemUser testUser = null;
		
		try {
			testUser = systemUserService.findByCriteria(params, 0);
		} catch (BadParameterException | SystemUserNotFoundException e) {
			fail();
		}
		
		assertEquals(user, testUser);
	}
	
	/**
	 * Test to verify successful insert of a new system user, which also includes successful insert of
	 * credentials. 
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws InsertFailedException
	 */
	@Test
	public void saveSuccessTest() throws NoSuchAlgorithmException, InvalidKeySpecException, InsertFailedException {
		
		doReturn(user2).when(systemUserRepo).save(any(SystemUser.class));
		doReturn(true).when(credentialsService).save(anyString(), any(SystemUser.class));
		
		SystemUser testUser = systemUserService.save(dto);
		
		assertEquals(user2, testUser);		
	}
	
	/**
	 * Test to verify InsertFailedException if the credentials insert portion fails. 
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws InsertFailedException
	 */
	@Test
	public void saveFailureTest() throws NoSuchAlgorithmException, InvalidKeySpecException, InsertFailedException {
		doReturn(user2).when(systemUserRepo).save(any(SystemUser.class));
		doReturn(false).when(credentialsService).save(anyString(), any(SystemUser.class));
		
		assertThrows(InsertFailedException.class, () -> {
			systemUserService.save(dto);
		});
	}
	
	/**
	 * Test to verify user data is updated when the update method is used. 
	 */
	@Test
	public void updateSuccessTest() {
		dto.setId(2);
		dto.setFirstName("TestChange");
		dto.setLastName("TestChange");
		dto.setEmail("test@change.com");
		
		SystemUser testUser = new SystemUser(dto.getFirstName(), dto.getFirstName(), dto.getEmail(), dto.getUsername());
		SystemUser updatedUser = null;
		doReturn(Optional.of(user2)).when(systemUserRepo).findById(2);
		try {
			doReturn(true).when(credentialsService).update(anyString(), anyInt());
			doReturn(testUser).when(systemUserRepo).save(any(SystemUser.class));
			updatedUser = systemUserService.update(dto);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException | SystemUserNotFoundException
				| CredentialsNotFoundException e) {
			fail();
		}
		
		assertEquals(testUser,updatedUser);
		
	}
	
	/**
	 * Test to verify SystemUserNotFoundException is thrown when no user is found during update. 
	 */
	@Test
	public void updateNotFoundTest() {
		dto.setId(3);
	
		doReturn(Optional.ofNullable(null)).when(systemUserRepo).findById(3);
		assertThrows(SystemUserNotFoundException.class, () -> {
			systemUserService.update(dto);
		});		
	}
	
	/**
	 * Test to verify successful deletion of user. 
	 * @throws CredentialsNotFoundException
	 * @throws SystemUserNotFoundException
	 */
	@Test
	public void deleteSuccessTest() throws CredentialsNotFoundException, SystemUserNotFoundException {
		doReturn(Optional.of(user2)).when(systemUserRepo).findById(2);
		doReturn(creds).when(credentialsService).findByUserId(2);
		
		assertTrue(systemUserService.delete(2));
	}
	
	/**
	 * Test to verify SystemUserNotFoundException is thrown when the user is not
	 * found in the repo. 
	 */
	@Test
	public void deleteNotFoundTest() {
		doReturn(Optional.ofNullable(null)).when(systemUserRepo).findById(3);
		
		assertThrows(SystemUserNotFoundException.class, () -> {
			systemUserService.delete(3);
		});
	}
}
