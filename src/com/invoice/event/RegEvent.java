package com.invoice.event;

import java.util.EventObject;

import com.invoice.pojo.BillCategory;

public class RegEvent extends EventObject {

	private static final long serialVersionUID = 5578351828721961924L;
	private String userId;
	private String password;
	private String confirmPassword;
	private String userName;
	private String pmName;
	private String role;
	private String approver;
	private BillCategory billStatus;
	private boolean approved;
	private String projectID;

	public RegEvent(Object source) {
		super(source);
	}

	public RegEvent(Object arg0, String userId, String password,
			String confirmPassword, String userName, String pmName,
			String role, String approver, BillCategory billStatus,
			boolean approved, String projectID) {
		super(arg0);
		this.userId = userId;
		this.password = password;
		this.confirmPassword = confirmPassword;
		this.userName = userName;
		this.pmName = pmName;
		this.role = role;
		this.approver = approver;
		this.billStatus = billStatus;
		this.approved = approved;
		this.projectID = projectID;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPmName() {
		return pmName;
	}

	public void setPmName(String pmName) {
		this.pmName = pmName;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getApprover() {
		return approver;
	}

	public void setApprover(String approver) {
		this.approver = approver;
	}

	public BillCategory getBillStatus() {
		return billStatus;
	}

	public void setBillStatus(BillCategory billStatus) {
		this.billStatus = billStatus;
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	public String getProjectID() {
		return projectID;
	}

	public void setProjectID(String projectID) {
		this.projectID = projectID;
	}

}
