package org.generictech.userauth.dto;

/**
 * DTO class to hold incoming user data for login
 * @author Jaden Wilson
 * @since 1.0
 */

public class CredentialsDTO {

	String username;
	String password;
	
	public CredentialsDTO() {
		super();
	}

	public CredentialsDTO(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "CredentialsDTO [username=" + username + ", password=" + password + "]";
	}
}
