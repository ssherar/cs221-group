package uk.ac.aber.dcs.cs221.n15.Controller;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * A class which has handly little functions which are used across the
 * whole program.

 */
public class Validator {
	/**
	 * A static variable for retrieving Random integers
	 */
	private static Random r = new Random();
	
	/**
	 * Hashes the string passed into it using the MD5 algorithm
	 * 
	 * @param s the string to be hashed
	 * @return the hashed password
	 */
	public static String toMD5(String s) {
		String hashedPassword = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(s.getBytes());
			hashedPassword = new BigInteger(1, md.digest()).toString(16);
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return hashedPassword;
	}
	
	/**
	 * Checks the length of a string
	 * 
	 * @param message the string to check
	 * @param length the min length
	 * @return true if it's less, false otherwise
	 */
	public static boolean checkLength(String message, int length) {
		return (message.length() >= length);
	}
	
	/**
	 * Checks if the two passwords are correct
	 * 
	 * @param password first password
	 * @param confirmPassword second password
	 * @return true/false
	 */
	public static boolean checkPassword(String password, String confirmPassword) {
		return (password.equals(confirmPassword));
	}
	
	/**
	 * Creates a random number between a range
	 * @param low the lower bound
	 * @param high the upper bound
	 * @return the random number
	 */
	public static int rand(int low, int high) {
		return r.nextInt(high - low) + low;
	}
}
