package com.invoice.pojo;

/**
 * @author Arshad
 *
 */
public class DefaulterPojo {

	private String userName;
	private String weekDate;
	private String projectID;
	private String userID;

	public DefaulterPojo() {
		super();
	}

	public DefaulterPojo(String userName, String weekDate, String projectID,
			String userID) {
		super();
		this.userName = userName;
		this.weekDate = weekDate;
		this.projectID = projectID;
		this.userID = userID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getWeekDate() {
		return weekDate;
	}

	public void setWeekDate(String weekDate) {
		this.weekDate = weekDate;
	}

	public String getProjectID() {
		return projectID;
	}

	public void setProjectID(String projectID) {
		this.projectID = projectID;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

}
