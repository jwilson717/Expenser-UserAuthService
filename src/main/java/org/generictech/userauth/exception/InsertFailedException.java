package org.generictech.userauth.exception;

/**
 * Exception class to handle failed inserts. 
 * @author Jaden Wilson
 * @since 1.0
 *
 */
@SuppressWarnings("serial")
public class InsertFailedException extends Exception {

	public InsertFailedException() {
		super("Entity save failed");
	}

	public InsertFailedException(String message) {
		super(message);
	}

	
}
