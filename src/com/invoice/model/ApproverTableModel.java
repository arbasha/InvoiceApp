package com.invoice.model;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.invoice.entity.Registration;

/**
 * @author Arshad
 *
 */
public class ApproverTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 5023511872672159221L;
	private List<Registration> approveList;
	private String Columns[] = { "Name", "User ID", "Role", "PM Name",
			"Bill Status", "User Type", "Approved" };

	public void setApproveList(List<Registration> approveList) {
		this.approveList = approveList;
	}

	public List<Registration> getApproveList() {
		return approveList;
	}

	@Override
	public int getColumnCount() {

		return 7;
	}

	@Override
	public int getRowCount() {

		return approveList.size() - 1;

	}

	@Override
	public Object getValueAt(int row, int col) {

		Registration emp = approveList.get(row);

		switch (col) {
		case 0:
			return emp.getUserName();
		case 1:
			return emp.getUserId();
		case 2:
			return emp.getRole();
		case 3:
			return emp.getPmName();
		case 4:
			return emp.getBillStatus();
		case 5:
			return emp.getUserType();
		case 6:
			return emp.isApproved();
		}
		return null;
	}

	@Override
	public boolean isCellEditable(int row, int col) {

		switch (col) {
		case 0:
			return false;
		case 1:
			return false;
		case 2:
			return false;
		case 3:
			return false;
		case 4:
			return false;
		case 5:
			return false;
		case 6:
			return true;
		}
		return false;

	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

		if (approveList == null)
			return;

		Registration reg = approveList.get(rowIndex);

		switch (columnIndex) {

		case 6:
			reg.setApproved((Boolean) aValue);
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
			return String.class;
		case 2:
			return String.class;
		case 3:
			return String.class;
		case 4:
			return String.class;
		case 5:
			return String.class;
		case 6:
			return Boolean.class;

		}
		return null;

	}

}
