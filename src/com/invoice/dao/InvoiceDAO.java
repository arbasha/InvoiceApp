package com.invoice.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.invoice.entity.Approver;
import com.invoice.entity.PmNames;
import com.invoice.entity.ProjectNames;
import com.invoice.entity.Registration;
import com.invoice.entity.Timesheet;
import com.invoice.entity.TimesheetUser;
import com.invoice.entity.WeeksTotal;
import com.invoice.pojo.DefaulterPojo;
import com.invoice.pojo.Employee;
import com.invoice.pojo.LeaveTypeCategory;
import com.invoice.pojo.UserDetailHolder;
import com.invoice.util.DateFormatter;
import com.invoice.util.HibernateUtil;

public class InvoiceDAO {
	static Logger logger = Logger.getLogger(InvoiceDAO.class);

	private List<Employee> emplist;
	private List<Timesheet> timelist;
	private List<Registration> approveList;
	private List<WeeksTotal> totalWeeklist;
	private List<WeeksTotal> totalAllDefaultlist;
	private List<Timesheet> weekDailylist;
	private DateFormatter df = DateFormatter.getInstance();

	public InvoiceDAO() {
		InvoiceDAO.tieSystemOutAndErrToLog();
		emplist = new LinkedList<Employee>();
		timelist = new LinkedList<Timesheet>();
		approveList = new LinkedList<Registration>();
		totalWeeklist = new LinkedList<WeeksTotal>();
		totalAllDefaultlist = new LinkedList<WeeksTotal>();
		weekDailylist = new LinkedList<Timesheet>();
	}

	public List<Employee> getEmployee() {
		return emplist;
	}

	public void addEmployee(Employee employee) {
		emplist.add(employee);
	}

	public void removeEmployee(int index) {
		emplist.remove(index);
	}

	public void saveToFile(File file) throws IOException {
		FileOutputStream fos = new FileOutputStream(file);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		Employee[] emp = emplist.toArray(new Employee[emplist.size()]);
		oos.writeObject(emp);
		oos.close();
	}

	public void loadFromFile(File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		ObjectInputStream ois = new ObjectInputStream(fis);
		try {
			Employee[] emp = (Employee[]) ois.readObject();
			emplist.clear();
			emplist.addAll(Arrays.asList(emp));

		} catch (ClassNotFoundException e) {
			logger.error("Class NOT Found", e);
		}
		ois.close();
	}

