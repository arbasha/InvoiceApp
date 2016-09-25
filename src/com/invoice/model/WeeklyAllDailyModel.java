package com.invoice.model;

import java.util.Date;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.invoice.entity.Timesheet;
import com.invoice.pojo.BillCategory;
import com.invoice.pojo.LeaveTypeCategory;
import com.invoice.util.DateFormatter;

public class WeeklyAllDailyModel extends AbstractTableModel {

	private static final long serialVersionUID = -6222323240229554668L;
	private List<Timesheet> weekDailyList;
	private DateFormatter df = DateFormatter.getInstance();
	private String Columns[] = { "Name", "P.S NO", "Date", "Hours Worked",
			"Regular Hours", "Off Hours", "Leave Type", "Status", "Backfilled",
			"LOB", "Remarks", "Reported Hours" };

	public void setWeekAllDailyList(List<Timesheet> weekDailyList) {
		this.weekDailyList = weekDailyList;
	}

	public List<Timesheet> getWeekAllDailyList() {
		return weekDailyList;
	}

	@Override
	public int getColumnCount() {

		return 12;
	}

	@Override
	public int getRowCount() {

		return weekDailyList.size() - 1;

	}

	@Override
	public Object getValueAt(int row, int col) {

		Timesheet emp = weekDailyList.get(row);

		switch (col) {

		case 0:
			if (row == weekDailyList.size() - 1)
				return emp.getUserDatePk().split("~")[0];
			return emp.getTsu().getUserName();
		case 1:
			if (row == weekDailyList.size() - 1)
				return "";
			return emp.getTsu().getUserId();
		case 2:
			if (row == weekDailyList.size() - 1)
				return "";
			return df.parseEMMMddyyyy(df.formatEMMMddyyyy(emp.getWeekDate()));
		case 3:
			return emp.getHrsWorked();
		case 4:
			return emp.getRegHrs();
		case 5:
			return emp.getOffHrs();
		case 6:
			if (row == weekDailyList.size() - 1)

				return emp.getUserDatePk().split("~")[1];
			return emp.getLeaveType();

		case 7:

			if (row == weekDailyList.size() - 1)

				return "";
			return emp.getBillStatus();
		case 8:
			if (row == weekDailyList.size() - 1)

				return emp.getUserDatePk().split("~")[2];

			return emp.isCovergeProv();
		case 9:
			return emp.getLobHrs();
		case 10:
			return emp.getRemarks();
		case 11:
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
			return String.class;
		case 1:
			return String.class;
		case 2:
			return Date.class;
		case 3:
			return Float.class;
		case 4:
			return Float.class;
		case 5:
			return Float.class;
		case 6:
			return LeaveTypeCategory.class;
		case 7:
			return BillCategory.class;
		case 8:
			return Boolean.class;
		case 9:
			return Float.class;
		case 10:
			return String.class;
		case 11:
			return Float.class;

		}
		return null;

	}

}
