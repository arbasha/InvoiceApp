package com.invoice.model;

import java.util.Date;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.invoice.entity.WeeksTotal;
import com.invoice.pojo.BillCategory;
import com.invoice.pojo.LeaveTypeCategory;

/**
 * @author Arshad
 *
 */
public class WeeklyTotalModel extends AbstractTableModel {

	private static final long serialVersionUID = -9072773588177452578L;
	private List<WeeksTotal> weekTotalList;
	private String Columns[] = { "From Date", "To Date", "Regular Hours",
			"Off Hours", "Reported Hours", "Leaves Taken", "Backfilled",
			"Holiday(s)", "LOB", "Remarks" };

	public void setWeekTotalList(List<WeeksTotal> weekTotalList) {
		this.weekTotalList = weekTotalList;
	}

	@Override
	public int getColumnCount() {

		return 10;
	}

	@Override
	public int getRowCount() {

		return weekTotalList.size() - 1;
	}

	@Override
	public Object getValueAt(int row, int col) {

		WeeksTotal wto = weekTotalList.get(row);

		switch (col) {
		case 0:
			if (row == weekTotalList.size() - 1)
				return wto.getUniqueID();
			return wto.getWeekStartDate();
		case 1:
			if (row == weekTotalList.size() - 1)
				return "";
			return wto.getWeekEndDate();
		case 2:
			return wto.getTotalRegHrs();
		case 3:
			return wto.getTotalOffHrs();
		case 4:
			return wto.getTotalReportedHrs();
		case 5:
			return wto.getTotalLeaveCount();
		case 6:
			return wto.getTotalCovCount();
		case 7:
			return wto.getTotalHolidayCount();
		case 8:
			return wto.getTotalLobHrs();
		case 9:
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

		case 9:

			return true;
		}

		return false;

	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {

		case 0:
			return Date.class;
		case 1:
			return Date.class;
		case 2:
			return Float.class;
		case 3:
			return Float.class;
		case 4:
			return Float.class;
		case 5:
			return LeaveTypeCategory.class;
		case 6:
			return BillCategory.class;
		case 7:
			return Integer.class;
		case 8:
			return Float.class;
		case 9:
			return String.class;

		}
		return null;

	}

	public List<WeeksTotal> getWeekTotalList() {
		return weekTotalList;
	}

}
