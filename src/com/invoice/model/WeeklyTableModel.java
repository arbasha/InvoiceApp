package com.invoice.model;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.invoice.entity.Timesheet;
import com.invoice.pojo.BillCategory;
import com.invoice.pojo.LeaveTypeCategory;
import com.invoice.util.DateFormatter;

/**
 * @author Arshad
 *
 */
public class WeeklyTableModel extends AbstractTableModel {

	private static final long serialVersionUID = -8305246128020689293L;
	private List<Timesheet> timeList;
	private DateFormatter df = DateFormatter.getInstance();
	private String Columns[] = { "Date", "Hours Worked", "Regular Hours",
			"Off Hours", "Leave Type", "Status", "Backfilled", "LOB",
			"Remarks", "Reported Hours" };

	public void setTimeList(List<Timesheet> timeList) {
		this.timeList = timeList;
	}

	public List<Timesheet> getTimeList() {
		return timeList;
	}

	@Override
	public int getColumnCount() {

		return 10;
	}

	@Override
	public int getRowCount() {

		return timeList.size();

	}

	public Float calculateTotal(int col) {
		Float total = (float) 0;
		for (int i = 0; i < getRowCount() - 1; i++) {
			Object value = getValueAt(i, col);
			if (value != null)
				total = total + (Float) value; // getValueAt(row, column)
		}
		return total;
	}

