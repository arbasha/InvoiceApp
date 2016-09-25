package com.invoice.model;

import java.util.Date;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.invoice.entity.WeeksTotal;
import com.invoice.pojo.BillCategory;
import com.invoice.pojo.LeaveTypeCategory;
import com.invoice.util.DateFormatter;

/**
 * @author Arshad
 *
 */
public class WeekAllTotalDefaultModel extends AbstractTableModel {

	private static final long serialVersionUID = 6981096340252890839L;
	private List<WeeksTotal> weekTotalAllList;
	DateFormatter df = DateFormatter.getInstance();
	private String Columns[] = { "Name", "P.S. NO", "From Date", "To Date",
			"Regular Hours", "Off Hours", "Reported Hours", "Leaves Taken",
			"Backfilled", "Holidays(s)", "LOB", "Remarks" };

	public void setWeekAllTotalList(List<WeeksTotal> weekTotalList) {
		this.weekTotalAllList = weekTotalList;
	}
	
	public List<WeeksTotal> getWeekAllTotalList() {
		return weekTotalAllList;
	}


	@Override
	public int getColumnCount() {
		return 12;
	}

	@Override
	public int getRowCount() {

		return weekTotalAllList.size() - 1;
	}

	@Override
	public Object getValueAt(int row, int col) {

		WeeksTotal wto = weekTotalAllList.get(row);

		switch (col) {
		case 0:
			if (row == weekTotalAllList.size() - 1)
				return wto.getUniqueID();
			if (wto.getTimeSheet() == null)
				return "";
			return wto.getTimeSheet().getTsu().getUserName();
		case 1:
			if (wto.getTimeSheet() == null)
				return "";
			return wto.getTimeSheet().getTsu().getUserId();
		case 2:
			if (row == weekTotalAllList.size() - 1)
				return "";
			return wto.getWeekStartDate();
		case 3:
			if (row == weekTotalAllList.size() - 1)
				return "";
			return wto.getWeekEndDate();
		case 4:
			return wto.getTotalRegHrs();
		case 5:
			return wto.getTotalOffHrs();
		case 6:
			return wto.getTotalReportedHrs();
		case 7:
			return wto.getTotalLeaveCount();
		case 8:
			return wto.getTotalCovCount();
		case 9:
			return wto.getTotalHolidayCount();
		case 10:
			return wto.getTotalLobHrs();
		case 11:
			String conRemarks = wto.getConsolidatedRemarks();
			conRemarks = conRemarks.replaceAll("~", "\n");
			return conRemarks;

		}
		return null;
	}

	@Override
	public String getColumnName(int col) {

		return Columns[col];
	}

	@Override
	public boolean isCellEditable(int row, int col) {

		switch (col) {

		case 11:
	/*		if (row == getRowCount() - 1)
			{
				return false;
			}*/
			return true;
		}

		return false;

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
			return Date.class;
		case 4:
			return Float.class;
		case 5:
			return Float.class;
		case 6:
			return Float.class;
		case 7:
			return LeaveTypeCategory.class;
		case 8:
			return BillCategory.class;
		case 9:
			return Integer.class;
		case 10:
			return Integer.class;
		case 11:
			return Integer.class;
		case 12:
			return String.class;

		}
		return null;

	}

}
