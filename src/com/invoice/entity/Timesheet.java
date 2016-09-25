package com.invoice.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.invoice.pojo.BillCategory;
import com.invoice.pojo.LeaveTypeCategory;

/**
 * @author Arshad
 *
 */
@Entity
@Table(name = "TIMESHEET_DATA")
public class Timesheet {
	@Id
	@Column(name = "DATE_USERID")
	private String userDatePk;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private TimesheetUser tsu;

	@ManyToOne(cascade = CascadeType.ALL)
	private WeeksTotal weekTotal;

	@Temporal(TemporalType.DATE)
	private Date weekDate;

	@Column(name = "HOURS_WORKED")
	private Float hrsWorked;

	@Column(name = "REGULAR_HOURS")
	private Float regHrs;

	@Column(name = "OFF_HOURS")
	private Float offHrs;

	@Column(name = "BILL_TYPE")
	@Enumerated(EnumType.STRING)
	private BillCategory billStatus;

	@Column(name = "LEAVE_TYPE")
	@Enumerated(EnumType.STRING)
	private LeaveTypeCategory leaveType;

	@Column(name = "REMARKS")
	private String remarks;

	@Column(name = "REPORTED_HOURS")
	private Float reportedHrs;

	@Column(name = "LOB_HOURS")
	private Float lobHrs;

	@Column(name = "Editable")
	private boolean editable;

	@Column(name = "Coverage_Provided")
	private boolean covergeProv;

	public Timesheet() {
		super();
	}

	public Timesheet(String userDatePk, TimesheetUser tsu,
			WeeksTotal weekTotal, Date weekDate, Float hrsWorked, Float regHrs,
			Float offHrs, BillCategory billStatus, LeaveTypeCategory leaveType,
			String remarks, Float reportedHrs, Float lobHrs, boolean editable,
			boolean covergeProv) {
		super();
		this.userDatePk = userDatePk;
		this.tsu = tsu;
		this.weekTotal = weekTotal;
		this.weekDate = weekDate;
		this.hrsWorked = hrsWorked;
		this.regHrs = regHrs;
		this.offHrs = offHrs;
		this.billStatus = billStatus;
		this.leaveType = leaveType;
		this.remarks = remarks;
		this.reportedHrs = reportedHrs;
		this.lobHrs = lobHrs;
		this.editable = editable;
		this.covergeProv = covergeProv;
	}

	public String getUserDatePk() {
		return userDatePk;
	}

	public void setUserDatePk(String userDatePk) {
		this.userDatePk = userDatePk;
	}

	public TimesheetUser getTsu() {
		return tsu;
	}

	public void setTsu(TimesheetUser tsu) {
		this.tsu = tsu;
	}

	public WeeksTotal getWeekTotal() {
		return weekTotal;
	}

	public void setWeekTotal(WeeksTotal weekTotal) {
		this.weekTotal = weekTotal;
	}

	public Date getWeekDate() {
		return weekDate;
	}

	public void setWeekDate(Date weekDate) {
		this.weekDate = weekDate;
	}

	public Float getHrsWorked() {
		return hrsWorked;
	}

	public void setHrsWorked(Float hrsWorked) {
		this.hrsWorked = hrsWorked;
	}

	public Float getRegHrs() {
		return regHrs;
	}

	public void setRegHrs(Float regHrs) {
		this.regHrs = regHrs;
	}

	public Float getOffHrs() {
		return offHrs;
	}

	public void setOffHrs(Float offHrs) {
		this.offHrs = offHrs;
	}

	public BillCategory getBillStatus() {
		return billStatus;
	}

	public void setBillStatus(BillCategory billStatus) {
		this.billStatus = billStatus;
	}

	public LeaveTypeCategory getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(LeaveTypeCategory leaveType) {
		this.leaveType = leaveType;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Float getReportedHrs() {
		return reportedHrs;
	}

	public void setReportedHrs(Float reportedHrs) {
		this.reportedHrs = reportedHrs;
	}

	public Float getLobHrs() {
		return lobHrs;
	}

	public void setLobHrs(Float lobHrs) {
		this.lobHrs = lobHrs;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public boolean isCovergeProv() {
		return covergeProv;
	}

	public void setCovergeProv(boolean covergeProv) {
		this.covergeProv = covergeProv;
	}

}
