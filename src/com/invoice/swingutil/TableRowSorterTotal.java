package com.invoice.swingutil;

import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 * @author Arshad
 *
 * @param <MyTableModel>
 */
public class TableRowSorterTotal<MyTableModel extends TableModel> extends
		TableRowSorter<TableModel> {

	public TableRowSorterTotal(MyTableModel model) {
		super(model);
	}

	public int convertRowIndexToModel(int index)
    {
        int maxRow = super.getViewRowCount();
        if (index >= maxRow)
            return index;
        return super.convertRowIndexToModel(index);
    }

    public int convertRowIndexToView(int index) 
    {
        int maxRow = super.getModelRowCount();
        if (index > maxRow)
            return index;
        return super.convertRowIndexToView(index);
    }

    public int getViewRowCount() 
    {
        return super.getViewRowCount() + 1;
    }
}