	@Override
	public Object getValueAt(int row, int col) {

		Timesheet emp = timeList.get(row);

		switch (col) {
		case 0:
			if (row == timeList.size() - 1)
				return emp.getUserDatePk();
			return df.formatEMMMddyyyy(emp.getWeekDate());
		case 1:
			return emp.getHrsWorked();
		case 2:
			return emp.getRegHrs();
		case 3:
			return emp.getOffHrs();
		case 4:
			if (row == timeList.size() - 1)

				return "";
			return emp.getLeaveType();

		case 5:
			/*
			 * if (emp.getTsu() != null) { return emp.getTsu().getBillStatus();
			 * } return "";
			 */
			if (row == timeList.size() - 1)

				return "";
			return emp.getBillStatus();
		case 6:
			if (row == timeList.size() - 1)

				return "";
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
	public boolean isCellEditable(int row, int col) {
		if (row != 7) {
			if (timeList.get(row).isEditable()) {
				switch (col) {
				case 0:
					return false;
				case 1:
					return true;
				case 2:
					return true;
				case 3:
					return true;
				case 4:
					/*
					 * if (row == 7 && col == 4) return false;
					 */
					return true;

				case 5:
					/*
					 * if (row == 7 && col == 5) return false;
					 */
					return true;
				case 6:
					if (!timeList.get(row).getLeaveType()
							.equals(LeaveTypeCategory.EMPTY)
							&& timeList.get(row).getBillStatus()
									.equals(BillCategory.Billable))
						return true;
					return false;
				case 7:
					return true;
				case 8:
					return true;
				case 9:
					return false;
				}
			}
		}
		return false;

	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

		fireTableDataChanged();
		if (timeList == null)
			return;
		Float repoHrs = (float) 0;
		Timesheet t = timeList.get(rowIndex);
		Timesheet t1 = timeList.get(7);
		switch (columnIndex) {
		case 1:

			t.setHrsWorked((Float) aValue);
			t1.setHrsWorked(calculateTotal(columnIndex));
			if (t.getBillStatus().equals(BillCategory.Buffer)) {
				if ((t.getRemarks() == null || t.getRemarks().equalsIgnoreCase(
						""))
						&& aValue != null) {
					t.setRemarks("On " + df.formatddMMMyyyy(t.getWeekDate())
							+ " Backfilled - ");

				} else if (t.getHrsWorked() == null && t.getOffHrs() == null
						&& t.getRegHrs() == null) {
					t.setRemarks("");
				}
				fireTableCellUpdated(5, rowIndex);
			}
			fireTableCellUpdated(7, columnIndex);
			break;
		case 2:
			t.setRegHrs((Float) aValue);
			if (t.getRegHrs() != null) {
				repoHrs = repoHrs + t.getRegHrs();
			}
			if (t.getOffHrs() != null) {
				repoHrs = repoHrs + (t.getOffHrs());
			}
			t.setReportedHrs(repoHrs);

			t1.setRegHrs(calculateTotal(columnIndex));
			if (t1.getRegHrs() == null) {
				t1.setRegHrs((float) 0);
			}
			if (t1.getOffHrs() == null) {
				t1.setOffHrs((float) 0);
			}

			t1.setReportedHrs(t1.getRegHrs() + t1.getOffHrs());
			if (t.getBillStatus().equals(BillCategory.Buffer)) {
				if ((t.getRemarks() == null || t.getRemarks().equalsIgnoreCase(
						""))
						&& aValue != null) {
					t.setRemarks("On " + df.formatddMMMyyyy(t.getWeekDate())
							+ " Backfilled - ");

				} else if (t.getHrsWorked() == null && t.getOffHrs() == null
						&& t.getRegHrs() == null) {
					t.setRemarks("");
				}
				fireTableCellUpdated(5, rowIndex);
			}
			fireTableCellUpdated(7, columnIndex);
			break;
		case 3:
			t.setOffHrs((Float) aValue);
			if (t.getRegHrs() != null) {
				repoHrs = repoHrs + t.getRegHrs();
			}
			if (t.getOffHrs() != null) {
				repoHrs = repoHrs + (t.getOffHrs());
			}
			t.setReportedHrs(repoHrs);

			t1.setOffHrs(calculateTotal(columnIndex));
			if (t1.getRegHrs() == null) {
				t1.setRegHrs((float) 0);
			}
			if (t1.getOffHrs() == null) {
				t1.setOffHrs((float) 0);
			}
			t1.setReportedHrs(t1.getRegHrs() + t1.getOffHrs());
			if (t.getBillStatus().equals(BillCategory.Buffer)) {
				if ((t.getRemarks() == null || t.getRemarks().equalsIgnoreCase(
						""))
						&& aValue != null) {
					t.setRemarks("On " + df.formatddMMMyyyy(t.getWeekDate())
							+ " Backfilled - ");

				} else if (t.getHrsWorked() == null && t.getOffHrs() == null
						&& t.getRegHrs() == null) {
					t.setRemarks("");
				}
				fireTableCellUpdated(5, rowIndex);
			}
			fireTableCellUpdated(7, columnIndex);
			break;
		case 4:

			t.setLeaveType((LeaveTypeCategory) aValue);
			if (t.getBillStatus().equals(BillCategory.Billable)) {
				if (t.getLeaveType().equals(LeaveTypeCategory.Planned)
						|| t.getLeaveType().equals(LeaveTypeCategory.UnPlanned)) {
					if (t.getLobHrs() == null) {
						if (!t.isCovergeProv())
							t.setLobHrs((float) 8);
					}

					t.setRemarks("OOO on "
							+ df.formatddMMMyyyy(t.getWeekDate())
							+ " Backfilled by -");
				} else if (t.getLeaveType().equals(LeaveTypeCategory.CompOff)) {
					if (t.getLobHrs() == null) {
						if (!t.isCovergeProv())
							t.setLobHrs((float) 8);
					}
					t.setRemarks("Took Comp Off on "
							+ df.formatddMMMyyyy(t.getWeekDate())
							+ " Backfilled by -");
				} else if (t.getLeaveType().equals(LeaveTypeCategory.Holiday)) {
					t.setLobHrs(null);
					t.setRemarks("L&T Holiday on "
							+ df.formatddMMMyyyy(t.getWeekDate()));
				} else if (t.getLeaveType().equals(LeaveTypeCategory.SplDay)) {
					if (t.getLobHrs() == null) {
						if (!t.isCovergeProv())
							t.setLobHrs((float) 8);
					}
					t.setRemarks("Special Day off on "
							+ df.formatddMMMyyyy(t.getWeekDate())
							+ " Backfilled by - ");
				} else {
					t.setLobHrs(null);
					t.setRemarks("");
				}
				if (t.getBillStatus().equals(BillCategory.Buffer)) {
					if ((t.getRemarks() == null || t.getRemarks()
							.equalsIgnoreCase("")) && aValue != null) {
						t.setRemarks("On "
								+ df.formatddMMMyyyy(t.getWeekDate())
								+ " Backfilled - ");

					} else if (t.getHrsWorked() == null
							&& t.getOffHrs() == null && t.getRegHrs() == null) {
						t.setRemarks("");
					}
					fireTableCellUpdated(5, rowIndex);
				}
				if (t.getLeaveType().equals(LeaveTypeCategory.EMPTY)) {
					t.setCovergeProv(false);
					fireTableCellUpdated(rowIndex, 6);
				}
			} else {
				t.setLobHrs(null);
				if (!t.getLeaveType().equals(LeaveTypeCategory.Holiday)
						&& !t.getLeaveType().equals(LeaveTypeCategory.EMPTY)) {
					t.setRemarks("OOO on "
							+ df.formatddMMMyyyy(t.getWeekDate())
							+ ", Buffer Resource");
				} else {
					t.setRemarks("");
				}
			}
			fireTableCellUpdated(rowIndex, 8);
			fireTableCellUpdated(rowIndex, 7);
			break;

		case 5:
			t.setBillStatus((BillCategory) aValue);
			if (t.getBillStatus().equals(BillCategory.Billable)) {
				if (t.getLeaveType().equals(LeaveTypeCategory.EMPTY)) {
					t.setRemarks("");
				} else if (t.getLeaveType().equals(LeaveTypeCategory.Planned)
						|| t.getLeaveType().equals(LeaveTypeCategory.UnPlanned)) {
					t.setRemarks("OOO on "
							+ df.formatddMMMyyyy(t.getWeekDate())
							+ " Backfilled by -");
					t.setLobHrs((float) 8);
				} else if (t.getLeaveType().equals(LeaveTypeCategory.CompOff)) {
					t.setRemarks("Took Comp Off on "
							+ df.formatddMMMyyyy(t.getWeekDate())
							+ " Backfilled by -");
					t.setLobHrs((float) 8);
				} else if (t.getLeaveType().equals(LeaveTypeCategory.SplDay)) {
					t.setRemarks("Special Day off on "
							+ df.formatddMMMyyyy(t.getWeekDate())
							+ " Backfilled by - ");
					t.setLobHrs((float) 8);
				} else if (t.getLeaveType().equals(LeaveTypeCategory.Holiday)) {
					t.setRemarks("L&T Holiday on "
							+ df.formatddMMMyyyy(t.getWeekDate()));
				}

			} else {
				if (t.getHrsWorked() != null || t.getRegHrs() != null
						|| t.getOffHrs() != null) {
					t.setRemarks("On " + df.formatddMMMyyyy(t.getWeekDate())
							+ " Backfilled - ");
				} else if (!t.getLeaveType().equals(LeaveTypeCategory.Holiday)
						&& !t.getLeaveType().equals(LeaveTypeCategory.EMPTY)) {
					t.setRemarks("OOO on "
							+ df.formatddMMMyyyy(t.getWeekDate())
							+ ", Buffer Resource");
				} else {
					t.setRemarks("");
				}
				t.setLobHrs(null);
				t.setCovergeProv(false);
			}
			fireTableCellUpdated(rowIndex, 6);
			fireTableCellUpdated(rowIndex, 7);
			fireTableCellUpdated(rowIndex, 8);
			break;
		case 6:

			t.setCovergeProv((Boolean) aValue);
			if (t.isCovergeProv()) {
				if (!t.getLeaveType().equals(LeaveTypeCategory.EMPTY)
						&& !t.getLeaveType().equals(LeaveTypeCategory.Holiday)) {
					t.setLobHrs(null);

				}
			} else {
				if (!t.getLeaveType().equals(LeaveTypeCategory.EMPTY)
						&& !t.getLeaveType().equals(LeaveTypeCategory.Holiday)) {
					t.setLobHrs((float) 8);

				}
			}
			t1.setLobHrs(calculateTotal(7));
			fireTableCellUpdated(rowIndex, 7);
			fireTableCellUpdated(7, 7);
			break;
		case 7:
			t.setLobHrs((Float) aValue);
			if (t.getBillStatus().equals(BillCategory.Buffer)) {
				if ((t.getRemarks() == null || t.getRemarks().equalsIgnoreCase(
						""))
						&& aValue != null) {
					t.setRemarks("On " + df.formatddMMMyyyy(t.getWeekDate())
							+ " Backfilled - ");

				} else if (t.getHrsWorked() == null && t.getOffHrs() == null
						&& t.getRegHrs() == null) {
					t.setRemarks("");
				}
				fireTableCellUpdated(5, rowIndex);
			}
			break;
		case 8:
			t.setRemarks((String) aValue);
			break;
		case 9:
			if (t.getRegHrs() != null) {
				repoHrs = repoHrs + t.getRegHrs();
			}
			if (t.getOffHrs() != null) {
				repoHrs = repoHrs + (t.getOffHrs());
			}
			t.setReportedHrs(repoHrs);

			t1.setReportedHrs(calculateTotal(columnIndex));

			break;
		}
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
