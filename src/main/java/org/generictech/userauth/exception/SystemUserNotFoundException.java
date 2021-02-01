package org.generictech.userauth.exception;

/**
 * Exception for when no system_user is found in the database according to the criteria specified.
 * @author Jaden Wilson
 * @since 1.0
 */

@SuppressWarnings("serial")
public class SystemUserNotFoundException extends Exception {
	
	public SystemUserNotFoundException() {
		super("System User Not Found");
	}

	public SystemUserNotFoundException(String message) {
		super(message);
	}
}
