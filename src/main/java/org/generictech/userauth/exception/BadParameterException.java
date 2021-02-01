package org.generictech.userauth.exception;

/**
 * Exception class for ill formed path parameters.
 * @author Jaden Wilspn
 *
 */
@SuppressWarnings("serial")
public class BadParameterException extends Exception {

	public BadParameterException() {
		super("Parameters do not match expected patterns");
	}
	
	public BadParameterException(String message) {
		super(message);
	}
}
