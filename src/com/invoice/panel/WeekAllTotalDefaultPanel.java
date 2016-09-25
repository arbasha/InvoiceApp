package com.invoice.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.invoice.editor.TextAreaEditor;
import com.invoice.entity.WeeksTotal;
import com.invoice.listener.ExportPanelListener;
import com.invoice.listener.WeekAllTotalDefaultListener;
import com.invoice.model.WeekAllTotalDefaultModel;
import com.invoice.renderer.TextAreaRenderer;
import com.invoice.swingutil.TableRowSorterTotal;
import com.invoice.util.DateFormatter;

/**
 * @author Arshad
 *
 */
public class WeekAllTotalDefaultPanel extends JPanel {

	private static final long serialVersionUID = -2542440139592943141L;

	private WeekAllTotalDefaultModel weekAllTotalDefaultModel;

	private JTable weeklyAllTotalTable;

	private ExportOrSendMailPanel exPanel;

	private WeekAllTotalDefaultListener weeklyAllTotalDefaultListener;

	private DateFormatter df = DateFormatter.getInstance();

	public WeekAllTotalDefaultPanel() {

		weekAllTotalDefaultModel = new WeekAllTotalDefaultModel();

		weeklyAllTotalTable = new JTable(weekAllTotalDefaultModel) {

			private static final long serialVersionUID = 5503832216080585099L;
			private boolean inLayout;

			@Override
			public boolean getScrollableTracksViewportWidth() {
				return hasExcessWidth();

			}

			@Override
			public void doLayout() {
				if (hasExcessWidth()) {
					// fool super
					autoResizeMode = AUTO_RESIZE_SUBSEQUENT_COLUMNS;
				}
				inLayout = true;
				super.doLayout();
				inLayout = false;
				autoResizeMode = AUTO_RESIZE_OFF;
			}

			protected boolean hasExcessWidth() {
				return getPreferredSize().width - getParent().getWidth() < 50;
			}

			@Override
			public void columnMarginChanged(ChangeEvent e) {

				saveRunningEditorResults();
				super.columnMarginChanged(e);
				if (isEditing()) {
					// JW: darn - cleanup to terminate editing ...
					removeEditor();
				}
				TableColumn resizingColumn = getTableHeader()
						.getResizingColumn();
				// Need to do this here, before the parent's
				// layout manager calls getPreferredSize().
				if (resizingColumn != null && autoResizeMode == AUTO_RESIZE_OFF
						&& !inLayout) {
					resizingColumn.setPreferredWidth(resizingColumn.getWidth());
				}
				resizeAndRepaint();

			}

			public TableCellRenderer getCellRenderer(int row, int column) {

				if (column == 11 && !(row == getRowCount() - 1)) {
					TableColumn tableColumn = weeklyAllTotalTable
							.getColumnModel().getColumn(column);
					if (tableColumn.getHeaderValue().toString()
							.equalsIgnoreCase("Remarks")) {
						tableColumn.setCellRenderer(new TextAreaRenderer());
						tableColumn.setCellEditor(new TextAreaEditor());
					}

				}
				if (row == getRowCount() - 1) {
					return getDefaultRenderer(String.class);
				}

				return super.getCellRenderer(row, column);
			}

			public Component prepareRenderer(TableCellRenderer renderer,
					int row, int column) {

				Component c = super.prepareRenderer(renderer, row, column);
				if (!isRowSelected(row)) {
					if (row == getRowCount() - 1) {

						c.setBackground(Color.GRAY);
						c.setForeground(Color.WHITE);
						return c;

					}
					c.setForeground(getForeground());

					c.setBackground(getBackground());
				}
				return c;
			}

			private void saveRunningEditorResults() {
				TableCellEditor cellEditor = getCellEditor();
				if (cellEditor != null) {
					if (!cellEditor.stopCellEditing()) {
						cellEditor.cancelCellEditing();
					}
				}
			}

			/**
			 * Overridden to save the editor state when the user clicks the
			 * tables header.
			 * 
			 * @param e
			 */
			public void columnMoved(TableColumnModelEvent e) {
				saveRunningEditorResults();
				super.columnMoved(e);
			}

		};

		TableCellRenderer tableCellRenderer = new DefaultTableCellRenderer() {

			private static final long serialVersionUID = 2335392897194440837L;

			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				if (value instanceof Date) {
					value = df.formatddMMMyyyy((Date) value);
				}
				return super.getTableCellRendererComponent(table, value,
						isSelected, hasFocus, row, column);
			}
		};

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		weeklyAllTotalTable.setRowHeight(37);
		weeklyAllTotalTable.getTableHeader().setBackground(Color.LIGHT_GRAY);
		weeklyAllTotalTable.getTableHeader().setPreferredSize(
				new Dimension(10000, 30));
		weeklyAllTotalTable.setSelectionBackground(Color.LIGHT_GRAY);
		weeklyAllTotalTable.setGridColor(Color.BLACK);
		weeklyAllTotalTable.setRowHeight(37);
		weeklyAllTotalTable.getTableHeader().setBackground(Color.LIGHT_GRAY);
		weeklyAllTotalTable.getTableHeader().setPreferredSize(
				new Dimension(10000, 30));
		weeklyAllTotalTable.setSelectionBackground(Color.LIGHT_GRAY);
		weeklyAllTotalTable.setGridColor(Color.BLACK);
		weeklyAllTotalTable.getColumnModel().getColumn(0)
				.setPreferredWidth(110);
		weeklyAllTotalTable.getColumnModel().getColumn(0).setMinWidth(25);
		weeklyAllTotalTable.getColumnModel().getColumn(0).setMaxWidth(200);