	public Registration checkAuthentication(String userID) {
		Registration reg = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			reg = (Registration) session.get(Registration.class, userID);
			transaction.commit();
		} catch (HibernateException e) {
			transaction.rollback();
			logger.error("Hibernate Exception", e);
		} finally {
			session.close();
		}
		return reg;
	}

	@SuppressWarnings("unchecked")
	public List<Object> getRegDropDowns() {
		List<PmNames> pmNames = null;
		List<Approver> appNames = null;
		List<ProjectNames> projectNames = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			Query query2 = session.createQuery("from PmNames");
			pmNames = query2.list();
			Query query1 = session.createQuery("from Approver");
			appNames = query1.list();

			Query query3 = session.createQuery("from ProjectNames");
			projectNames = query3.list();

			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			transaction.rollback();
			logger.error("Hibernate Exception", e);
		} finally {
			session.close();
		}
		List<Object> dropContain = new LinkedList<Object>();
		dropContain.add(pmNames);
		dropContain.add(appNames);
		dropContain.add(projectNames);
		return dropContain;

	}

	public String addUser(Registration reg) {
		Registration regId = null;
		String message = "S: Registration Successful...Login with your credentials";
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			regId = (Registration) session.get(Registration.class,
					reg.getUserId());
			if (regId == null) {

				session.save(reg);

			} else {
				message = "P.S NO already Exist";
			}

			transaction.commit();
		} catch (HibernateException e) {
			transaction.rollback();
			logger.error("Hibernate Exception", e);
		} finally {
			session.close();
		}
		return message;

	}

	public List<Timesheet> getWeeklyList() {

		return timelist;
	}

	@SuppressWarnings("unchecked")
	public List<Timesheet> getWeekList(Date startDate, Date endDate) {

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			Query query4 = session
					.createQuery("from Timesheet p where p.weekDate between :startDate and :endDate and p.tsu.userId =:userId order by p.weekDate");
			query4.setParameter("userId", UserDetailHolder.getUserID());
			query4.setParameter("startDate", startDate);
			query4.setParameter("endDate", endDate);
			timelist = query4.list();
			transaction.commit();
		} catch (HibernateException e) {
			transaction.rollback();
			logger.error("Hibernate Exception", e);
		} finally {
			session.close();
		}
		return timelist;
	}

	public String saveWeekList(List<Timesheet> timelist, WeeksTotal wt) {
		String msg;
		TimesheetUser tuser;
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			tuser = getTuser(session, UserDetailHolder.getUserID());

			if (tuser == null) {
				Query query = session
						.createQuery("select max(id) from TimesheetUser");
				Long count = (Long) query.uniqueResult();
				if (count == null) {
					count = (long) 0;
				} else {
					count = count + 1;
				}
				TimesheetUser temp = (TimesheetUser) session.get(
						TimesheetUser.class, count);
				while (temp != null) {
					count = count + 1;
					temp = (TimesheetUser) session.get(TimesheetUser.class,
							count);
				}
				tuser = new TimesheetUser(count, UserDetailHolder.getUserID(),
						UserDetailHolder.getUserName(),
						UserDetailHolder.getBillStatus(),
						UserDetailHolder.getProjectID(),
						UserDetailHolder.getPmName());

				session.save(tuser);
			}

			for (int i = 0; i < timelist.size() - 1; i++) {
				Timesheet t = timelist.get(i);
				t.setTsu(tuser);
				t.setWeekTotal(wt);
				session.merge(t);
			}
			wt.setTimeSheet(timelist.get(0));
			session.merge(wt);
			this.timelist = timelist;
			transaction.commit();
			msg = "S: Timesheet data saved succesfully";
		} catch (HibernateException e) {
			transaction.rollback();
			msg = "Save Incomplete - " + "Contact IT team";
			logger.error("Hibernate Exception" + msg, e);
		} finally {
			session.close();
		}
		return msg;
	}

	@SuppressWarnings("unchecked")
	public List<Registration> getIntialApproverList() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		TimesheetUser tuser = null;
		try {
			transaction = session.beginTransaction();
			Query query4 = session
					.createQuery("from Registration p where p.approver =:userName and p.approved =:flag");
			query4.setParameter("userName", UserDetailHolder.getUserName());
			query4.setParameter("flag", false);
			approveList = query4.list();
			for (Registration reg : approveList) {

				tuser = getTuser(session, reg.getUserId());
				if (tuser == null) {
					reg.setUserType("New User");
				} else {
					reg.setUserType("Existing User");
				}
			}

			transaction.commit();
		} catch (HibernateException e) {
			transaction.rollback();
			logger.error("Hibernate Exception", e);
		} finally {
			session.close();
		}

		return approveList;
	}

	public String saveAccessList(List<Registration> approveList) {

		String msg;
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();

			for (int i = 0; i < approveList.size(); i++) {
				Registration reg = approveList.get(i);
				session.saveOrUpdate(reg);
			}
			transaction.commit();
			this.approveList = approveList;
			msg = "S: Access list saved succesfully";
		} catch (HibernateException e) {
			transaction.rollback();
			msg = "Save Incomplete - " + "Contact IT team";
			logger.error("Hibernate Exception", e);
		} finally {
			session.close();
		}
		return msg;
	}

	public String deleteDecline(List<Registration> deletList) {

		String msg;
		Session session = HibernateUtil.getSessionFactory().openSession();
		TimesheetUser tuser = null;
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();

			for (int i = 0; i < deletList.size(); i++) {
				Registration reg = deletList.get(i);
				tuser = getTuser(session, reg.getUserId());
				session.delete(tuser);
				session.delete(reg);

			}
			transaction.commit();
			msg = "S: Decline Successful";
		} catch (HibernateException e) {
			transaction.rollback();
			msg = "Decline Incomplete - " + "Contact IT team";
			logger.error("Hibernate Exception", e);
		} finally {
			session.close();
		}
		return msg;
	}

	@SuppressWarnings("unchecked")
	public String getTotalWeekList(Calendar[] dailyDates1,
			Calendar[] weeklyDates, Calendar[] dailyDates2)
			throws ParseException {
		totalWeeklist = new LinkedList<WeeksTotal>();
		String msg = "";
		List<Timesheet> tempTimelist1 = null;
		List<Timesheet> tempTimelist2 = null;
		List<WeeksTotal> weeklyDateslist = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		try {

			transaction = session.beginTransaction();
			if (dailyDates1[0] != null && dailyDates1[1] != null
					&& dailyDates2[0] != null && dailyDates2[1] != null) {

				if (/*
					 * !(dailyDates2[0].compareTo(dailyDates2[1]) == 0) &&
					 */!(weeklyDates[1].compareTo(weeklyDates[0]) < 0)) {

					tempTimelist1 = getDailyDates1(dailyDates1, session);
					weeklyDateslist = getWeeklyDatesList(weeklyDates, session);
					tempTimelist2 = getDailyDates2(dailyDates2, session);

				} else {

					tempTimelist1 = getDailyDates1(dailyDates1, session);
					tempTimelist2 = getDailyDates2(dailyDates2, session);
				}

			} else if (dailyDates1[0] != null && dailyDates1[1] == null
					&& dailyDates2[0] != null && dailyDates2[1] != null) {
				if (dailyDates1[0].compareTo(dailyDates2[0]) == 0
						&& dailyDates2[0].compareTo(weeklyDates[0]) == 0) {

					tempTimelist2 = getDailyDates2(dailyDates2, session);

				} else {

					weeklyDateslist = getWeeklyDatesList(weeklyDates, session);
					tempTimelist2 = getDailyDates2(dailyDates2, session);
				}
			} else if (dailyDates1[0] != null && dailyDates1[1] != null
					&& dailyDates2[0] == null && dailyDates2[1] != null) {
				if (dailyDates1[1].compareTo(dailyDates2[1]) == 0
						&& dailyDates2[1].compareTo(weeklyDates[1]) == 0) {

					tempTimelist1 = getDailyDates1(dailyDates1, session);

				} else {

					tempTimelist1 = getDailyDates1(dailyDates1, session);
					weeklyDateslist = getWeeklyDatesList(weeklyDates, session);
				}
			}

			else if (dailyDates1[1] == null && dailyDates2[0] == null) {

				Query query = session
						.createQuery("from WeeksTotal p where p.weekStartDate >=:weekStartDate and p.weekEndDate <= :weekEndDate and p.timeSheet.tsu.userId =:userId order by p.weekStartDate");
				query.setParameter("weekStartDate", df.parseMMddYYYY(df
						.formatMMddYYYY(weeklyDates[0].getTime())));
				query.setParameter("weekEndDate", df.parseMMddYYYY(df
						.formatMMddYYYY(weeklyDates[1].getTime())));
				query.setParameter("userId", UserDetailHolder.getUserID());
				weeklyDateslist = query.list();

			}

			transaction.commit();
		} catch (HibernateException e) {
			transaction.rollback();
			msg = "Error Occured while fetching data";
			logger.error("Hibernate Exception", e);
		} finally {
			session.close();
		}
		msg = "No records found for selected dates";
		if (tempTimelist1 != null && !tempTimelist1.isEmpty()) {
			calculateDailyTotalList(tempTimelist1, dailyDates1);
			msg = "";
		}
		if (weeklyDateslist != null && !weeklyDateslist.isEmpty()) {

			totalWeeklist.addAll(weeklyDateslist);
			msg = "";
		}
		if (tempTimelist2 != null && !tempTimelist2.isEmpty()) {
			calculateDailyTotalList(tempTimelist2, dailyDates2);
			msg = "";
		}
		return msg;
	}

	private void calculateDailyTotalList(List<Timesheet> tempTimelist,
			Calendar[] dailyDates) {
		Timesheet ts = null;
		int compoffCount = 0;
		int plCount = 0;
		int unplCount = 0;
		int covCount = 0;
		int splCount = 0;
		int holCount = 0;
		StringBuilder sb = new StringBuilder();
		WeeksTotal wt = new WeeksTotal();
		wt.setWeekStartDate(dailyDates[0].getTime());
		wt.setWeekEndDate(dailyDates[1].getTime());

		// Initialize total field
		wt.setTotalRegHrs((float) 0);
		wt.setTotalOffHrs((float) 0);
		wt.setTotalReportedHrs((float) 0);
		wt.setTotalLeaveCount(0);
		wt.setTotalCovCount(0);
		wt.setTotalHolidayCount(0);
		wt.setConsolidatedRemarks("");
		wt.setTotalLobHrs((float) 0);

		for (int i = 0; i < tempTimelist.size(); i++) {
			ts = tempTimelist.get(i);
			wt.setTotalRegHrs(getvalue(ts.getRegHrs()) + wt.getTotalRegHrs());
			wt.setTotalOffHrs(getvalue(ts.getOffHrs()) + wt.getTotalOffHrs());
			wt.setTotalReportedHrs(getvalue(ts.getReportedHrs())
					+ wt.getTotalReportedHrs());
			if (ts.getLeaveType() != null) {
				if (!ts.getLeaveType().equals(LeaveTypeCategory.EMPTY)) {
					if (ts.getLeaveType().equals(LeaveTypeCategory.CompOff)) {
						compoffCount = compoffCount + 1;
					} else if (ts.getLeaveType().equals(
							LeaveTypeCategory.UnPlanned)) {
						unplCount = unplCount + 1;
					} else if (ts.getLeaveType().equals(
							LeaveTypeCategory.Planned)) {
						plCount = plCount + 1;
					} else if (ts.getLeaveType().equals(
							LeaveTypeCategory.SplDay)) {
						splCount = splCount + 1;
					} else if (ts.getLeaveType().equals(
							LeaveTypeCategory.Holiday)) {
						holCount = holCount + 1;
					}
				}
			}
			if (ts.getRemarks() != null
					&& !ts.getRemarks().equalsIgnoreCase("")) {
				sb.append(ts.getRemarks().replaceAll("~", ""));
				sb.append("~");
			}
			if (ts.isCovergeProv()) {
				covCount = covCount + 1;
			}
			wt.setTotalLeaveCount(compoffCount + unplCount + plCount + splCount);
			wt.setTotalCovCount(covCount);
			wt.setTotalHolidayCount(holCount);
			wt.setConsolidatedRemarks(sb.toString().trim());
			if (ts.getLobHrs() == null)
				ts.setLobHrs((float) 0);
			wt.setTotalLobHrs(wt.getTotalLobHrs() + ts.getLobHrs());
		}
		wt.setTimeSheet(ts);
		totalWeeklist.add(wt);
	}

	private void calculateAllDailyTotalList(List<Timesheet> tempTimelist,
			Calendar[] dailyDates, String viewSelected) {
		Timesheet ts = null;
		int compoffCount = 0;
		int plCount = 0;
		int unplCount = 0;
		int covCount = 0;
		int splCount = 0;
		int holCount = 0;
		StringBuilder sb = null;
		WeeksTotal wt = null;
		String prevName = "";
		for (int i = 0; i < tempTimelist.size(); i++) {
			ts = tempTimelist.get(i);
			if (prevName.equalsIgnoreCase("")
					|| !prevName.equalsIgnoreCase(ts.getTsu().getUserName())) {
				prevName = ts.getTsu().getUserName();
				if (wt != null) {
					if (viewSelected.equalsIgnoreCase("weekly")) {
						totalWeeklist.add(wt);
					} else if (viewSelected.equalsIgnoreCase("default")) {
						totalAllDefaultlist.add(wt);
					}
				}
				compoffCount = 0;
				plCount = 0;
				unplCount = 0;
				covCount = 0;
				holCount = 0;
				sb = new StringBuilder();
				wt = new WeeksTotal();
				wt.setWeekStartDate(dailyDates[0].getTime());
				wt.setWeekEndDate(dailyDates[1].getTime());

				// Initialize total field
				wt.setTotalRegHrs((float) 0);
				wt.setTotalOffHrs((float) 0);
				wt.setTotalReportedHrs((float) 0);
				wt.setTotalLeaveCount(0);
				wt.setTotalHolidayCount(0);
				wt.setTotalCovCount(0);
				wt.setConsolidatedRemarks("");
				wt.setTotalLobHrs((float) 0);
			}
			wt.setTotalRegHrs(getvalue(ts.getRegHrs()) + wt.getTotalRegHrs());
			wt.setTotalOffHrs(getvalue(ts.getOffHrs()) + wt.getTotalOffHrs());
			wt.setTotalReportedHrs(getvalue(ts.getReportedHrs())
					+ wt.getTotalReportedHrs());
			if (ts.getLeaveType() != null) {
				if (!ts.getLeaveType().equals(LeaveTypeCategory.EMPTY)) {
					if (ts.getLeaveType().equals(LeaveTypeCategory.CompOff)) {
						compoffCount = compoffCount + 1;
					} else if (ts.getLeaveType().equals(
							LeaveTypeCategory.UnPlanned)) {
						unplCount = unplCount + 1;
					} else if (ts.getLeaveType().equals(
							LeaveTypeCategory.Planned)) {
						plCount = plCount + 1;
					} else if (ts.getLeaveType().equals(
							LeaveTypeCategory.SplDay)) {
						splCount = splCount + 1;
					} else if (ts.getLeaveType().equals(
							LeaveTypeCategory.Holiday)) {
						holCount = holCount + 1;
					}
				}
			}
			if (ts.getRemarks() != null
					&& !ts.getRemarks().equalsIgnoreCase("")) {
				sb.append(ts.getRemarks().replaceAll("~", ""));
				sb.append("~");
			}
			if (ts.isCovergeProv()) {
				covCount = covCount + 1;
			}
			wt.setTotalLeaveCount(compoffCount + unplCount + plCount);
			wt.setTotalCovCount(covCount);
			wt.setTotalHolidayCount(holCount);
			wt.setConsolidatedRemarks(sb.toString().trim());
			if (ts.getLobHrs() == null)
				ts.setLobHrs((float) 0);
			wt.setTotalLobHrs(wt.getTotalLobHrs() + ts.getLobHrs());
			wt.setTimeSheet(ts);
		}
		if (wt != null) {
			if (viewSelected.equalsIgnoreCase("weekly")) {
				totalWeeklist.add(wt);
			} else if (viewSelected.equalsIgnoreCase("default")) {
				totalAllDefaultlist.add(wt);
			}
		}

	}

	private Float getvalue(Float value) {
		if (value == null)
			return (float) 0;

		return value;
	}

	@SuppressWarnings("unchecked")
	private List<Timesheet> getDailyDates2(Calendar[] dailyDates2,
			Session session) {
		Query query = session
				.createQuery("from Timesheet p where p.weekDate between :startDate and :endDate and p.tsu.userId =:userId order by p.weekDate");
		query.setParameter("startDate",
				df.parseMMddYYYY(df.formatMMddYYYY(dailyDates2[0].getTime())));
		query.setParameter("endDate",
				df.parseMMddYYYY(df.formatMMddYYYY(dailyDates2[1].getTime())));
		query.setParameter("userId", UserDetailHolder.getUserID());

		return query.list();
	}

	@SuppressWarnings("unchecked")
	private List<WeeksTotal> getWeeklyDatesList(Calendar[] weeklyDates,
			Session session) {
		Query query = session
				.createQuery("from WeeksTotal p where p.weekStartDate >=:weekStartDate and p.weekEndDate <= :weekEndDate and p.timeSheet.tsu.userId =:userId order by p.weekStartDate");
		query.setParameter("weekStartDate",
				df.parseMMddYYYY(df.formatMMddYYYY(weeklyDates[0].getTime())));
		query.setParameter("weekEndDate",
				df.parseMMddYYYY(df.formatMMddYYYY(weeklyDates[1].getTime())));
		query.setParameter("userId", UserDetailHolder.getUserID());
		return query.list();
	}

	@SuppressWarnings("unchecked")
	private List<Timesheet> getDailyDates1(Calendar[] dailyDates1,
			Session session) {
		Query query = session
				.createQuery("from Timesheet p where p.weekDate between :startDate and :endDate and p.tsu.userId =:userId order by p.weekDate");
		query.setParameter("startDate",
				df.parseMMddYYYY(df.formatMMddYYYY(dailyDates1[0].getTime())));
		query.setParameter("endDate",
				df.parseMMddYYYY(df.formatMMddYYYY(dailyDates1[1].getTime())));
		query.setParameter("userId", UserDetailHolder.getUserID());

		return query.list();
	}

	public List<WeeksTotal> getTotalWeekDate() {
		return totalWeeklist;

	}

	public List<Timesheet> getWeekDailyList() {

		return weekDailylist;
	}

	@SuppressWarnings("unchecked")
	public String getWeekDailyListDB(Calendar fromDate, Calendar toDate) {
		String msg = "";
		List<Object[]> result = null;
		List<Long> resultCount = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		weekDailylist = new LinkedList<Timesheet>();
		try {
			transaction = session.beginTransaction();

			Query query = session
					.createQuery("from Timesheet p where p.weekDate between :startDate and :endDate and p.tsu.userId =:userId order by p.weekDate");
			query.setParameter("userId", UserDetailHolder.getUserID());
			query.setParameter("startDate",
					df.parseMMddYYYY(df.formatMMddYYYY(fromDate.getTime())));
			query.setParameter("endDate",
					df.parseMMddYYYY(df.formatMMddYYYY(toDate.getTime())));
			weekDailylist = query.list();

			Query query1 = session
					.createQuery("select sum(p.hrsWorked), sum(p.regHrs) ,sum(p.offHrs),sum(p.reportedHrs),sum(p.lobHrs) from Timesheet p where p.weekDate between :startDate and :endDate and p.tsu.userId =:userId");
			query1.setParameter("userId", UserDetailHolder.getUserID());
			query1.setParameter("startDate",
					df.parseMMddYYYY(df.formatMMddYYYY(fromDate.getTime())));
			query1.setParameter("endDate",
					df.parseMMddYYYY(df.formatMMddYYYY(toDate.getTime())));
			result = query1.list();

			Query query2 = session
					.createQuery("select count(p.covergeProv) from Timesheet p where p.weekDate between :startDate and :endDate and p.tsu.userId =:userId and p.covergeProv =:flag");
			query2.setParameter("userId", UserDetailHolder.getUserID());
			query2.setParameter("startDate",
					df.parseMMddYYYY(df.formatMMddYYYY(fromDate.getTime())));
			query2.setParameter("endDate",
					df.parseMMddYYYY(df.formatMMddYYYY(toDate.getTime())));
			query2.setParameter("flag", true);
			resultCount = query2.list();

			transaction.commit();
		} catch (HibernateException e) {
			msg = "Error occured while fetching records, contact IT team";
			transaction.rollback();
			logger.error("Hibernate Exception", e);
		} finally {
			session.close();
		}
		msg = "No records found for the selection made";
		if (!weekDailylist.isEmpty()) {
			Timesheet t = new Timesheet();
			t.setUserDatePk("Total: ~" + calculateLeaveCount(weekDailylist));
			if (result != null) {
				t.setHrsWorked(doubleToFloat((Double) result.get(0)[0]));
				t.setRegHrs(doubleToFloat((Double) result.get(0)[1]));
				t.setOffHrs(doubleToFloat((Double) result.get(0)[2]));
				t.setReportedHrs(doubleToFloat((Double) result.get(0)[3]));
				t.setLobHrs(doubleToFloat((Double) result.get(0)[4]));
			}
			if (resultCount != null) {

				t.setUserDatePk(t.getUserDatePk() + "~" + resultCount.get(0));
			}
			weekDailylist.add(t);
			msg = "";
		}
		return msg;

	}

	private Integer calculateLeaveCount(List<Timesheet> weekDailylist2) {
		Integer total = 0;
		for (int i = 0; i < weekDailylist2.size(); i++) {
			Timesheet ts = weekDailylist2.get(i);

			if (ts.getLeaveType() != null) {
				if (!ts.getLeaveType().equals(LeaveTypeCategory.EMPTY)
						&& !ts.getLeaveType().equals(LeaveTypeCategory.Holiday)) {
					total = total + 1;

				}
			}

		}
		return total;

	}

	private Float doubleToFloat(Double object) {
		if (object == null)
			return (float) 0;
		float f = object.floatValue();
		return f;
	}

	@SuppressWarnings("unchecked")
	public List<Object> getAllUserIds() {

		List<Object> userIds = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();

			Query query4 = session
					.createQuery("Select r.userId from Registration r where r.pmName =:pmName and r.approved =:flag order by r.userName");
			query4.setParameter("pmName", "Sriram R");
			query4.setParameter("flag", true);
			userIds = query4.list();

			transaction.commit();
		} catch (HibernateException e) {
			transaction.rollback();
			logger.error("Hibernate Exception", e);
		} finally {
			session.close();
		}
		return userIds;
	}

	@SuppressWarnings("unchecked")
	public List<Object> getUserDropDowns() {

		List<Object> userNames = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();

			Query query4 = session
					.createQuery("Select r.userName from Registration r where r.pmName =:pmName and r.approved =:flag order by r.userName");
			query4.setParameter("pmName", "Sriram R");
			query4.setParameter("flag", true);
			userNames = query4.list();

			transaction.commit();
		} catch (HibernateException e) {
			transaction.rollback();
			logger.error("Hibernate Exception", e);
		} finally {
			session.close();
		}
		return userNames;
	}

	public String getAllTotalWeekList(Calendar[] dailyDates1,
			Calendar[] weeklyDates, Calendar[] dailyDates2,
			List<String> listVal, String viewSelected) {
		totalWeeklist = new LinkedList<WeeksTotal>();
		String msg = "";
		Boolean all = false;
		List<Timesheet> tempTimelist1 = null;
		List<Timesheet> tempTimelist2 = null;
		List<WeeksTotal> weeklyDateslist = null;
		List<String> projectList = null;
		List<String> namesList = null;
		for (int i = 0; i < listVal.size(); i++) {
			if (listVal.get(i).equalsIgnoreCase("All")) {
				all = true;
			} else if (listVal.get(i).equalsIgnoreCase("E5091")
					|| listVal.get(i).equalsIgnoreCase("E3199")
					|| listVal.get(i).equalsIgnoreCase("E3187")) {
				if (projectList == null) {
					projectList = new ArrayList<String>();
				}
				projectList.add(listVal.get(i));
			} else {
				if (namesList == null) {
					namesList = new ArrayList<String>();
				}
				namesList.add(listVal.get(i));
			}

		}

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		try {

			transaction = session.beginTransaction();
			if (dailyDates1[0] != null && dailyDates1[1] != null
					&& dailyDates2[0] != null && dailyDates2[1] != null) {

				if (/*
					 * !(dailyDates2[0].compareTo(dailyDates2[1]) == 0) &&
					 */!(weeklyDates[1].compareTo(weeklyDates[0]) < 0)) {

					tempTimelist1 = getAllDailyDates1(dailyDates1, all,
							projectList, namesList, session);
					weeklyDateslist = getAllWeeklyDatesList(weeklyDates,
							session, all, projectList, namesList);
					tempTimelist2 = getAllDailyDates2(dailyDates2, session,
							all, projectList, namesList);

				} else {

					if ((dailyDates2[0].compareTo(dailyDates2[1]) == 0)) {
						Calendar[] temdailyDates = new Calendar[2];
						temdailyDates[0] = dailyDates1[0];
						temdailyDates[1] = dailyDates2[0];
						tempTimelist1 = getAllDailyDates1(temdailyDates, all,
								projectList, namesList, session);
					} else {
						tempTimelist1 = getAllDailyDates1(dailyDates1, all,
								projectList, namesList, session);
						tempTimelist2 = getAllDailyDates2(dailyDates2, session,
								all, projectList, namesList);
					}
				}

			} else if (dailyDates1[0] != null && dailyDates1[1] == null
					&& dailyDates2[0] != null && dailyDates2[1] != null) {
				if (dailyDates1[0].compareTo(dailyDates2[0]) == 0
						&& dailyDates2[0].compareTo(weeklyDates[0]) == 0) {

					tempTimelist2 = getAllDailyDates2(dailyDates2, session,
							all, projectList, namesList);

				} else {

					weeklyDateslist = getAllWeeklyDatesList(weeklyDates,
							session, all, projectList, namesList);
					tempTimelist2 = getAllDailyDates2(dailyDates2, session,
							all, projectList, namesList);
				}
			} else if (dailyDates1[0] != null && dailyDates1[1] != null
					&& dailyDates2[0] == null && dailyDates2[1] != null) {
				if (dailyDates1[1].compareTo(dailyDates2[1]) == 0
						&& dailyDates2[1].compareTo(weeklyDates[1]) == 0) {

					tempTimelist1 = getAllDailyDates1(dailyDates1, all,
							projectList, namesList, session);

				} else {

					tempTimelist1 = getAllDailyDates1(dailyDates1, all,
							projectList, namesList, session);
					weeklyDateslist = getAllWeeklyDatesList(weeklyDates,
							session, all, projectList, namesList);
				}
			}

			else if (dailyDates1[1] == null && dailyDates2[0] == null) {

				weeklyDateslist = getAllWeeklyDatesList(weeklyDates, session,
						all, projectList, namesList);

			}

			transaction.commit();
		} catch (HibernateException e) {
			transaction.rollback();
			msg = "Error Occured while fetching data";
			logger.error("Hibernate Exception", e);
		} finally {
			session.close();
		}
		msg = "No records found for the selection made";
		if (tempTimelist1 != null && !tempTimelist1.isEmpty()) {
			calculateAllDailyTotalList(tempTimelist1, dailyDates1, viewSelected);
			msg = "";
		}
		if (weeklyDateslist != null && !weeklyDateslist.isEmpty()) {

			totalWeeklist.addAll(weeklyDateslist);
			msg = "";
		}
		if (tempTimelist2 != null && !tempTimelist2.isEmpty()) {
			calculateAllDailyTotalList(tempTimelist2, dailyDates2, viewSelected);
			msg = "";
		}

		return msg;
	}

	@SuppressWarnings("unchecked")
	private List<Timesheet> getAllDailyDates2(Calendar[] dailyDates2,
			Session session, Boolean all, List<String> listVal,
			List<String> namesList) {
		Query query2 = null;
		if (all) {
			Query query = session
					.createQuery("from Timesheet p where p.weekDate between :startDate and :endDate and p.tsu.pmName =:pmName order by p.tsu.userName,p.weekDate");
			query.setParameter("startDate", df.parseMMddYYYY(df
					.formatMMddYYYY(dailyDates2[0].getTime())));
			query.setParameter("endDate", df.parseMMddYYYY(df
					.formatMMddYYYY(dailyDates2[1].getTime())));
			query.setParameter("pmName", UserDetailHolder.getPmName());
			return query.list();

		} else {

			if (listVal != null) {
				query2 = session
						.createQuery("from Timesheet p where p.weekDate between :startDate and :endDate and p.tsu.projectID in (:ids) and p.tsu.pmName =:pmName order by  p.tsu.userName,p.weekDate ");

				query2.setParameter("startDate", df.parseMMddYYYY(df
						.formatMMddYYYY(dailyDates2[0].getTime())));
				query2.setParameter("endDate", df.parseMMddYYYY(df
						.formatMMddYYYY(dailyDates2[1].getTime())));
				query2.setParameter("pmName", UserDetailHolder.getPmName());
				query2.setParameterList("ids", listVal);
				return query2.list();
			} else if (namesList != null) {
				query2 = session
						.createQuery("from Timesheet p where p.weekDate between :startDate and :endDate and p.tsu.userName in (:namesList) and p.tsu.pmName =:pmName order by  p.tsu.userName, p.weekDate ");

				query2.setParameter("startDate", df.parseMMddYYYY(df
						.formatMMddYYYY(dailyDates2[0].getTime())));
				query2.setParameter("endDate", df.parseMMddYYYY(df
						.formatMMddYYYY(dailyDates2[1].getTime())));
				query2.setParameter("pmName", UserDetailHolder.getPmName());
				query2.setParameterList("namesList", namesList);
				return query2.list();
			}

		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private List<WeeksTotal> getAllWeeklyDatesList(Calendar[] weeklyDates,
			Session session, Boolean all, List<String> listVal,
			List<String> nameList) {
		Query query2 = null;
		if (all) {
			Query query = session
					.createQuery("from WeeksTotal p where p.weekStartDate >=:weekStartDate and p.weekEndDate <= :weekEndDate and p.timeSheet.tsu.pmName =:pmName order by p.timeSheet.tsu.userName,p.weekStartDate");
			query.setParameter("weekStartDate", df.parseMMddYYYY(df
					.formatMMddYYYY(weeklyDates[0].getTime())));
			query.setParameter("weekEndDate", df.parseMMddYYYY(df
					.formatMMddYYYY(weeklyDates[1].getTime())));
			query.setParameter("pmName", UserDetailHolder.getPmName());
			return query.list();
		} else {
			if (listVal != null) {
				query2 = session
						.createQuery("from WeeksTotal p where p.weekStartDate >=:weekStartDate and p.weekEndDate <= :weekEndDate and p.timeSheet.tsu.projectID in (:ids) and p.timeSheet.tsu.pmName =:pmName order by p.timeSheet.tsu.userName,p.weekStartDate");
				query2.setParameter("weekStartDate", df.parseMMddYYYY(df
						.formatMMddYYYY(weeklyDates[0].getTime())));
				query2.setParameter("weekEndDate", df.parseMMddYYYY(df
						.formatMMddYYYY(weeklyDates[1].getTime())));
				query2.setParameterList("ids", listVal);
				query2.setParameter("pmName", UserDetailHolder.getPmName());
				return query2.list();
			} else if (nameList != null) {
				query2 = session
						.createQuery("from WeeksTotal p where p.weekStartDate >=:weekStartDate and p.weekEndDate <= :weekEndDate and  p.timeSheet.tsu.userName in (:nameList) and p.timeSheet.tsu.pmName =:pmName order by p.timeSheet.tsu.userName,p.weekStartDate");
				query2.setParameter("weekStartDate", df.parseMMddYYYY(df
						.formatMMddYYYY(weeklyDates[0].getTime())));
				query2.setParameter("weekEndDate", df.parseMMddYYYY(df
						.formatMMddYYYY(weeklyDates[1].getTime())));
				query2.setParameterList("nameList", nameList);
				query2.setParameter("pmName", UserDetailHolder.getPmName());
				return query2.list();
			}

		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private List<Timesheet> getAllDailyDates1(Calendar[] dailyDates1,
			Boolean all, List<String> listVal, List<String> namesList,
			Session session) {
		Query query2 = null;
		if (all) {
			Query query = session
					.createQuery("from Timesheet p where p.weekDate between :startDate and :endDate and p.tsu.pmName =:pmName order by p.tsu.userName, p.weekDate");
			query.setParameter("startDate", df.parseMMddYYYY(df
					.formatMMddYYYY(dailyDates1[0].getTime())));
			query.setParameter("endDate", df.parseMMddYYYY(df
					.formatMMddYYYY(dailyDates1[1].getTime())));
			query.setParameter("pmName", UserDetailHolder.getPmName());
			return query.list();

		} else {

			if (listVal != null) {
				query2 = session
						.createQuery("from Timesheet p where p.weekDate between :startDate and :endDate and p.tsu.projectID in (:ids) and p.tsu.pmName =:pmName order by p.tsu.userName, p.weekDate ");

				query2.setParameter("startDate", df.parseMMddYYYY(df
						.formatMMddYYYY(dailyDates1[0].getTime())));
				query2.setParameter("endDate", df.parseMMddYYYY(df
						.formatMMddYYYY(dailyDates1[1].getTime())));
				query2.setParameter("pmName", UserDetailHolder.getPmName());
				query2.setParameterList("ids", listVal);
				return query2.list();
			} else if (namesList != null) {
				query2 = session
						.createQuery("from Timesheet p where p.weekDate between :startDate and :endDate and p.tsu.userName in (:namesList) and p.tsu.pmName =:pmName order by p.tsu.userName,p.weekDate ");

				query2.setParameter("startDate", df.parseMMddYYYY(df
						.formatMMddYYYY(dailyDates1[0].getTime())));
				query2.setParameter("endDate", df.parseMMddYYYY(df
						.formatMMddYYYY(dailyDates1[1].getTime())));
				query2.setParameter("pmName", UserDetailHolder.getPmName());
				query2.setParameterList("namesList", namesList);
				return query2.list();
			}

		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public String getAllWeekDailyListDB(Calendar fromDate, Calendar toDate,
			List<String> listVal) {
		String msg = "";
		Boolean all = false;
		List<Object[]> result = null;
		List<Long> resultCount = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;

		List<String> projectList = null;
		List<String> namesList = null;
		weekDailylist = new LinkedList<Timesheet>();
		for (int i = 0; i < listVal.size(); i++) {
			if (listVal.get(i).equalsIgnoreCase("All")) {
				all = true;
			} else if (listVal.get(i).equalsIgnoreCase("E5091")
					|| listVal.get(i).equalsIgnoreCase("E3199")
					|| listVal.get(i).equalsIgnoreCase("E3187")) {
				if (projectList == null) {
					projectList = new ArrayList<String>();
				}
				projectList.add(listVal.get(i));
			} else {
				if (namesList == null) {
					namesList = new ArrayList<String>();
				}
				namesList.add(listVal.get(i));
			}

		}
		try {
			transaction = session.beginTransaction();

			if (all) {
				Query query = session
						.createQuery("from Timesheet p where p.weekDate between :startDate and :endDate and p.tsu.pmName =:pmName order by p.tsu.userName, p.weekDate");
				query.setParameter("pmName", UserDetailHolder.getPmName());
				query.setParameter("startDate",
						df.parseMMddYYYY(df.formatMMddYYYY(fromDate.getTime())));
				query.setParameter("endDate",
						df.parseMMddYYYY(df.formatMMddYYYY(toDate.getTime())));
				weekDailylist = query.list();

				Query query1 = session
						.createQuery("select sum(p.hrsWorked), sum(p.regHrs) ,sum(p.offHrs),sum(p.reportedHrs),sum(p.lobHrs) from Timesheet p where p.weekDate between :startDate and :endDate and p.tsu.pmName =:pmName");
				query1.setParameter("pmName", UserDetailHolder.getPmName());
				query1.setParameter("startDate",
						df.parseMMddYYYY(df.formatMMddYYYY(fromDate.getTime())));
				query1.setParameter("endDate",
						df.parseMMddYYYY(df.formatMMddYYYY(toDate.getTime())));
				result = query1.list();

				Query query2 = session
						.createQuery("select count(p.covergeProv) from Timesheet p where p.weekDate between :startDate and :endDate and p.tsu.pmName =:pmName and p.covergeProv =:flag");
				query2.setParameter("pmName", UserDetailHolder.getPmName());
				query2.setParameter("startDate",
						df.parseMMddYYYY(df.formatMMddYYYY(fromDate.getTime())));
				query2.setParameter("endDate",
						df.parseMMddYYYY(df.formatMMddYYYY(toDate.getTime())));
				query2.setParameter("flag", true);
				resultCount = query2.list();

			} else {
				if (projectList != null) {

					Query query = session
							.createQuery("from Timesheet p where p.weekDate between :startDate and :endDate and p.tsu.projectID in (:ids) and p.tsu.pmName =:pmName order by p.tsu.userName, p.weekDate ");

					query.setParameter("startDate", df.parseMMddYYYY(df
							.formatMMddYYYY(fromDate.getTime())));
					query.setParameter("endDate", df.parseMMddYYYY(df
							.formatMMddYYYY(toDate.getTime())));
					query.setParameter("pmName", UserDetailHolder.getPmName());
					query.setParameterList("ids", projectList);
					weekDailylist = query.list();

					Query query1 = session
							.createQuery("select sum(p.hrsWorked), sum(p.regHrs) ,sum(p.offHrs),sum(p.reportedHrs),sum(p.lobHrs) from Timesheet p where p.weekDate between :startDate and :endDate and p.tsu.projectID in (:ids)  and p.tsu.pmName =:pmName");
					query1.setParameter("pmName", UserDetailHolder.getPmName());
					query1.setParameter("startDate", df.parseMMddYYYY(df
							.formatMMddYYYY(fromDate.getTime())));
					query1.setParameter("endDate", df.parseMMddYYYY(df
							.formatMMddYYYY(toDate.getTime())));
					query1.setParameterList("ids", projectList);
					result = query1.list();

					Query query2 = session
							.createQuery("select count(p.covergeProv) from Timesheet p where p.weekDate between :startDate and :endDate and p.tsu.projectID in (:ids) and p.tsu.pmName =:pmName and p.covergeProv =:flag");
					query2.setParameter("pmName", UserDetailHolder.getPmName());
					query2.setParameter("startDate", df.parseMMddYYYY(df
							.formatMMddYYYY(fromDate.getTime())));
					query2.setParameter("endDate", df.parseMMddYYYY(df
							.formatMMddYYYY(toDate.getTime())));
					query2.setParameter("flag", true);
					query2.setParameterList("ids", projectList);
					resultCount = query2.list();

				} else if (namesList != null) {

					Query query = session
							.createQuery("from Timesheet p where p.weekDate between :startDate and :endDate and p.tsu.userName in (:namesList) and p.tsu.pmName =:pmName order by p.tsu.userName, p.weekDate ");

					query.setParameter("startDate", df.parseMMddYYYY(df
							.formatMMddYYYY(fromDate.getTime())));
					query.setParameter("endDate", df.parseMMddYYYY(df
							.formatMMddYYYY(toDate.getTime())));
					query.setParameter("pmName", UserDetailHolder.getPmName());
					query.setParameterList("namesList", namesList);
					weekDailylist = query.list();

					Query query1 = session
							.createQuery("select sum(p.hrsWorked), sum(p.regHrs) ,sum(p.offHrs),sum(p.reportedHrs), sum(p.lobHrs) from Timesheet p where p.weekDate between :startDate and :endDate and p.tsu.userName in (:namesList)  and p.tsu.pmName =:pmName");
					query1.setParameter("pmName", UserDetailHolder.getPmName());
					query1.setParameter("startDate", df.parseMMddYYYY(df
							.formatMMddYYYY(fromDate.getTime())));
					query1.setParameter("endDate", df.parseMMddYYYY(df
							.formatMMddYYYY(toDate.getTime())));
					query1.setParameterList("namesList", namesList);
					result = query1.list();

					Query query2 = session
							.createQuery("select count(p.covergeProv) from Timesheet p where p.weekDate between :startDate and :endDate and p.tsu.userName in (:namesList) and p.tsu.pmName =:pmName and p.covergeProv =:flag");
					query2.setParameter("pmName", UserDetailHolder.getPmName());
					query2.setParameter("startDate", df.parseMMddYYYY(df
							.formatMMddYYYY(fromDate.getTime())));
					query2.setParameter("endDate", df.parseMMddYYYY(df
							.formatMMddYYYY(toDate.getTime())));
					query2.setParameter("flag", true);
					query2.setParameterList("namesList", namesList);
					resultCount = query2.list();
				}
			}

			transaction.commit();
		} catch (HibernateException e) {
			msg = "Error occured while fetching records, contact IT team";
			transaction.rollback();
			logger.error("Hibernate Exception", e);
		} finally {
			session.close();
		}
		msg = "No records found for selected input";
		if (!weekDailylist.isEmpty()) {
			msg = "";
			Timesheet t = new Timesheet();
			t.setUserDatePk("Total: ~" + calculateLeaveCount(weekDailylist));
			if (result != null && !result.isEmpty()) {
				t.setHrsWorked(doubleToFloat((Double) result.get(0)[0]));
				t.setRegHrs(doubleToFloat((Double) result.get(0)[1]));
				t.setOffHrs(doubleToFloat((Double) result.get(0)[2]));
				t.setReportedHrs(doubleToFloat((Double) result.get(0)[3]));

				t.setLobHrs(doubleToFloat((Double) result.get(0)[4]));

			}
			if (resultCount != null && !resultCount.isEmpty()) {

				t.setUserDatePk(t.getUserDatePk() + "~" + resultCount.get(0));
			}

			weekDailylist.add(t);
		}
		return msg;

	}

	public static void tieSystemOutAndErrToLog() {
		/* System.setOut(createLoggingProxy(System.out)); */
		System.setErr(createLoggingProxy(System.err));
	}

	public static PrintStream createLoggingProxy(
			final PrintStream realPrintStream) {
		return new PrintStream(realPrintStream) {
			public void print(final String string) {

				logger.error(string);
			}

			public void println(final String string) {
				logger.error(string);
			}
		};
	}

	@SuppressWarnings("unchecked")
	public String setEnableTimeSheet(Calendar fromDate, Calendar toDate,
			List<String> listVal) {
		String msg = "S: All records for selected dates are enabled for editing";
		Boolean all = false;
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		List<String> projectList = null;
		List<String> namesList = null;
		weekDailylist = new LinkedList<Timesheet>();
		for (int i = 0; i < listVal.size(); i++) {
			if (listVal.get(i).equalsIgnoreCase("All")) {
				all = true;
			} else if (listVal.get(i).equalsIgnoreCase("E5091")
					|| listVal.get(i).equalsIgnoreCase("E3199")
					|| listVal.get(i).equalsIgnoreCase("E3187")) {
				if (projectList == null) {
					projectList = new ArrayList<String>();
				}
				projectList.add(listVal.get(i));
			} else {
				if (namesList == null) {
					namesList = new ArrayList<String>();
				}
				namesList.add(listVal.get(i));
			}

		}
		try {
			transaction = session.beginTransaction();

			if (all) {
				Query query = session
						.createQuery("from Timesheet p where p.weekDate between :startDate and :endDate and p.tsu.pmName =:pmName order by p.weekDate");
				query.setParameter("pmName", UserDetailHolder.getPmName());
				query.setParameter("startDate",
						df.parseMMddYYYY(df.formatMMddYYYY(fromDate.getTime())));
				query.setParameter("endDate",
						df.parseMMddYYYY(df.formatMMddYYYY(toDate.getTime())));
				weekDailylist = query.list();

			} else {
				if (projectList != null) {

					Query query = session
							.createQuery("from Timesheet p where p.weekDate between :startDate and :endDate and p.tsu.projectID in (:ids) and p.tsu.pmName =:pmName order by p.weekDate ");

					query.setParameter("startDate", df.parseMMddYYYY(df
							.formatMMddYYYY(fromDate.getTime())));
					query.setParameter("endDate", df.parseMMddYYYY(df
							.formatMMddYYYY(toDate.getTime())));
					query.setParameter("pmName", UserDetailHolder.getPmName());
					query.setParameterList("ids", projectList);

				} else if (namesList != null) {

					Query query = session
							.createQuery("from Timesheet p where p.weekDate between :startDate and :endDate and p.tsu.userName in (:namesList) and p.tsu.pmName =:pmName order by p.weekDate ");

					query.setParameter("startDate", df.parseMMddYYYY(df
							.formatMMddYYYY(fromDate.getTime())));
					query.setParameter("endDate", df.parseMMddYYYY(df
							.formatMMddYYYY(toDate.getTime())));
					query.setParameter("pmName", UserDetailHolder.getPmName());
					query.setParameterList("namesList", namesList);
					weekDailylist = query.list();
				}
			}

			for (int i = 0; i < weekDailylist.size(); i++) {

				weekDailylist.get(i).setEditable(true);

			}
			transaction.commit();
		} catch (HibernateException e) {
			msg = "Error occured while fetching records, contact IT team";
			transaction.rollback();
			logger.error("Hibernate Exception", e);
		} finally {
			session.close();
		}

		if (weekDailylist.isEmpty()) {
			msg = "No Records found for enabling ";

		}
		return msg;

	}

	@SuppressWarnings("unchecked")
	public String setDisableTimeSheet(Calendar fromDate, Calendar toDate,
			List<String> listVal) {
		String msg = "S: All records for selected dates are disabled for editing";
		Boolean all = false;
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		List<String> projectList = null;
		List<String> namesList = null;
		weekDailylist = new LinkedList<Timesheet>();
		for (int i = 0; i < listVal.size(); i++) {
			if (listVal.get(i).equalsIgnoreCase("All")) {
				all = true;
			} else if (listVal.get(i).equalsIgnoreCase("E5091")
					|| listVal.get(i).equalsIgnoreCase("E3199")
					|| listVal.get(i).equalsIgnoreCase("E3187")) {
				if (projectList == null) {
					projectList = new ArrayList<String>();
				}
				projectList.add(listVal.get(i));
			} else {
				if (namesList == null) {
					namesList = new ArrayList<String>();
				}
				namesList.add(listVal.get(i));
			}

		}
		try {
			transaction = session.beginTransaction();

			if (all) {
				Query query = session
						.createQuery("from Timesheet p where p.weekDate between :startDate and :endDate and p.tsu.pmName =:pmName order by p.weekDate");
				query.setParameter("pmName", UserDetailHolder.getPmName());
				query.setParameter("startDate",
						df.parseMMddYYYY(df.formatMMddYYYY(fromDate.getTime())));
				query.setParameter("endDate",
						df.parseMMddYYYY(df.formatMMddYYYY(toDate.getTime())));
				weekDailylist = query.list();

			} else {
				if (projectList != null) {

					Query query = session
							.createQuery("from Timesheet p where p.weekDate between :startDate and :endDate and p.tsu.projectID in (:ids) and p.tsu.pmName =:pmName order by p.weekDate ");

					query.setParameter("startDate", df.parseMMddYYYY(df
							.formatMMddYYYY(fromDate.getTime())));
					query.setParameter("endDate", df.parseMMddYYYY(df
							.formatMMddYYYY(toDate.getTime())));
					query.setParameter("pmName", UserDetailHolder.getPmName());
					query.setParameterList("ids", projectList);

				} else if (namesList != null) {

					Query query = session
							.createQuery("from Timesheet p where p.weekDate between :startDate and :endDate and p.tsu.userName in (:namesList) and p.tsu.pmName =:pmName order by p.weekDate ");

					query.setParameter("startDate", df.parseMMddYYYY(df
							.formatMMddYYYY(fromDate.getTime())));
					query.setParameter("endDate", df.parseMMddYYYY(df
							.formatMMddYYYY(toDate.getTime())));
					query.setParameter("pmName", UserDetailHolder.getPmName());
					query.setParameterList("namesList", namesList);
					weekDailylist = query.list();
				}
			}

			for (int i = 0; i < weekDailylist.size(); i++) {

				weekDailylist.get(i).setEditable(false);

			}
			transaction.commit();
		} catch (HibernateException e) {
			msg = "Error occured while fetching records, contact IT team";
			transaction.rollback();
			logger.error("Hibernate Exception", e);
		} finally {
			session.close();
		}

		if (weekDailylist.isEmpty()) {
			msg = "No Records found for disabling";

		}
		return msg;

	}

	@SuppressWarnings("unchecked")
	public List<DefaulterPojo> getDefaulterList(Date fromDate, Date toDate) {

		// DefaulterPojo List
		List<DefaulterPojo> defaultersList = new LinkedList<DefaulterPojo>();

		Session session = HibernateUtil.getSessionFactory().openSession();

		while (fromDate.compareTo(toDate) < 0) {

			Transaction tx = session.beginTransaction();
			Query query = session
					.createQuery("select tsu.userName,tsu.projectID,tsu.userId from TimesheetUser tsu where tsu.userName NOT IN (select p.timeSheet.tsu.userName from  WeeksTotal p where p.weekStartDate =:weekStartDate)");

			query.setParameter("weekStartDate", fromDate);
			// list of defaulter's name
			List<Object[]> defaultersNames;

			defaultersNames = (List<Object[]>) query.list();

			Calendar c = Calendar.getInstance();
			c.setTime(fromDate);
			c.add(Calendar.DATE, 6); // number of days to add
			String currentWeekEnd = df.formatMMMddyyyy(c.getTime());

			for (Object[] defaultersName : defaultersNames) {
				Registration reg = (Registration) session.get(
						Registration.class, (String) defaultersName[2]);
				if (!reg.isApproved()) {
					continue;
				}
				DefaulterPojo defaulter = new DefaulterPojo();
				defaulter.setUserName((String) defaultersName[0]);
				defaulter.setWeekDate(df.formatMMMddyyyy(fromDate) + " - "
						+ currentWeekEnd);
				defaulter.setProjectID((String) defaultersName[1]);
				defaulter.setUserID((String) defaultersName[2]);
				defaultersList.add(defaulter);
			}

			c.setTime(fromDate);
			c.add(Calendar.DATE, 7); // number of days to add
			String fromNext = df.formatMMMddyyyy(c.getTime());
			fromDate = df.parseMMMddyyyy(fromNext);

			tx.commit();
		}

		return defaultersList;
	}

	public String changePassword(String userID, String newPWDValue) {

		Registration reg = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			reg = (Registration) session.get(Registration.class, userID);
			if (reg != null) {
				reg.setPassword(newPWDValue);
			}
			transaction.commit();
		} catch (HibernateException e) {

			transaction.rollback();
			logger.error("Hibernate Exception", e);
			return "Error Occured while changing password";
		} finally {
			session.close();
		}
		return "S: Password Changed Successfully";
	}

	public List<WeeksTotal> getTotalWeekDefaultDate() {

		return totalAllDefaultlist;
	}

	public String getAllTotalDefaultList(Calendar fromDate, Calendar toDate,
			List<String> listVal, String viewSelected) {
		totalAllDefaultlist = new LinkedList<WeeksTotal>();
		Calendar[] dailyDates1 = new Calendar[2];
		dailyDates1[0] = fromDate;
		dailyDates1[1] = toDate;
		String msg = "";
		Boolean all = false;
		List<Timesheet> tempTimelist1 = null;
		List<String> projectList = null;
		List<String> namesList = null;
		for (int i = 0; i < listVal.size(); i++) {
			if (listVal.get(i).equalsIgnoreCase("All")) {
				all = true;
			} else if (listVal.get(i).equalsIgnoreCase("E5091")
					|| listVal.get(i).equalsIgnoreCase("E3199")
					|| listVal.get(i).equalsIgnoreCase("E3187")) {
				if (projectList == null) {
					projectList = new ArrayList<String>();
				}
				projectList.add(listVal.get(i));
			} else {
				if (namesList == null) {
					namesList = new ArrayList<String>();
				}
				namesList.add(listVal.get(i));
			}

		}

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		try {

			transaction = session.beginTransaction();

			tempTimelist1 = getAllDailyDates1(dailyDates1, all, projectList,
					namesList, session);

			transaction.commit();
		} catch (HibernateException e) {
			transaction.rollback();
			msg = "Error Occured while fetching data";
			logger.error("Hibernate Exception", e);
		} finally {
			session.close();
		}
		msg = "No records found for the selection made";
		if (tempTimelist1 != null && !tempTimelist1.isEmpty()) {
			calculateAllDailyTotalList(tempTimelist1, dailyDates1, viewSelected);
			msg = "";
		}
		return msg;
	}

	@SuppressWarnings("unchecked")
	public String disApproveUser(List<String> listVal) {

		String msg = "S: User(s) selected are Disapproved for Time Booking";
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;

		try {
			transaction = session.beginTransaction();

			Query query = session
					.createQuery("from Registration p where p.userName in (:names)");

			query.setParameterList("names", listVal);

			List<Registration> regList = query.list();

			for (int i = 0; i < regList.size(); i++) {

				regList.get(i).setApproved(false);

			}
			transaction.commit();
		} catch (HibernateException e) {
			msg = "Error occured while executing records, contact IT team";
			transaction.rollback();
			logger.error("Hibernate Exception", e);
		} finally {
			session.close();
		}
		return msg;

	}

	public String changePersonalInfo(Registration reg) {
		String msg = "Changes saved successfully - User needs to be approved to proceed";
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		TimesheetUser tuser = null;

		try {
			transaction = session.beginTransaction();
			reg.setApproved(false);
			session.saveOrUpdate(reg);
			tuser = getTuser(session, reg.getUserId());
			if (tuser != null) {
				tuser.setProjectID(reg.getProjectID());
				tuser.setPmName(reg.getPmName());
				tuser.setBillStatus(reg.getBillStatus());
				tuser.setUserName(reg.getUserName());
			}

			transaction.commit();
		} catch (HibernateException e) {
			msg = "Error occured while executing records, contact IT team";
			transaction.rollback();
			logger.error("Hibernate Exception", e);
		} finally {
			session.close();
		}
		return msg;

	}

	public String[] getPSNO(Registration reg) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		String PSNo[] = new String[2];

		try {
			transaction = session.beginTransaction();
			Query query = session
					.createQuery("select p.userId from Registration p where p.userName =:pmName");
			query.setParameter("pmName", reg.getPmName());
			PSNo[0] = (String) query.uniqueResult();
			Query query1 = session
					.createQuery("select p.userId from Registration p where p.userName =:approverName");
			query1.setParameter("approverName", reg.getApprover());
			PSNo[1] = (String) query1.uniqueResult();

			transaction.commit();
		} catch (HibernateException e) {
			transaction.rollback();
			logger.error("Hibernate Exception", e);
		} finally {
			session.close();
		}
		return PSNo;

	}

	public TimesheetUser getTuser(Session session, String userID) {
		Query query3 = session
				.createQuery("from TimesheetUser as t where t.userId=:userId");
		query3.setParameter("userId", userID);

		return (TimesheetUser) query3.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public String getIndTotalDefaultList(Calendar fromDate, Calendar toDate,
			String viewSelected) {
		totalAllDefaultlist = new LinkedList<WeeksTotal>();
		Calendar[] dailyDates1 = new Calendar[2];
		dailyDates1[0] = fromDate;
		dailyDates1[1] = toDate;
		List<Timesheet> tempTimelist1 = null;
		String msg = "";
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		try {

			transaction = session.beginTransaction();

			Query query = session
					.createQuery("from Timesheet p where p.weekDate between :startDate and :endDate and p.tsu.userId =:id and p.tsu.pmName =:pmName order by p.tsu.userName, p.weekDate ");

			query.setParameter("startDate", df.parseMMddYYYY(df
					.formatMMddYYYY(dailyDates1[0].getTime())));
			query.setParameter("endDate", df.parseMMddYYYY(df
					.formatMMddYYYY(dailyDates1[1].getTime())));
			query.setParameter("pmName", UserDetailHolder.getPmName());
			query.setParameter("id", UserDetailHolder.getUserID());
			tempTimelist1 = query.list();

			transaction.commit();
		} catch (HibernateException e) {
			transaction.rollback();
			msg = "Error Occured while fetching data";
			logger.error("Hibernate Exception", e);
		} finally {
			session.close();
		}
		msg = "No records found for the selection made";
		if (tempTimelist1 != null && !tempTimelist1.isEmpty()) {
			calculateAllDailyTotalList(tempTimelist1, dailyDates1, viewSelected);
			msg = "";
		}
		return msg;
	}
}
