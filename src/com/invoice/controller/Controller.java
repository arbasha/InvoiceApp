package com.invoice.controller;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.invoice.crypt.PasswordService;
import com.invoice.dao.InvoiceDAO;
import com.invoice.entity.AgeCategory;
import com.invoice.entity.Registration;
import com.invoice.entity.Timesheet;
import com.invoice.entity.WeeksTotal;
import com.invoice.event.FormEvent;
import com.invoice.event.LoginEvent;
import com.invoice.event.RegEvent;
import com.invoice.pojo.DefaulterPojo;
import com.invoice.pojo.Employee;
import com.invoice.pojo.LeaveTypeCategory;
import com.invoice.pojo.UserDetailHolder;
import com.invoice.swingutil.MessageHolder;
import com.invoice.util.DateFormatter;

/**
 * @author Arshad
 *
 */
public class Controller {
	private static Logger logger = Logger.getLogger(Controller.class);
	private InvoiceDAO indao = new InvoiceDAO();
	private DateFormatter df = DateFormatter.getInstance();

	public void addEmployee(FormEvent e) {

		AgeCategory ageCategory = null;

		switch (e.getAgeCategory()) {
		case 0:
			ageCategory = AgeCategory.Buffer;
			break;
		case 1:
			ageCategory = AgeCategory.Billed;
			break;
		}

		Employee employee = new Employee(e.getName(), e.getComp(),
				e.getBillability(), ageCategory, e.isUsCitizen(),
				e.getTaxName(), e.getGender());
		indao.addEmployee(employee);

	}

	public List<Employee> getEmployeeList() {
		return indao.getEmployee();
	}

	public void saveToFile(File file) throws IOException {
		indao.saveToFile(file);
	}

	public void loadFromFile(File file) throws IOException {
		indao.loadFromFile(file);
	}

	public void removeEmployee(int index) {
		indao.removeEmployee(index);
	}

	public LoginEvent checkAuthentication(LoginEvent le, MessageHolder msgHolder) {

		Registration reg = indao.checkAuthentication(le.getUserID());

		if (reg != null) {
			if (PasswordService.decrypt(reg.getPassword()).equals(
					le.getPassword().toLowerCase())) {
				if (reg.isApproved()) {
					if (le.getAccessType() == "approver") {
						if (reg.isAccessApprover()) {
							le.setAuthenticated(true);
						} else {
							le.setAuthenticated(false);
							msgHolder
									.setErrorMessage("User dont have the access of Approver");
						}

					} else {
						le.setAuthenticated(true);
					}
					// Set the values to the static
					UserDetailHolder.setUserID(reg.getUserId());
					UserDetailHolder.setBillStatus(reg.getBillStatus());
					UserDetailHolder.setUserName(reg.getUserName());
					UserDetailHolder.setProjectID(reg.getProjectID());
					UserDetailHolder.setRole(reg.getRole());
					UserDetailHolder.setPmName(reg.getPmName());
					String psNo[] = indao.getPSNO(reg);
					UserDetailHolder.setPmPSNO(psNo[0]);
					UserDetailHolder.setApproverPSNO(psNo[1]);

				} else {
					le.setAuthenticated(false);
					msgHolder
							.setErrorMessage("User yet to be Approved by Approver");
				}
			} else {
				le.setAuthenticated(false);
				msgHolder.setErrorMessage("Password is Incorrect");
			}
		} else {
			le.setAuthenticated(false);
			msgHolder.setErrorMessage("User does not exist");
		}

		return le;
	}

	public List<Object> getRegDropDowns() {

		return indao.getRegDropDowns();
	}

	public List<Object> getUserDropDowns() {

		return indao.getUserDropDowns();
	}

	public List<Object> getAllUserIds() {

		return indao.getAllUserIds();
	}

	public String insertinsertRegDetails(RegEvent regEvent,
			MessageHolder msgHolder) {
		Registration reg = new Registration(regEvent.getUserId(),
				regEvent.getPassword(), regEvent.getUserName(),
				regEvent.getPmName(), regEvent.getRole(),
				regEvent.getApprover(), regEvent.getBillStatus(),
				regEvent.isApproved(), false, regEvent.getProjectID());
		return indao.addUser(reg);

	}

