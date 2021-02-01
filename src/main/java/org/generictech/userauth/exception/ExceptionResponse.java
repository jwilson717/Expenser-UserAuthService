package org.generictech.userauth.exception;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class to handle Http responses when an exception occurs. 
 * @author Jaden Wilson
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponse {

	private Date timestamp;
	private int status;
	private String error, message;
}
