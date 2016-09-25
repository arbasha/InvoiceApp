package com.invoice.renderer;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import com.invoice.pojo.LeaveTypeCategory;

/**
 * @author Arshad
 *
 */
public class LeaveTypeCategoryRenderer implements TableCellRenderer {

	@SuppressWarnings("rawtypes")
	private JComboBox combo;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public LeaveTypeCategoryRenderer() {
		combo = new JComboBox(LeaveTypeCategory.values());
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		combo.setSelectedItem(value);
		combo.setBackground(Color.LIGHT_GRAY);
		combo.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		return combo;
	}

}
