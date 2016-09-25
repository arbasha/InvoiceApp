package com.invoice.model;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.invoice.pojo.DefaulterPojo;

/**
 * @author Arshad
 *
 */
public class DefaulterListTableModel extends AbstractTableModel {

	private static final long serialVersionUID = -1241536063734069189L;
	private List<DefaulterPojo> defaulterList;
	private String Columns[] = { "P.S.NO", "Name", "Week", "Project" };

	public void setDefaulterList(List<DefaulterPojo> defaulterlist2) {
		this.defaulterList = defaulterlist2;
	}

	public List<DefaulterPojo> getDefaulterList() {
		return defaulterList;
	}

	@Override
	public int getColumnCount() {

		return 4;
	}

	@Override
	public int getRowCount() {

		return defaulterList.size() - 1;

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

		DefaulterPojo emp = defaulterList.get(row);

		switch (col) {
		case 0:
			return emp.getUserID();

		case 1:
			return emp.getUserName();

		case 2:
			return emp.getWeekDate();

		case 3:
			return emp.getProjectID();
		}
		return null;
	}

	@Override
	public String getColumnName(int col) {

		return Columns[col];
	}

}
