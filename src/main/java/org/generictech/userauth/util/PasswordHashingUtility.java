package org.generictech.userauth.util;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.springframework.stereotype.Component;

/**
 * Utility class to handle password hashing.
 * @author Jaden Wilson
 * @since 1.0
 */

@Component
public class PasswordHashingUtility {
	/**
	 * Method to generate a hashed password and salt to go with the hash. 
	 * @param passwd String version of password to be hashed
	 * @param salt String value for the salt to be used with the password.
	 * @return String 
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException 
	 */
	public String generateHash(String passwd, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
		KeySpec spec = new PBEKeySpec(passwd.toCharArray(), salt, 131072, 256);
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		byte[] hash = factory.generateSecret(spec).getEncoded();
		
		return getString(hash);
	}
	
	/**
	 * Method to validate entered password
	 * @param password String value of password to be checked
	 * @param hashedPassword Hashed password for user
	 * @param salt String value for the hash used for the users password
	 * @return boolean value stating whether the user is authenticated or not. 
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException 
	 */
	public boolean validatePassword(String password, String hashedPassword, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
		String hashedSet = generateHash(password, salt.getBytes(StandardCharsets.UTF_8));
		return hashedPassword.equals(hashedSet);
	}
	
	/**
	 * Method to get a random salt value to be used when hashing passwords. 
	 * @return String salt value
	 */
	public String getSalt() {
		SecureRandom rand = new SecureRandom();
		byte[] salt = new byte[16];
		rand.nextBytes(salt);
		return getString(salt);
	}
	
	/**
	 * Helper method to convert byte[] values used in hashing to strings for simpler database storage. 
	 * @param in byte[] value to be converted
	 * @return String version of entered byte[].
	 */
	private String getString(byte[] in) {
		StringBuilder sb = new StringBuilder();
		for (byte b : in) {
			sb.append(String.format("%02x", b));
		}
		
		return sb.toString();
	}
}
