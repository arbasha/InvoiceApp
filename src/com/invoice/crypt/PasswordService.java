package com.invoice.crypt;

import java.math.BigInteger;

/**
 * @author Arshad
 *
 */
public class PasswordService {
	private static int firstKey = 5;
	private static int secondKey = 19;
	private static int module = 26;

	public PasswordService() {

	}

	public static String encrypt(String input) {
		StringBuilder builder = new StringBuilder();
		for (int in = 0; in < input.length(); in++) {
			char character = input.charAt(in);
			if (Character.isLetter(character)) {
				character = (char) ((firstKey * (character - 'a') + secondKey)
						% module + 'a');
			}
			builder.append(character);
		}
		return builder.toString();
	}

	public static String decrypt(String input) {
		StringBuilder builder = new StringBuilder();
		// compute firstKey^-1 aka "modular inverse"
		BigInteger inverse = BigInteger.valueOf(firstKey).modInverse(
				BigInteger.valueOf(module));
		// perform actual decryption
		for (int in = 0; in < input.length(); in++) {
			char character = input.charAt(in);
			if (Character.isLetter(character)) {
				int decoded = inverse.intValue()
						* (character - 'a' - secondKey + module);
				character = (char) (decoded % module + 'a');
			}
			builder.append(character);
		}
		return builder.toString();
	}
}