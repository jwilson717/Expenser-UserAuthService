package org.generictech.userauth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class to hold incoming parameters to search for system users.
 * @author Jaden Wilson
 * @since 1.0
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemUserParams {

	private int id;
	private String username;
	private String email;
	
	public boolean empty() {
		boolean empty = true;
		if (id != 0 || username != null || email != null) {
			empty = false;
		}
		
		return empty;
	}
	
	public String toString() {
		StringBuilder toString = new StringBuilder();
		
		if (!empty()) {
			toString.append("?");
		} else {
			return "";
		}
		
		if (id != 0) {
			toString.append("id=" + id);
		}
		if (username != null) {
			toString.append("username=" + username);
		}
		if(email != null) {
			toString.append("email=" + email);
		}
		
		return toString.toString();
	}
}
