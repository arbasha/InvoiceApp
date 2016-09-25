package com.invoice.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Date;
import java.util.List;

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
import com.invoice.listener.WeeklyTotalListener;
import com.invoice.model.WeeklyTotalModel;
import com.invoice.renderer.TextAreaRenderer;
import com.invoice.swingutil.TableRowSorterTotal;
import com.invoice.util.DateFormatter;

public class WeekTotalPanel extends JPanel {

	private static final long serialVersionUID = 876545398305206456L;

	private WeeklyTotalModel weekTotalModel;

	private JTable weeklyTotalTable;

	private ExportPanel exPanel;

	private WeeklyTotalListener weeklyTotalListener;

	DateFormatter df = DateFormatter.getInstance();

	public WeekTotalPanel() {

		weekTotalModel = new WeeklyTotalModel();

		weeklyTotalTable = new JTable(weekTotalModel) {

			private static final long serialVersionUID = 667524269972045979L;
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

				if (column == 9 && !(row == getRowCount() - 1)) {
					TableColumn tableColumn = weeklyTotalTable.getColumnModel()
							.getColumn(column);
					tableColumn.setCellRenderer(new TextAreaRenderer());
					tableColumn.setCellEditor(new TextAreaEditor());

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
			
			private static final long serialVersionUID = -984342781303256129L;

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

		weeklyTotalTable.setRowHeight(37);
		weeklyTotalTable.getTableHeader().setBackground(Color.LIGHT_GRAY);
		weeklyTotalTable.getTableHeader().setPreferredSize(
				new Dimension(10000, 30));
		weeklyTotalTable.setSelectionBackground(Color.LIGHT_GRAY);
		weeklyTotalTable.setGridColor(Color.BLACK);
		weeklyTotalTable.getColumnModel().getColumn(0)
				.setCellRenderer(tableCellRenderer);
		weeklyTotalTable.getColumnModel().getColumn(0).setPreferredWidth(90);
		weeklyTotalTable.getColumnModel().getColumn(0).setMinWidth(25);
		weeklyTotalTable.getColumnModel().getColumn(0).setMaxWidth(200);

		weeklyTotalTable.getColumnModel().getColumn(1)
				.setCellRenderer(tableCellRenderer);
		weeklyTotalTable.getColumnModel().getColumn(1).setPreferredWidth(90);
		weeklyTotalTable.getColumnModel().getColumn(1).setMinWidth(25);
		weeklyTotalTable.getColumnModel().getColumn(1).setMaxWidth(150);

		weeklyTotalTable.getColumnModel().getColumn(2)
				.setCellRenderer(centerRenderer);
		weeklyTotalTable.getColumnModel().getColumn(2).setPreferredWidth(100);
		weeklyTotalTable.getColumnModel().getColumn(2).setMinWidth(25);
		weeklyTotalTable.getColumnModel().getColumn(2).setMaxWidth(150);

		weeklyTotalTable.getColumnModel().getColumn(3)
				.setCellRenderer(centerRenderer);
		weeklyTotalTable.getColumnModel().getColumn(3).setPreferredWidth(90);
		weeklyTotalTable.getColumnModel().getColumn(3).setMinWidth(25);
		weeklyTotalTable.getColumnModel().getColumn(3).setMaxWidth(150);

		weeklyTotalTable.getColumnModel().getColumn(4)
				.setCellRenderer(centerRenderer);
		weeklyTotalTable.getColumnModel().getColumn(4).setPreferredWidth(100);
		weeklyTotalTable.getColumnModel().getColumn(4).setMinWidth(25);
		weeklyTotalTable.getColumnModel().getColumn(4).setMaxWidth(175);

		weeklyTotalTable.getColumnModel().getColumn(5)
				.setCellRenderer(centerRenderer);
		weeklyTotalTable.getColumnModel().getColumn(5).setPreferredWidth(100);
		weeklyTotalTable.getColumnModel().getColumn(5).setMinWidth(25);
		weeklyTotalTable.getColumnModel().getColumn(5).setMaxWidth(175);

		weeklyTotalTable.getColumnModel().getColumn(6)
				.setCellRenderer(centerRenderer);
		weeklyTotalTable.getColumnModel().getColumn(6).setPreferredWidth(100);
		weeklyTotalTable.getColumnModel().getColumn(6).setMinWidth(25);
		weeklyTotalTable.getColumnModel().getColumn(6).setMaxWidth(175);

		weeklyTotalTable.getColumnModel().getColumn(7)
				.setCellRenderer(centerRenderer);
		weeklyTotalTable.getColumnModel().getColumn(7).setPreferredWidth(75);
		weeklyTotalTable.getColumnModel().getColumn(7).setMinWidth(25);
		weeklyTotalTable.getColumnModel().getColumn(7).setMaxWidth(100);

		weeklyTotalTable.getColumnModel().getColumn(8)
				.setCellRenderer(centerRenderer);
		weeklyTotalTable.getColumnModel().getColumn(8).setPreferredWidth(50);
		weeklyTotalTable.getColumnModel().getColumn(8).setMinWidth(25);
		weeklyTotalTable.getColumnModel().getColumn(8).setMaxWidth(100);

		weeklyTotalTable.getColumnModel().getColumn(9).setPreferredWidth(250);
		weeklyTotalTable.getColumnModel().getColumn(9).setMinWidth(25);
		weeklyTotalTable.getColumnModel().getColumn(9).setMaxWidth(1000);

		exPanel = new ExportPanel();
		exPanel.setExportPaneListener(new ExportPanelListener() {

			@Override
			public void export() {
				if (weeklyTotalListener != null) {
					weeklyTotalListener.export(weekTotalModel.getWeekTotalList());
				}
			}

			@Override
			public void sendMail() {
				
			}
		});

		setLayout(new BorderLayout());
		add(new JScrollPane(weeklyTotalTable), BorderLayout.CENTER);
		add(exPanel, BorderLayout.SOUTH);
	}

	public void setTableListener(WeeklyTotalListener weeklyTotalListener) {
		this.weeklyTotalListener = weeklyTotalListener;

	}

	public void setTotalWeeklyList(List<WeeksTotal> totalWeekList) {

		weekTotalModel.setWeekTotalList(totalWeekList);

		TableRowSorterTotal<WeeklyTotalModel> sorter = new TableRowSorterTotal<WeeklyTotalModel>(
				weekTotalModel);
		weeklyTotalTable.setRowSorter(sorter);
		refreshTotalWeekModel();
	}

	public void refreshTotalWeekModel() {
		weekTotalModel.fireTableDataChanged();
	}

}
