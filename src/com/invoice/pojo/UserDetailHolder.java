package com.invoice.pojo;


/**
 * @author Arshad
 *
 */
public class UserDetailHolder {

	public static String userID;
	public static BillCategory billStatus;
	public static String userName;
	public static String projectID;
	public static String role;
	public static String pmName;
	public static String pmPSNO;
	public static String approverPSNO;

	public static String getPmPSNO() {
		return pmPSNO;
	}

	public static void setPmPSNO(String pmPSNO) {
		UserDetailHolder.pmPSNO = pmPSNO;
	}

	public static String getApproverPSNO() {
		return approverPSNO;
	}

	public static void setApproverPSNO(String approverPSNO) {
		UserDetailHolder.approverPSNO = approverPSNO;
	}

	public static String getPmName() {
		return pmName;
	}

	public static void setPmName(String pmName) {
		UserDetailHolder.pmName = pmName;
	}

	public static String getRole() {
		return role;
	}

	public static void setRole(String role) {
		UserDetailHolder.role = role;
	}

	public static String getProjectID() {
		return projectID;
	}

	public static void setProjectID(String projectID) {
		UserDetailHolder.projectID = projectID;
	}

	public static String getUserID() {
		return userID;
	}

	public static void setUserID(String userID) {
		UserDetailHolder.userID = userID;
	}

	public static BillCategory getBillStatus() {
		return billStatus;
	}

	public static void setBillStatus(BillCategory billStatus) {
		UserDetailHolder.billStatus = billStatus;
	}

	public static String getUserName() {
		return userName;
	}

	public static void setUserName(String userName) {
		UserDetailHolder.userName = userName;
	}

}
