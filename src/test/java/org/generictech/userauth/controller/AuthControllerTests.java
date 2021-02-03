package org.generictech.userauth.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.generictech.userauth.dto.CredentialsDTO;
import org.generictech.userauth.dto.SystemUserDTO;
import org.generictech.userauth.exception.BadParameterException;
import org.generictech.userauth.exception.InvalidTokenException;
import org.generictech.userauth.exception.SystemUserNotFoundException;
import org.generictech.userauth.service.AuthService;
import org.generictech.userauth.util.TokenUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Test class for AuthController unit tests
 * @author Jaden Wilson
 * @since 1.0
 */
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = AuthController.class)
public class AuthControllerTests {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper om;
	
	@MockBean
	private AuthService authService;
	
	@MockBean
	private TokenUtility tokenUtility;
	
	private SystemUserDTO user;
	private CredentialsDTO creds;
	
	@BeforeEach
	public void setUp() {
		user = new SystemUserDTO(1, "TTester", "test@test.com");
		creds = new CredentialsDTO("TTester", "password");
	}
	
	/**
	 * Test to verify 200 status code, and user content, and token header are all present.
	 * @throws JsonProcessingException
	 * @throws Exception
	 */
	@Test
	public void loginSuccessTest() throws JsonProcessingException, Exception {
		doReturn(user).when(authService).login(any(CredentialsDTO.class));
		doReturn("token").when(tokenUtility).createJWT(String.valueOf(user.getId()), "org.generictech.Expenser", user.getUsername(), 20000000);
		mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON)
				.content(om.writeValueAsString(creds))
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.content().string(om.writeValueAsString(user)))
			.andExpect(MockMvcResultMatchers.header().exists("tokenId"));
	}
	
	/**
	 * Test to verify 404 if user or credentials not found. 
	 * @throws JsonProcessingException
	 * @throws Exception
	 */
	@Test
	public void loginFailureTest() throws JsonProcessingException, Exception {
		doThrow(new SystemUserNotFoundException()).when(authService).login(any(CredentialsDTO.class));
		doReturn("token").when(tokenUtility).createJWT(String.valueOf(user.getId()), "org.generictech.Expenser", user.getUsername(), 20000000);
		mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON)
				.content(om.writeValueAsString(creds))
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(MockMvcResultMatchers.content().string(containsString("SystemUserNotFoundException")));
	}
	
	/**
	 * Test to verify 400 if password is wrong. 
	 * @throws JsonProcessingException
	 * @throws Exception
	 */
	@Test
	public void loginPasswordFailureTest() throws JsonProcessingException, Exception {
		doReturn(null).when(authService).login(any(CredentialsDTO.class));
		doReturn("token").when(tokenUtility).createJWT(String.valueOf(user.getId()), "org.generictech.Expenser", user.getUsername(), 20000000);
		mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON)
				.content(om.writeValueAsString(creds))
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());
	}
	
	/**
	 * Test to verify 200 status code if token is valid. 
	 * @throws Exception
	 */
	@Test
	public void validateTokenSuccessTest() throws Exception {
		doReturn(user).when(authService).validateToken(anyString());
		
		mockMvc.perform(post("/validate").contentType(MediaType.APPLICATION_JSON)
				.content("{\"token\": " + "\"testToken\"}")
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(MockMvcResultMatchers.content().string(om.writeValueAsString(user)));
	}
	
	/**
	 * Test to verify 401 status code and exception return when token is invalid.
	 * @throws Exception
	 */
	@Test
	public void validateTokenFalureTest() throws Exception {
		doThrow(new InvalidTokenException()).when(authService).validateToken(anyString());
		
		mockMvc.perform(post("/validate").contentType(MediaType.APPLICATION_JSON)
				.content("{\"token\": " + "\"testToken\"}")
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isUnauthorized())
		.andExpect(MockMvcResultMatchers.content().string(containsString("InvalidTokenException")));
	}
	
	/**
	 * Test to verify 400 status code and BadParameterException if no token is provided.
	 * @throws Exception
	 */
	@Test
	public void validateTokenMissingTokenTest() throws Exception {
		doThrow(new BadParameterException()).when(authService).validateToken(isNull());
		
		mockMvc.perform(post("/validate").contentType(MediaType.APPLICATION_JSON)
				.content("{ }")
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest())
		.andExpect(MockMvcResultMatchers.content().string(containsString("BadParameterException")));
	}
	
	/**
	 * Test to verify 404 status code when user is not found from token id. 
	 * @throws Exception
	 */
	@Test
	public void validateTokenUserNoLongerExistsTest() throws Exception {
		doThrow(new SystemUserNotFoundException()).when(authService).validateToken(anyString());
		
		mockMvc.perform(post("/validate").contentType(MediaType.APPLICATION_JSON)
				.content("{\"token\": \"tokenId\" }")
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound())
		.andExpect(MockMvcResultMatchers.content().string(containsString("SystemUserNotFoundException")));
	}
	
}
