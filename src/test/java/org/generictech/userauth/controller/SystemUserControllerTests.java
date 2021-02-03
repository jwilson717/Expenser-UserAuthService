package org.generictech.userauth.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.generictech.userauth.dto.SystemUserDataDTO;
import org.generictech.userauth.dto.SystemUserParams;
import org.generictech.userauth.exception.InsertFailedException;
import org.generictech.userauth.exception.SystemUserNotFoundException;
import org.generictech.userauth.model.SystemUser;
import org.generictech.userauth.service.SystemUserService;
import org.generictech.userauth.util.TokenUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;

/**
 * Test class to test the {@link SystemUserController}
 * @author Jaden Wilson
 * @since 1.0
 */
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = SystemUserController.class)
public class SystemUserControllerTests {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper om;
	
	@MockBean
	private SystemUserService systemUserService;
	
	@MockBean
	private TokenUtility tokenUtility;

	@Mock
	private Claims claims;
	private SystemUser user;
	private SystemUserDataDTO userData;
	
	@BeforeEach
	public void setUp() {
		claims = mock(Claims.class);
		
		user = new SystemUser(1, "Test", "Tester", "test@test.com", "TTester");
		userData = new SystemUserDataDTO();
		userData.setFirstName("Tested");
		userData.setLastName("Testers");
		userData.setEmail("tested@test.com");
		userData.setUsername("TdTesters");
		userData.setPassword("password");
	}
	
