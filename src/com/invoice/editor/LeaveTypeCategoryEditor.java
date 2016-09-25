package com.invoice.editor;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import com.invoice.pojo.LeaveTypeCategory;

/**
 * @author Arshad
 *
 */
public class LeaveTypeCategoryEditor extends AbstractCellEditor implements
		TableCellEditor {

	private static final long serialVersionUID = 2737541376865485652L;
	@SuppressWarnings("rawtypes")
	private JComboBox combo;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public LeaveTypeCategoryEditor() {
		combo = new JComboBox(LeaveTypeCategory.values());
	}

	@Override
	public Object getCellEditorValue() {

		return combo.getSelectedItem();
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		combo.setSelectedItem(value);
		combo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				fireEditingStopped();
			}
		});
		return combo;
	}

	@Override
	public boolean isCellEditable(EventObject arg0) {

		return true;
	}

}
