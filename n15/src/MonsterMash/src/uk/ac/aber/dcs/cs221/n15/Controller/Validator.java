package uk.ac.aber.dcs.cs221.n15.Controller;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Validator {
	private static Random r = new Random();
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
	
	public static boolean checkLength(String message, int length) {
		return (message.length() >= length);
	}
	
	public static boolean checkPassword(String password, String confirmPassword) {
		return (password.equals(confirmPassword));
	}
	
	public static int rand(int low, int high) {
		return r.nextInt(high - low) + low;
	}
}