	/**
	 * Test to make sure a valid user is returned when no parameters are included in the request.
	 * @throws Exception
	 */
	@Test
	public void getSystemUserWithNoParametersTest() throws Exception {
		doReturn(claims).when(tokenUtility).decodeJWT("testToken");
		doReturn("1").when(claims).getId();
		doReturn(user).when(systemUserService).findByCriteria(new SystemUserParams(), 1);
		mockMvc.perform(get("/systemuser")
				.header("tokenId", "testToken"))
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.content().string(om.writeValueAsString(user)));
	}
	
	/**
	 * Test to verify that a bad parameter exception is thrown if a malformed parameter is passed in. 
	 * @throws Exception
	 */
	@Test
	public void getSystemUserWithBadParametersTest() throws Exception {
		doReturn(claims).when(tokenUtility).decodeJWT("testToken");
		doReturn("1").when(claims).getId();
		doReturn(user).when(systemUserService).findByCriteria(any(SystemUserParams.class), anyInt());
		mockMvc.perform(get("/systemuser?usrname=TTester")
				.header("tokenId", "testToken"))
			.andExpect(status().is(400));
	}
	
	/**
	 * Test to verify the status of 404 if a user is not found in the results of the request. 
	 * @throws Exception
	 */
	@Test
	public void getSystemUserNotFoundTest() throws Exception {
		doReturn(claims).when(tokenUtility).decodeJWT("testToken");
		doReturn("1").when(claims).getId();
		doThrow(new SystemUserNotFoundException()).when(systemUserService).findByCriteria(any(SystemUserParams.class), anyInt());
		mockMvc.perform(get("/systemuser?username=Test")
				.header("tokenId", "testToken"))
			.andExpect(status().is(404));
		
		SystemUserParams params = new SystemUserParams();
		params.setUsername("Test");
		doThrow(new SystemUserNotFoundException()).when(systemUserService).findByCriteria(params, 1);
		mockMvc.perform(get("/systemuser?username=Test")
				.header("tokenId", "testToken"))
			.andExpect(status().is(404));
	}
	
	/**
	 * Test to verify that the correct user is found and returned if the username parameter matches.
	 * @throws Exception
	 */
	@Test
	public void getSystemUserByUsernameTest() throws Exception {
		SystemUserParams params = new SystemUserParams();
		params.setUsername("TTester");
		doReturn(claims).when(tokenUtility).decodeJWT("testToken");
		doReturn("1").when(claims).getId();
		doReturn(user).when(systemUserService).findByCriteria(params, 1);
		mockMvc.perform(get("/systemuser?username=TTester")
				.header("tokenId", "testToken"))
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.content().string(om.writeValueAsString(user)));
	}
	
	/**
	 * Test to verify that the correct user is returned with an OK status if the email parameter matches.
	 * @throws Exception
	 */
	@Test
	public void getSystemUserByEmailTest() throws Exception {
		SystemUserParams params = new SystemUserParams();
		params.setEmail("test@test.com");
		doReturn(claims).when(tokenUtility).decodeJWT("testToken");
		doReturn("1").when(claims).getId();
		doReturn(user).when(systemUserService).findByCriteria(params, 1);
		mockMvc.perform(get("/systemuser?email=test@test.com")
				.header("tokenId", "testToken"))
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.content().string(om.writeValueAsString(user)));
	}
	
	
	/**
	 * Test to verify that the correct user is returned with an OK status if the id parameter matches.
	 * @throws Exception
	 */
	@Test
	public void getSystemUserByIdTest() throws Exception {
		SystemUserParams params = new SystemUserParams();
		params.setId(1);
		doReturn(claims).when(tokenUtility).decodeJWT("testToken");
		doReturn("1").when(claims).getId();
		doReturn(user).when(systemUserService).findByCriteria(params, 1);
		mockMvc.perform(get("/systemuser?id=1")
				.header("tokenId", "testToken"))
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.content().string(om.writeValueAsString(user)));
	}
	
	/**
	 * Test to verify successful return of data and OK status upon creation of new system user.
	 * @throws Exception
	 */
	@Test
	public void postSystemUserSuccessTest() throws Exception {
		SystemUser user2 = new SystemUser(2, userData.getFirstName(), userData.getLastName()
				, userData.getEmail(), userData.getUsername());
		when(systemUserService.save(any(SystemUserDataDTO.class))).thenReturn(user2);
		
		mockMvc.perform(post("/systemuser").contentType(MediaType.APPLICATION_JSON)
				.content(om.writeValueAsString(userData))
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andExpect(MockMvcResultMatchers.content().string(om.writeValueAsString(user2)));
	}
	
	/**
	 * Test to verify status code 409 returned upon failure to insert. 
	 * @throws Exception
	 */
	@Test
	public void postSystemUserFailureTest() throws Exception {
		doThrow(new InsertFailedException()).when(systemUserService).save(any(SystemUserDataDTO.class));
		
		mockMvc.perform(post("/systemuser").contentType(MediaType.APPLICATION_JSON)
				.content(om.writeValueAsString(userData))
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isConflict());
	}
	
	/**
	 * Test to verify status code 200 on update of user
	 * @throws Exception
	 */
	@Test
	public void putSystemUserSuccessTest() throws Exception {
		userData.setUsername("TestUsername");
		SystemUser user2 = new SystemUser(2, userData.getFirstName(), userData.getLastName()
				, userData.getEmail(), userData.getUsername());
		doReturn(user2).when(systemUserService).update(any(SystemUserDataDTO.class));
		mockMvc.perform(put("/systemuser/2").contentType(MediaType.APPLICATION_JSON)
				.content(om.writeValueAsString(userData))
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.content().string(containsString("TestUsername")));
	}
	
	/**
	 * Test to verify status code 404 on update of user if user id not found
	 * @throws Exception
	 */
	@Test
	public void putSystemUserNotFoundTest() throws Exception {
		userData.setUsername("TestUsername");
		doThrow(new SystemUserNotFoundException()).when(systemUserService).update(any(SystemUserDataDTO.class));
		mockMvc.perform(put("/systemuser/2").contentType(MediaType.APPLICATION_JSON)
				.content(om.writeValueAsString(userData))
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(MockMvcResultMatchers.content().string(containsString("SystemUserNotFoundException")));
	}
	
	/**
	 * Test to verify status code 204 on deleting a user.
	 * @throws Exception
	 */
	@Test
	public void deleteSystemUserSuccessTest() throws Exception {
		doReturn(true).when(systemUserService).delete(2);
		mockMvc.perform(delete("/systemuser/2").contentType(MediaType.APPLICATION_JSON)
				.content(om.writeValueAsString(userData))
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}
	
	/**
	 * Test to verify status code 404 on deleting a user when the user is not found.
	 * @throws Exception
	 */
	@Test
	public void deleteSystemUserFailureTest() throws Exception {
		doThrow(new SystemUserNotFoundException()).when(systemUserService).delete(3);
		mockMvc.perform(delete("/systemuser/3").contentType(MediaType.APPLICATION_JSON)
				.content(om.writeValueAsString(userData))
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound());
	}
}
