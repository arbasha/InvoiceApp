package com.invoice.model;

import java.util.Hashtable;

import javax.swing.table.TableCellEditor;

/**
 * @author Arshad
 *
 */
public class RowEditorModel {
	@SuppressWarnings("rawtypes")
	private Hashtable data;

	@SuppressWarnings("rawtypes")
	public RowEditorModel() {
		data = new Hashtable();
	}

	@SuppressWarnings("unchecked")
	public void addEditorForRow(int row, TableCellEditor e) {
		data.put(new Integer(row), e);
	}

	public void removeEditorForRow(int row) {
		data.remove(new Integer(row));
	}

	public TableCellEditor getEditor(int row) {
		return (TableCellEditor) data.get(new Integer(row));
	}
}
