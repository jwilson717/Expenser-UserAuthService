package org.generictech.userauth.service;

import static org.mockito.Mockito.doReturn;
import static org.junit.jupiter.api.Assertions.*;

import org.generictech.userauth.exception.CredentialsNotFoundException;
import org.generictech.userauth.model.Credentials;
import org.generictech.userauth.model.SystemUser;
import org.generictech.userauth.repo.CredentialsRepo;
import org.generictech.userauth.util.PasswordHashingUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

/**
 * Test class to test the methods in the CredentialsService class
 * @author Jaden Wilson
 * @since 1.0
 */
@ExtendWith(MockitoExtension.class)
public class CredentialsServiceTests {

	@Mock
	private CredentialsRepo credentialsRepo;
	
	@Mock
	private PasswordHashingUtility hashingUtility;
	
	@InjectMocks
	private CredentialsService credentialsService;
	
	private Credentials creds;
	private SystemUser user;
	private Credentials creds2;
//	private SystemUser user2;
	
	@BeforeEach
	public void setUp() {
		user = new SystemUser(1, "Test", "Tester", "test@test.com", "TTester");
		creds = new Credentials(1, "password", "salt", user);
//		user2 = new SystemUser(2, "Tested", "Testers", "t@t.com", "TdTesters");
		creds2 = new Credentials(3, "password2", "salt2", user);
	}
	
	/**
	 * Test to verify the successful return of a credentials object when 
	 * searching by ID value.
	 */
	@Test
	public void findByIdSuccessTest() {
		doReturn(Optional.of(creds)).when(credentialsRepo).findById(1);
		
		Credentials testCreds = null;
		try {
			testCreds = credentialsService.findById(1);			
		} catch (CredentialsNotFoundException e) {
			fail();
		}
		
		assertEquals(creds, testCreds);
	}
	
	/**
	 * Test to verify CredentialsNotFoundException is thrown when no credentials
	 * are returned from the repo. 
	 */
	@Test
	public void findByIdNotFoundTest() {
		doReturn(Optional.ofNullable(null)).when(credentialsRepo).findById(2);
		
		assertThrows(CredentialsNotFoundException.class, () -> {
			credentialsService.findById(2);
		});
	}
	
	/**
	 * Test to verify successful return of credentials when searching by the ID
	 * of the associated user. A second assert is included to verify that the 
	 * query still works when the id value of the user and the credentials do not match.
	 */
	@Test
	public void findByUserIdSuccessTest() {
		doReturn(Optional.of(creds)).when(credentialsRepo).findCredentials(1);
		
		Credentials testCreds = null;
		try {
			testCreds = credentialsService.findByUserId(1);			
		} catch (CredentialsNotFoundException e) {
			fail();
		}
		
		assertEquals(creds, testCreds);
		
		doReturn(Optional.of(creds2)).when(credentialsRepo).findCredentials(2);
		try {
			testCreds = credentialsService.findByUserId(2);			
		} catch (CredentialsNotFoundException e) {
			fail();
		}
		
		assertEquals(creds2, testCreds);
	}
	
	/**
	 * Test to verify that CredentialsNotFoundException is thrown when no credentials
	 * are returned from the repo when searching by the id of the associated user. 
	 */
	@Test
	public void findByUserIdNotFound() {
		doReturn(Optional.ofNullable(null)).when(credentialsRepo).findCredentials(3);
		
		assertThrows(CredentialsNotFoundException.class, () -> {
			credentialsService.findByUserId(3);
		});
	}
}
