package com.invoice.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.invoice.pojo.BillCategory;

/**
 * @author Arshad
 *
 */
@Entity
@Table(name = "TIMESHEET_USERS")
public class TimesheetUser {
	@Id
	private Long id;

	@OneToMany(mappedBy = "tsu", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Timesheet> timeSheet;

	@Column(name = "USER_ID")
	private String userId;

	@Column(name = "USER_NAME")
	private String userName;

	public List<Timesheet> getTimeSheet() {
		return timeSheet;
	}

	public void setTimeSheet(List<Timesheet> timeSheet) {
		this.timeSheet = timeSheet;
	}

	@Column(name = "BILL_TYPE")
	@Enumerated(EnumType.STRING)
	private BillCategory billStatus;

	@Column(name = "PROJECT_ID")
	private String projectID;

	@Column(name = "PM_NAME")
	private String pmName;

	public String getPmName() {
		return pmName;
	}

	public void setPmName(String pmName) {
		this.pmName = pmName;
	}

	public TimesheetUser(Long id, String userId, String userName,
			BillCategory billStatus, String projectID, String pmName) {
		super();
		this.id = id;
		this.userId = userId;
		this.userName = userName;
		this.billStatus = billStatus;
		this.projectID = projectID;
		this.pmName = pmName;
	}

	public TimesheetUser() {
		super();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Long getId() {
		return id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public BillCategory getBillStatus() {
		return billStatus;
	}

	public void setBillStatus(BillCategory billStatus) {
		this.billStatus = billStatus;
	}

	public String getProjectID() {
		return projectID;
	}

	public void setProjectID(String projectID) {
		this.projectID = projectID;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
