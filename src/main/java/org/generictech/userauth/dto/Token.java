package org.generictech.userauth.dto;

/**
 * DTO class to hold incoming token data
 * @author Jaden Wilson
 * @since 1.0
 */
public class Token {

	private String token;

	public Token() {
		super();
	}

	public Token(String token) {
		super();
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
}
