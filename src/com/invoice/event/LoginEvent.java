package com.invoice.event;

import java.util.EventObject;

/**
 * @author Arshad
 *
 */
public class LoginEvent extends EventObject {

	private static final long serialVersionUID = -2724057610948138647L;
	private String userID;
	private String password;
	private String role;
	private String accessType;
	private boolean authenticated;

	public String getAccessType() {
		return accessType;
	}

	public LoginEvent(Object source, String userID, String password) {
		super(source);
		this.userID = userID;
		this.password = password;

	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean isAuthenticated() {
		return authenticated;
	}

	public void setAuthenticated(boolean authenticated) {
		this.authenticated = authenticated;
	}

	public void setAccessType(String accessType) {

		this.accessType = accessType;

	}

}