	public List<Timesheet> getWeeklyList() {

		return indao.getWeeklyList();
	}

	public List<Registration> getIntialApproverList() {

		return indao.getIntialApproverList();
	}

	public List<Timesheet> getWeekList(String comp) {
		Date startDate = null;
		Date endDate = null;

		startDate = df.parseMMMddyyyy(comp.split("-")[0].trim());
		endDate = df.parseMMMddyyyy(comp.split("-")[1].trim());
		List<Timesheet> timeList = indao.getWeekList(startDate, endDate);

		if (timeList.isEmpty()) {
			for (int i = 0; i < 7; i++) {
				Timesheet t = new Timesheet();
				t.setWeekDate(startDate);
				t.setBillStatus(UserDetailHolder.getBillStatus());
				t.setLeaveType(LeaveTypeCategory.EMPTY);
				t.setUserDatePk(df.formatMMMddyyyy(startDate) + "_"
						+ UserDetailHolder.getUserID());
				t.setCovergeProv(false);
				t.setEditable(true);
				timeList.add(t);
				Calendar c = Calendar.getInstance();
				c.setTime(startDate);
				c.add(Calendar.DATE, 1);
				startDate = c.getTime();
			}
			Timesheet t = new Timesheet();
			t.setUserDatePk("Total:");
			timeList.add(t);
		} else {
			Timesheet t = timeList.get(0);
			Timesheet totalLine = new Timesheet();
			totalLine.setUserDatePk("Total:");
			totalLine.setHrsWorked(t.getWeekTotal().getTotalHrsWorked());
			totalLine.setRegHrs(t.getWeekTotal().getTotalRegHrs());
			totalLine.setOffHrs(t.getWeekTotal().getTotalOffHrs());
			totalLine.setReportedHrs(t.getWeekTotal().getTotalReportedHrs());
			totalLine.setLobHrs(t.getWeekTotal().getTotalLobHrs());
			totalLine.setBillStatus(null);
			totalLine.setLeaveType(null);
			totalLine.setRemarks("");
			timeList.add(totalLine);
		}
		return timeList;
	}

	public String saveWeekList(List<Timesheet> timeList, String weekDates) {
		logger.info("Saving TimeList");
		if (timeList.size() == 0)
			return "No Timesheet found to save, create one before saving";
		int compoffCount = 0;
		int plCount = 0;
		int unplCount = 0;
		int covCount = 0;
		int splCount = 0;
		int holCount = 0;
		StringBuilder sb = new StringBuilder();
		// Store the total in separate table
		Timesheet t = timeList.get(timeList.size() - 1);
		WeeksTotal wt = new WeeksTotal();
		wt.setUniqueID(weekDates.split("-")[0].trim() + "_"
				+ UserDetailHolder.getUserID());
		wt.setWeekStartDate(df.parseMMMddyyyy(weekDates.split("-")[0].trim()));
		wt.setWeekEndDate(df.parseMMMddyyyy(weekDates.split("-")[1].trim()));
		wt.setTotalHrsWorked(t.getHrsWorked());
		wt.setTotalRegHrs(t.getRegHrs());
		wt.setTotalOffHrs(t.getOffHrs());
		wt.setTotalReportedHrs(t.getReportedHrs());
		for (int i = 0; i < timeList.size(); i++) {
			Timesheet leaveTime = timeList.get(i);
			if (leaveTime.getLeaveType() != null) {
				if (!leaveTime.getLeaveType().equals(LeaveTypeCategory.EMPTY)) {
					if (leaveTime.getLeaveType().equals(
							LeaveTypeCategory.CompOff)) {
						compoffCount = compoffCount + 1;
					} else if (leaveTime.getLeaveType().equals(
							LeaveTypeCategory.UnPlanned)) {
						unplCount = unplCount + 1;
					} else if (leaveTime.getLeaveType().equals(
							LeaveTypeCategory.Planned)) {
						plCount = plCount + 1;
					} else if (leaveTime.getLeaveType().equals(
							LeaveTypeCategory.SplDay)) {
						splCount = splCount + 1;
					} else if (leaveTime.getLeaveType().equals(
							LeaveTypeCategory.Holiday)) {
						holCount = holCount + 1;
					}
				}
			}
			if (leaveTime.getRemarks() != null
					&& !leaveTime.getRemarks().equalsIgnoreCase("")) {
				sb.append(leaveTime.getRemarks().replaceAll("[~;]", ""));
				sb.append("~");
			}
			if (leaveTime.isCovergeProv()) {
				covCount = covCount + 1;
			}
		}
		wt.setTotalCompOffCount(compoffCount);
		wt.setTotalUnPlannedCount(unplCount);
		wt.setTotalPlannedCount(plCount);
		wt.setTotalLeaveCount(compoffCount + unplCount + plCount + splCount);
		wt.setTotalSplCount(splCount);
		wt.setConsolidatedRemarks(sb.toString().trim());
		wt.setTotalCovCount(covCount);
		wt.setTotalHolidayCount(holCount);
		wt.setTotalLobHrs(t.getLobHrs());
		return indao.saveWeekList(timeList, wt);

	}

