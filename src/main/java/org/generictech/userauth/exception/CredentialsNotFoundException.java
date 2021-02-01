package org.generictech.userauth.exception;
/**
 * Exception for when credentials are not found in the database according to the specified 
 * search criteria.
 * @author Jaden Wilson
 * @since 1.0
 *
 */

@SuppressWarnings("serial")
public class CredentialsNotFoundException extends Exception {

	public CredentialsNotFoundException() {
		super("Credentials not found.");
	}
	
	public CredentialsNotFoundException(String message) {
		super(message);
	}
}
