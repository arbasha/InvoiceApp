package com.invoice.model;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.invoice.pojo.Employee;

/**
 * @author Arshad
 *
 */
public class EmployeeTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1459557656254498644L;
	private List<Employee> emplist;
	private String Columns[] = { "Name", "Combo Bill", "Billability",
			"Age Category", "US Citizen", "Tax ID", "Gender" };

	public void setEmpList(List<Employee> emplist) {
		this.emplist = emplist;
	}

	@Override
	public int getColumnCount() {
		return 7;
	}

	@Override
	public int getRowCount() {
		return emplist.size();
	}

	@Override
	public Object getValueAt(int row, int col) {

		Employee emp = emplist.get(row);

		switch (col) {
		case 0:
			return emp.getName();
		case 1:
			return emp.getComp();
		case 2:
			return emp.getBillability();
		case 3:
			return emp.getAgeCategory();
		case 4:
			return emp.isUsCitizen();
		case 5:
			return emp.getTaxName();
		case 6:
			return emp.getGender();

		}
		return null;
	}

	@Override
	public String getColumnName(int col) {

		return Columns[col];
	}

}
