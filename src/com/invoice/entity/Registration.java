package com.invoice.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.invoice.pojo.BillCategory;

/**
 * @author Arshad
 *
 */
@Entity
@Table(name = "REGISTER")
public class Registration {

	@Id
	private String userId;

	@Column(name = "Password")
	private String password;

	@Column(name = "User_Name")
	private String userName;

	@Column(name = "PM_Name")
	private String pmName;

	@Column(name = "Role")
	private String role;

	@Column(name = "Approver")
	private String approver;

	@Column(name = "Bill_Status")
	@Enumerated(EnumType.STRING)
	private BillCategory billStatus;

	@Column(name = "Approved")
	private boolean approved;

	@Column(name = "Approving_Access")
	private boolean accessApprover;

	@Column(name = "Project_ID")
	private String projectID;

	@Transient
	private String userType;

	public Registration() {
		super();

	}

	public Registration(String userId, String password, String userName,
			String pmName, String role, String approver,
			BillCategory billStatus, boolean approved, boolean accessApprover,
			String projectID) {
		super();
		this.userId = userId;
		this.password = password;
		this.userName = userName;
		this.pmName = pmName;
		this.role = role;
		this.approver = approver;
		this.billStatus = billStatus;
		this.approved = approved;
		this.accessApprover = accessApprover;
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

	public boolean isAccessApprover() {
		return accessApprover;
	}

	public void setAccessApprover(boolean accessApprover) {
		this.accessApprover = accessApprover;
	}

	public String getProjectID() {
		return projectID;
	}

	public void setProjectID(String projectID) {
		this.projectID = projectID;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

}
