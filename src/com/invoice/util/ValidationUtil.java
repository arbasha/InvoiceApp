package com.invoice.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Arshad
 *
 */
public class ValidationUtil {

	private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";
	private static final String NUMBER_PATTERN = "^[0-9]+$";
	private static final String ALPHA_PATTERN = "^\\p{Alpha}+$";
	// private static final String ALPHA_DOT_SPACE = "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$";
	private static final String ALPHANUMERIC_DOT_SPACE = "^[a-zA-Z0-9_\\. ]+$";

	public boolean isEmpty(String text) {

		return text.equalsIgnoreCase("");

	}

	public boolean isOnlyNumber(String text) {
		if (text != "" && text != null) {
			Pattern pattern = Pattern.compile(NUMBER_PATTERN);

			Matcher matcher = pattern.matcher(text);

			return matcher.matches();

		}
		return false;
	}

	public boolean isOnlyAlphabets(String text) {
		if (text != "" && text != null) {
			Pattern pattern = Pattern.compile(ALPHA_PATTERN);
			Matcher matcher = pattern.matcher(text);

			return matcher.matches();

		}
		return false;
	}

	public boolean validateUserName(String text) {
		if (text != "" && text != null) {
			Pattern pattern = Pattern.compile(ALPHANUMERIC_DOT_SPACE);
			Matcher matcher = pattern.matcher(text);

			return matcher.matches();

		}
		return false;
	}

	public boolean validatePassword(String text) {
		if (text != "" && text != null) {
			Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
			Matcher matcher = pattern.matcher(text);

			return matcher.matches();

		}
		return false;
	}

}