		weeklyAllTotalTable.getColumnModel().getColumn(1)
				.setPreferredWidth(110);
		weeklyAllTotalTable.getColumnModel().getColumn(1).setMinWidth(25);
		weeklyAllTotalTable.getColumnModel().getColumn(1).setMaxWidth(150);

		weeklyAllTotalTable.getColumnModel().getColumn(2).setCellRenderer(
				tableCellRenderer);
		weeklyAllTotalTable.getColumnModel().getColumn(2).setPreferredWidth(90);
		weeklyAllTotalTable.getColumnModel().getColumn(2).setMinWidth(25);
		weeklyAllTotalTable.getColumnModel().getColumn(2).setMaxWidth(150);

		weeklyAllTotalTable.getColumnModel().getColumn(3).setCellRenderer(
				tableCellRenderer);
		weeklyAllTotalTable.getColumnModel().getColumn(3).setPreferredWidth(80);
		weeklyAllTotalTable.getColumnModel().getColumn(3).setMinWidth(25);
		weeklyAllTotalTable.getColumnModel().getColumn(3).setMaxWidth(150);

		weeklyAllTotalTable.getColumnModel().getColumn(4).setCellRenderer(
				centerRenderer);
		weeklyAllTotalTable.getColumnModel().getColumn(4)
				.setPreferredWidth(100);
		weeklyAllTotalTable.getColumnModel().getColumn(4).setMinWidth(25);
		weeklyAllTotalTable.getColumnModel().getColumn(4).setMaxWidth(175);

		weeklyAllTotalTable.getColumnModel().getColumn(5).setCellRenderer(
				centerRenderer);
		weeklyAllTotalTable.getColumnModel().getColumn(5)
				.setPreferredWidth(100);
		weeklyAllTotalTable.getColumnModel().getColumn(5).setMinWidth(25);
		weeklyAllTotalTable.getColumnModel().getColumn(5).setMaxWidth(175);

		weeklyAllTotalTable.getColumnModel().getColumn(6).setCellRenderer(
				centerRenderer);
		weeklyAllTotalTable.getColumnModel().getColumn(6)
				.setPreferredWidth(100);
		weeklyAllTotalTable.getColumnModel().getColumn(6).setMinWidth(25);
		weeklyAllTotalTable.getColumnModel().getColumn(6).setMaxWidth(175);

		weeklyAllTotalTable.getColumnModel().getColumn(7).setCellRenderer(
				centerRenderer);
		weeklyAllTotalTable.getColumnModel().getColumn(7)
				.setPreferredWidth(100);
		weeklyAllTotalTable.getColumnModel().getColumn(7).setMinWidth(25);
		weeklyAllTotalTable.getColumnModel().getColumn(7).setMaxWidth(175);

