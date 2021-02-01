package org.generictech.userauth.dto;

/**
 * DTO class to hold minimal systemUser data to be returned to the client. 
 * @author Jaden Wilson
 * @since 1.0
 */

public class SystemUserDTO {

	int id;
	String username;
	String email;
	
	public SystemUserDTO() {
		super();
	}

	public SystemUserDTO(int id, String username, String email) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "SystemUserDTO [id=" + id + ", username=" + username + ", email=" + email + "]";
	}
	
}
