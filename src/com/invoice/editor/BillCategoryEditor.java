package com.invoice.editor;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import com.invoice.pojo.BillCategory;

/**
 * @author Arshad
 *
 */
public class BillCategoryEditor extends AbstractCellEditor implements
		TableCellEditor {

	private static final long serialVersionUID = 8980492641292692505L;
	@SuppressWarnings("rawtypes")
	private JComboBox combo;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public BillCategoryEditor() {
		combo = new JComboBox(BillCategory.values());

	}

	@Override
	public Object getCellEditorValue() {

		return combo.getSelectedItem();
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		combo.setSelectedItem(value);
		combo.setBackground(Color.LIGHT_GRAY);
		combo.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
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