	public String saveAccessList(List<Registration> approveList) {
		return indao.saveAccessList(approveList);

	}

	public String deleteDecline(List<Registration> deletList) {

		return indao.deleteDecline(deletList);

	}

	public Object getTotalWeekList(Calendar fromDate, Calendar toDate, String wd) {
		if (wd.equalsIgnoreCase("weekly")) {
			Calendar[] dailyDates1 = new Calendar[2];
			Calendar[] dailyDates2 = new Calendar[2];
			Calendar[] weeklyDates = new Calendar[2];
			Calendar tempFromDate = (Calendar) fromDate.clone();
			dailyDates1[0] = tempFromDate;

			if (!(fromDate.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY)) {
				while (!(fromDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
						&& toDate.compareTo(fromDate) >= 0) {
					fromDate.add(Calendar.DAY_OF_MONTH, 1);
				}

				dailyDates1[1] = fromDate;
				Calendar tempDate = (Calendar) fromDate.clone();
				tempDate.add(Calendar.DAY_OF_MONTH, 1);

				weeklyDates[0] = tempDate;
			} else {

				weeklyDates[0] = fromDate;

			}
			Calendar tempToDate = (Calendar) toDate.clone();
			dailyDates2[1] = tempToDate;
			if (!(toDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)) {
				while (!(toDate.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY)
						&& toDate.compareTo(fromDate) >= 0) {
					toDate.add(Calendar.DAY_OF_MONTH, -1);
				}
				dailyDates2[0] = toDate;
				Calendar tempDate = (Calendar) toDate.clone();
				tempDate.add(Calendar.DAY_OF_MONTH, -1);
				weeklyDates[1] = tempDate;
			} else {

				weeklyDates[1] = toDate;

			}

			try {
				String msg = indao.getTotalWeekList(dailyDates1, weeklyDates,
						dailyDates2);
				if (!msg.equals("")) {
					return msg;
				}
				List<WeeksTotal> totalWeeklist = indao.getTotalWeekDate();
				if (totalWeeklist != null) {
					WeeksTotal wtNew = new WeeksTotal();
					wtNew.setUniqueID("Total:");
					wtNew.setTotalRegHrs((float) 0);
					wtNew.setTotalOffHrs((float) 0);
					wtNew.setTotalReportedHrs((float) 0);
					wtNew.setTotalHolidayCount(0);
					wtNew.setTotalLeaveCount(0);
					wtNew.setTotalCovCount(0);
					wtNew.setTotalLobHrs((float) 0);
					wtNew.setConsolidatedRemarks("");
					for (int i = 0; i < totalWeeklist.size(); i++) {

						WeeksTotal ts = totalWeeklist.get(i);
						if (ts.getTotalRegHrs() == null)
							ts.setTotalRegHrs((float) 0);
						wtNew.setTotalRegHrs(ts.getTotalRegHrs()
								+ wtNew.getTotalRegHrs());
						if (ts.getTotalOffHrs() == null)
							ts.setTotalOffHrs((float) 0);
						wtNew.setTotalOffHrs(ts.getTotalOffHrs()
								+ wtNew.getTotalOffHrs());
						if (ts.getTotalReportedHrs() == null)
							ts.setTotalReportedHrs((float) 0);
						wtNew.setTotalReportedHrs(ts.getTotalReportedHrs()
								+ wtNew.getTotalReportedHrs());
						wtNew.setTotalLeaveCount(ts.getTotalLeaveCount()
								+ wtNew.getTotalLeaveCount());
						wtNew.setTotalCovCount(ts.getTotalCovCount()
								+ wtNew.getTotalCovCount());
						wtNew.setTotalHolidayCount(ts.getTotalHolidayCount()
								+ wtNew.getTotalHolidayCount());
						if (ts.getTotalLobHrs() == null)
							ts.setTotalLobHrs((float) 0);
						wtNew.setTotalLobHrs(ts.getTotalLobHrs()
								+ wtNew.getTotalLobHrs());

					}
					totalWeeklist.add(wtNew);
				}

				return totalWeeklist;
			} catch (ParseException e) {
				logger.error(UserDetailHolder.getUserID()
						+ " Exception while parsing", e);
			}

			return null;
		} else if (wd.equalsIgnoreCase("default")) {

			String msg = indao.getIndTotalDefaultList(fromDate, toDate, wd);
			if (!msg.equals("")) {
				return msg;
			}
			List<WeeksTotal> totalWeeklist = indao.getTotalWeekDefaultDate();

			addTotalRow(totalWeeklist);

			return totalWeeklist;
		} else {
			String msg = indao.getWeekDailyListDB(fromDate, toDate);
			if (!msg.equals("")) {
				return msg;
			}
			return indao.getWeekDailyList();
		}
	}

	public List<WeeksTotal> getTotalWeekDate() {
		return indao.getTotalWeekDate();
	}

	public List<Timesheet> getWeekDailyList() {

		return indao.getWeekDailyList();
	}

	public Object getAllTotalWeekList(Calendar fromDate, Calendar toDate,
			List<String> listVal, String wd) {
		if (wd.equalsIgnoreCase("weekly")) {
			Calendar[] dailyDates1 = new Calendar[2];
			Calendar[] dailyDates2 = new Calendar[2];
			Calendar[] weeklyDates = new Calendar[2];
			Calendar tempFromDate = (Calendar) fromDate.clone();
			dailyDates1[0] = tempFromDate;

			if (!(fromDate.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY)) {
				while (!(fromDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
						&& toDate.compareTo(fromDate) >= 0) {
					fromDate.add(Calendar.DAY_OF_MONTH, 1);
				}

				dailyDates1[1] = fromDate;
				Calendar tempDate = (Calendar) fromDate.clone();
				tempDate.add(Calendar.DAY_OF_MONTH, 1);

				weeklyDates[0] = tempDate;
			} else {

				weeklyDates[0] = fromDate;

			}
			Calendar tempToDate = (Calendar) toDate.clone();
			dailyDates2[1] = tempToDate;
			if (!(toDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)) {
				while (!(toDate.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY)
						&& toDate.compareTo(fromDate) >= 0) {
					toDate.add(Calendar.DAY_OF_MONTH, -1);
				}
				dailyDates2[0] = toDate;
				Calendar tempDate = (Calendar) toDate.clone();
				tempDate.add(Calendar.DAY_OF_MONTH, -1);
				weeklyDates[1] = tempDate;
			} else {

				weeklyDates[1] = toDate;

			}

			String msg = indao.getAllTotalWeekList(dailyDates1, weeklyDates,
					dailyDates2, listVal, wd);
			if (!msg.equals("")) {
				return msg;
			}
			List<WeeksTotal> totalWeeklist = indao.getTotalWeekDate();
			addTotalRow(totalWeeklist);
			return totalWeeklist;
		} else if (wd.equalsIgnoreCase("default")) {

			String msg = indao.getAllTotalDefaultList(fromDate, toDate,
					listVal, wd);
			if (!msg.equals("")) {
				return msg;
			}
			List<WeeksTotal> totalWeeklist = indao.getTotalWeekDefaultDate();

			addTotalRow(totalWeeklist);

			return totalWeeklist;
		} else {
			String msg = indao.getAllWeekDailyListDB(fromDate, toDate, listVal);
			if (!msg.equals("")) {
				return msg;
			}
			return indao.getWeekDailyList();
		}
	}

	private void addTotalRow(List<WeeksTotal> totalWeeklist) {
		if (totalWeeklist != null) {
			WeeksTotal wtNew = new WeeksTotal();
			wtNew.setUniqueID("Total:");
			wtNew.setTotalRegHrs((float) 0);
			wtNew.setTotalOffHrs((float) 0);
			wtNew.setTotalReportedHrs((float) 0);
			wtNew.setTotalLeaveCount(0);
			wtNew.setTotalHolidayCount(0);
			wtNew.setTotalCovCount(0);
			wtNew.setTotalLobHrs((float) 0);
			wtNew.setConsolidatedRemarks("");
			for (int i = 0; i < totalWeeklist.size(); i++) {

				WeeksTotal ts = totalWeeklist.get(i);
				if (ts.getTotalRegHrs() == null)
					ts.setTotalRegHrs((float) 0);
				wtNew.setTotalRegHrs(ts.getTotalRegHrs()
						+ wtNew.getTotalRegHrs());
				if (ts.getTotalOffHrs() == null)
					ts.setTotalOffHrs((float) 0);
				wtNew.setTotalOffHrs(ts.getTotalOffHrs()
						+ wtNew.getTotalOffHrs());
				if (ts.getTotalReportedHrs() == null)
					ts.setTotalReportedHrs((float) 0);
				wtNew.setTotalReportedHrs(ts.getTotalReportedHrs()
						+ wtNew.getTotalReportedHrs());
				wtNew.setTotalLeaveCount(ts.getTotalLeaveCount()
						+ wtNew.getTotalLeaveCount());
				wtNew.setTotalCovCount(ts.getTotalCovCount()
						+ wtNew.getTotalCovCount());
				wtNew.setTotalHolidayCount(ts.getTotalHolidayCount()
						+ wtNew.getTotalHolidayCount());
				if (ts.getTotalLobHrs() == null)
					ts.setTotalLobHrs((float) 0);
				wtNew.setTotalLobHrs(ts.getTotalLobHrs()
						+ wtNew.getTotalLobHrs());

			}
			totalWeeklist.add(wtNew);
		}

	}

	public String setEnable(Calendar fromDate, Calendar toDate,
			List<String> listVal) {
		String msg = indao.setEnableTimeSheet(fromDate, toDate, listVal);
		return msg;
	}

	public String setDisable(Calendar fromDate, Calendar toDate,
			List<String> listVal) {
		String msg = indao.setDisableTimeSheet(fromDate, toDate, listVal);
		return msg;
	}

	public List<DefaulterPojo> getDefaulterList(Date fromDate, Date toDate) {

		List<DefaulterPojo> list = indao.getDefaulterList(fromDate, toDate);

		if (list.size() > 0) {
			Collections.sort(list, new Comparator<DefaulterPojo>() {
				@Override
				public int compare(final DefaulterPojo object1,
						final DefaulterPojo object2) {
					return object1.getUserName().compareTo(
							object2.getUserName());
				}
			});
		}

		return list;
	}

	public String changePassword(String oldPWDValue, String newPWDValue,
			String conPWDValue) {
		Registration reg = indao.checkAuthentication(UserDetailHolder
				.getUserID());
		if (reg != null) {
			if (!PasswordService.decrypt(reg.getPassword()).equals(
					oldPWDValue.toLowerCase())) {

				return "Old Password is Incorrect";

			} else {
				return indao.changePassword(UserDetailHolder.getUserID(),
						PasswordService.encrypt(newPWDValue.toLowerCase()));

			}
		}
		return "DB down - No user ID found ";
	}

	public List<WeeksTotal> getTotalWeekDefaultDate() {
		return indao.getTotalWeekDefaultDate();
	}

	public String getPassword(String userid) {

		Registration reg = indao.checkAuthentication(userid);

		if (reg != null) {

			String pwd = PasswordService.decrypt(reg.getPassword());

			return pwd;

		}
		return "E: No user ID found, Please try again with valid user ID";
	}

	public String disApproveUser(List<String> listVal) {

		return indao.disApproveUser(listVal);
	}

	public Registration getInfo(String userID) {
		Registration reg = indao.checkAuthentication(userID);
		return reg;

	}

	public String changePersonalInfo(Registration reg) {
		return indao.changePersonalInfo(reg);
	}
}
