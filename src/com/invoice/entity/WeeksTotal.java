package com.invoice.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author Arshad
 *
 */
@Entity
@Table(name = "WEEKS_TOTAL")
public class WeeksTotal {

	@Id
	@Column(name = "Unique_ID")
	private String uniqueID;

	@Temporal(TemporalType.DATE)
	private Date weekStartDate;

	@Temporal(TemporalType.DATE)
	private Date weekEndDate;

	@Column(name = "TOTAL_HOURS_WORKED")
	private Float totalHrsWorked;

	@Column(name = "TOTAL_REGULAR_HOURS")
	private Float totalRegHrs;

	@Column(name = "TOTAL_OFF_HOURS")
	private Float totalOffHrs;

	@Column(name = "TOTAL_REPORTED_HOURS")
	private Float totalReportedHrs;

	@Column(name = "TOTAL_LEAVE_COUNT")
	private Integer totalLeaveCount;

	@Column(name = "COMPOFF_COUNT")
	private Integer totalCompOffCount;

	@Column(name = "PLANNED_LEAVE_COUNT")
	private Integer totalPlannedCount;

	@Column(name = "UNPLANNED_LEAVE_COUNT")
	private Integer totalUnPlannedCount;

	@Column(name = "SPECIAL_LEAVE_COUNT")
	private Integer totalSplCount;

	@Column(name = "CONSOLIDATED_REMARKS")
	private String consolidatedRemarks;

	@OneToOne(fetch = FetchType.EAGER)
	private Timesheet timeSheet;

	@Column(name = "COV_COUNT")
	private Integer totalCovCount;

	@Column(name = "HOLIDAY_COUNT")
	private Integer totalHolidayCount;
	
	@Column(name = "LOB_HOURS")
	private Float totalLobHrs;

	public WeeksTotal() {
		super();
	}

	public WeeksTotal(String uniqueID, Date weekStartDate, Date weekEndDate,
			Float totalHrsWorked, Float totalRegHrs, Float totalOffHrs,
			Float totalReportedHrs, Integer totalLeaveCount,
			Integer totalCompOffCount, Integer totalPlannedCount,
			Integer totalUnPlannedCount, Integer totalSplCount,
			String consolidatedRemarks, Timesheet timeSheet,
			Integer totalCovCount, Integer totalHolidayCount, Float totalLobHrs) {
		super();
		this.uniqueID = uniqueID;
		this.weekStartDate = weekStartDate;
		this.weekEndDate = weekEndDate;
		this.totalHrsWorked = totalHrsWorked;
		this.totalRegHrs = totalRegHrs;
		this.totalOffHrs = totalOffHrs;
		this.totalReportedHrs = totalReportedHrs;
		this.totalLeaveCount = totalLeaveCount;
		this.totalCompOffCount = totalCompOffCount;
		this.totalPlannedCount = totalPlannedCount;
		this.totalUnPlannedCount = totalUnPlannedCount;
		this.totalSplCount = totalSplCount;
		this.consolidatedRemarks = consolidatedRemarks;
		this.timeSheet = timeSheet;
		this.totalCovCount = totalCovCount;
		this.totalHolidayCount = totalHolidayCount;
		this.totalLobHrs = totalLobHrs;
	}

	public String getUniqueID() {
		return uniqueID;
	}

	public void setUniqueID(String uniqueID) {
		this.uniqueID = uniqueID;
	}

	public Date getWeekStartDate() {
		return weekStartDate;
	}

	public void setWeekStartDate(Date weekStartDate) {
		this.weekStartDate = weekStartDate;
	}

	public Date getWeekEndDate() {
		return weekEndDate;
	}

	public void setWeekEndDate(Date weekEndDate) {
		this.weekEndDate = weekEndDate;
	}

	public Float getTotalHrsWorked() {
		return totalHrsWorked;
	}

	public void setTotalHrsWorked(Float totalHrsWorked) {
		this.totalHrsWorked = totalHrsWorked;
	}

	public Float getTotalRegHrs() {
		return totalRegHrs;
	}

	public void setTotalRegHrs(Float totalRegHrs) {
		this.totalRegHrs = totalRegHrs;
	}

	public Float getTotalOffHrs() {
		return totalOffHrs;
	}

	public void setTotalOffHrs(Float totalOffHrs) {
		this.totalOffHrs = totalOffHrs;
	}

	public Float getTotalReportedHrs() {
		return totalReportedHrs;
	}

	public void setTotalReportedHrs(Float totalReportedHrs) {
		this.totalReportedHrs = totalReportedHrs;
	}

	public Integer getTotalLeaveCount() {
		return totalLeaveCount;
	}

	public void setTotalLeaveCount(Integer totalLeaveCount) {
		this.totalLeaveCount = totalLeaveCount;
	}

	public Integer getTotalCompOffCount() {
		return totalCompOffCount;
	}

	public void setTotalCompOffCount(Integer totalCompOffCount) {
		this.totalCompOffCount = totalCompOffCount;
	}

	public Integer getTotalPlannedCount() {
		return totalPlannedCount;
	}

	public void setTotalPlannedCount(Integer totalPlannedCount) {
		this.totalPlannedCount = totalPlannedCount;
	}

	public Integer getTotalUnPlannedCount() {
		return totalUnPlannedCount;
	}

	public void setTotalUnPlannedCount(Integer totalUnPlannedCount) {
		this.totalUnPlannedCount = totalUnPlannedCount;
	}

	public Integer getTotalSplCount() {
		return totalSplCount;
	}

	public void setTotalSplCount(Integer totalSplCount) {
		this.totalSplCount = totalSplCount;
	}

	public String getConsolidatedRemarks() {
		return consolidatedRemarks;
	}

	public void setConsolidatedRemarks(String consolidatedRemarks) {
		this.consolidatedRemarks = consolidatedRemarks;
	}

	public Timesheet getTimeSheet() {
		return timeSheet;
	}

	public void setTimeSheet(Timesheet timeSheet) {
		this.timeSheet = timeSheet;
	}

	public Integer getTotalCovCount() {
		return totalCovCount;
	}

	public void setTotalCovCount(Integer totalCovCount) {
		this.totalCovCount = totalCovCount;
	}

	public Integer getTotalHolidayCount() {
		return totalHolidayCount;
	}

	public void setTotalHolidayCount(Integer totalHolidayCount) {
		this.totalHolidayCount = totalHolidayCount;
	}

	public Float getTotalLobHrs() {
		return totalLobHrs;
	}

	public void setTotalLobHrs(Float totalLobHrs) {
		this.totalLobHrs = totalLobHrs;
	}

	
}