		weeklyAllTotalTable.getColumnModel().getColumn(8).setCellRenderer(
				centerRenderer);
		weeklyAllTotalTable.getColumnModel().getColumn(8).setPreferredWidth(90);
		weeklyAllTotalTable.getColumnModel().getColumn(8).setMinWidth(25);
		weeklyAllTotalTable.getColumnModel().getColumn(8).setMaxWidth(175);

		weeklyAllTotalTable.getColumnModel().getColumn(9).setCellRenderer(
				centerRenderer);

		weeklyAllTotalTable.getColumnModel().getColumn(9).setPreferredWidth(75);
		weeklyAllTotalTable.getColumnModel().getColumn(9).setMinWidth(25);
		weeklyAllTotalTable.getColumnModel().getColumn(9).setMaxWidth(175);

		weeklyAllTotalTable.getColumnModel().getColumn(10).setCellRenderer(
				centerRenderer);
		weeklyAllTotalTable.getColumnModel().getColumn(10)
				.setPreferredWidth(50);
		weeklyAllTotalTable.getColumnModel().getColumn(10).setMinWidth(25);
		weeklyAllTotalTable.getColumnModel().getColumn(10).setMaxWidth(175);

		weeklyAllTotalTable.getColumnModel().getColumn(11).setPreferredWidth(
				250);
		weeklyAllTotalTable.getColumnModel().getColumn(11).setMinWidth(25);
		weeklyAllTotalTable.getColumnModel().getColumn(11).setMaxWidth(1000);

		exPanel = new ExportOrSendMailPanel();
		exPanel.setExportPaneListener(new ExportPanelListener() {

			@Override
			public void export() {
				if (weeklyAllTotalDefaultListener != null) {
					weeklyAllTotalDefaultListener
							.export(getTotalDefaultViewList());
				}
			}

			@Override
			public void sendMail() {
				if (weeklyAllTotalDefaultListener != null) {
					String filePath = weeklyAllTotalDefaultListener
							.export(getTotalDefaultViewList());
					if (!filePath.equalsIgnoreCase("")) {
						weeklyAllTotalDefaultListener.sendMail(
								getTotalDefaultViewList(), filePath);
					} else {
						JOptionPane.showMessageDialog(null,
								"An unknown error occured while sending mail",
								"", JOptionPane.INFORMATION_MESSAGE);
					}
				}

			}
		});

		setLayout(new BorderLayout());
		add(new JScrollPane(weeklyAllTotalTable), BorderLayout.CENTER);
		add(exPanel, BorderLayout.SOUTH);
	}

	public void setTableListener(
			WeekAllTotalDefaultListener weeklyAllTotalDefaultListener) {
		this.weeklyAllTotalDefaultListener = weeklyAllTotalDefaultListener;

	}

	public void setAllTotalWeeklyDefaultList(
			List<WeeksTotal> totalWeekDefaultList) {

		weekAllTotalDefaultModel.setWeekAllTotalList(totalWeekDefaultList);
		refreshAllTotalWeekModel();
		TableRowSorterTotal<WeekAllTotalDefaultModel> sorter = new TableRowSorterTotal<WeekAllTotalDefaultModel>(
				weekAllTotalDefaultModel);
		weeklyAllTotalTable.setRowSorter(sorter);
		updateRowHeights();
		refreshAllTotalWeekModel();
	}

	public void refreshAllTotalWeekModel() {
		weekAllTotalDefaultModel.fireTableDataChanged();
	}

	private void updateRowHeights() {
		try {
			for (int row = 0; row < weeklyAllTotalTable.getRowCount(); row++) {
				int rowHeight = weeklyAllTotalTable.getRowHeight();

				for (int column = 0; column < weeklyAllTotalTable
						.getColumnCount(); column++) {
					Component comp = weeklyAllTotalTable.prepareRenderer(
							weeklyAllTotalTable.getCellRenderer(row, column),
							row, column);
					rowHeight = Math.max(rowHeight,
							comp.getPreferredSize().height);
				}

				weeklyAllTotalTable.setRowHeight(row, rowHeight);
			}
		} catch (ClassCastException e) {
		}

	}

	public List<WeeksTotal> getTotalDefaultViewList() {
		return weekAllTotalDefaultModel.getWeekAllTotalList();
	}

	public void showMail(boolean flag) {
		exPanel.showMail(flag);

	}
}
