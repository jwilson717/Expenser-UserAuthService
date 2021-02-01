package org.generictech.userauth.exception;

@SuppressWarnings("serial")
public class InvalidTokenException extends Exception {

	public InvalidTokenException() {
		super("Token given was invalid");
	}
	
	public InvalidTokenException(String message) {
		super(message);
	}
}
