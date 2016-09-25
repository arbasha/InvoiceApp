package com.invoice.model;

import java.util.Date;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.invoice.entity.Timesheet;
import com.invoice.pojo.BillCategory;
import com.invoice.pojo.LeaveTypeCategory;

/**
 * @author Arshad
 *
 */
public class WeeklyDailyModel extends AbstractTableModel {

	private static final long serialVersionUID = 1338444400080964742L;
	private List<Timesheet> weekDailyList;
	private String Columns[] = { "Date", "Hours Worked", "Regular Hours",
			"Off Hours", "Leave Type", "Status", "Backfilled", "LOB",
			"Remarks", "Reported Hours" };

	public void setWeekDailyList(List<Timesheet> weekDailyList) {
		this.weekDailyList = weekDailyList;
	}

	public List<Timesheet> getWeekDailyList() {
		return weekDailyList;
	}

	@Override
	public int getColumnCount() {

		return 10;
	}

	@Override
	public int getRowCount() {

		return weekDailyList.size()-1;

	}

	@Override
	public Object getValueAt(int row, int col) {

		Timesheet emp = weekDailyList.get(row);

		switch (col) {
		case 0:
			if (row == weekDailyList.size() - 1)
				return emp.getUserDatePk().split("~")[0];
			return emp.getWeekDate();
		case 1:
			return emp.getHrsWorked();
		case 2:
			return emp.getRegHrs();
		case 3:
			return emp.getOffHrs();
		case 4:
			if (row == weekDailyList.size() - 1)

				return emp.getUserDatePk().split("~")[1];
			return emp.getLeaveType();

		case 5:
			/*
			 * if (emp.getTsu() != null) { return emp.getTsu().getBillStatus();
			 * } return "";
			 */
			if (row == weekDailyList.size() - 1)

				return "";
			return emp.getBillStatus();
		case 6:
			if (row == weekDailyList.size() - 1)

				return emp.getUserDatePk().split("~")[2];

			return emp.isCovergeProv();
		case 7:
			return emp.getLobHrs();
		case 8:
			return emp.getRemarks();
		case 9:
			return emp.getReportedHrs();

		}
		return null;
	}

	@Override
	public String getColumnName(int col) {

		return Columns[col];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return Date.class;
		case 1:
			return Float.class;
		case 2:
			return Float.class;
		case 3:
			return Float.class;
		case 4:
			return LeaveTypeCategory.class;
		case 5:
			return BillCategory.class;
		case 6:
			return Boolean.class;
		case 7:
			return Float.class;
		case 8:
			return String.class;
		case 9:
			return Float.class;
		}
		return null;

	}

}
