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

import com.invoice.editor.BillCategoryEditor;
import com.invoice.editor.LeaveTypeCategoryEditor;
import com.invoice.entity.Timesheet;
import com.invoice.listener.ExportPanelListener;
import com.invoice.listener.WeekDailyListener;
import com.invoice.model.WeeklyDailyModel;
import com.invoice.pojo.BillCategory;
import com.invoice.pojo.LeaveTypeCategory;
import com.invoice.renderer.BillCategoryRenderer;
import com.invoice.renderer.LeaveTypeCategoryRenderer;
import com.invoice.swingutil.TableRowSorterTotal;
import com.invoice.util.DateFormatter;

/**
 * @author Arshad
 *
 */
public class WeekDailyPanel extends JPanel {

	private static final long serialVersionUID = 7604177916797261038L;

	private WeeklyDailyModel weekDailyModel;

	private JTable weekDailyTable;

	private DailyExportPanel exPanel;

	private WeekDailyListener weekDailyListener;
	DateFormatter df = DateFormatter.getInstance();

	@SuppressWarnings("serial")
	public WeekDailyPanel() {

		weekDailyModel = new WeeklyDailyModel();

		weekDailyTable = new JTable(weekDailyModel) {
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
				if (row == getRowCount() - 1) {
					/* Class cellClass = getValueAt(row, column).getClass(); */

					/* Class cellClass = getValueAt(row, column).getClass(); */
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
					if (row >= 0) {

						if (getValueAt(row, 0) instanceof LeaveTypeCategory) {
							LeaveTypeCategory leaveType = (LeaveTypeCategory) getValueAt(
									row, 0);
							if (!leaveType.equals(LeaveTypeCategory.EMPTY)) {
								c.setBackground(Color.decode("#FFCB05"));
								c.setForeground(Color.BLACK);
								return c;
							}

						}
						if (getValueAt(row, 1) instanceof LeaveTypeCategory) {
							LeaveTypeCategory leaveType = (LeaveTypeCategory) getValueAt(
									row, 1);
							if (!leaveType.equals(LeaveTypeCategory.EMPTY)) {
								c.setBackground(Color.decode("#FFCB05"));
								c.setForeground(Color.BLACK);
								return c;
							}

						}
						if (getValueAt(row, 2) instanceof LeaveTypeCategory) {
							LeaveTypeCategory leaveType = (LeaveTypeCategory) getValueAt(
									row, 2);
							if (!leaveType.equals(LeaveTypeCategory.EMPTY)) {
								c.setBackground(Color.decode("#FFCB05"));
								c.setForeground(Color.BLACK);
								return c;
							}

						}
						if (getValueAt(row, 3) instanceof LeaveTypeCategory) {
							LeaveTypeCategory leaveType = (LeaveTypeCategory) getValueAt(
									row, 3);
							if (!leaveType.equals(LeaveTypeCategory.EMPTY)) {
								c.setBackground(Color.decode("#FFCB05"));
								c.setForeground(Color.BLACK);
								return c;
							}

						}
						if (getValueAt(row, 4) instanceof LeaveTypeCategory) {
							LeaveTypeCategory leaveType = (LeaveTypeCategory) getValueAt(
									row, 4);
							if (!leaveType.equals(LeaveTypeCategory.EMPTY)) {
								c.setBackground(Color.decode("#FFCB05"));
								c.setForeground(Color.BLACK);
								return c;
							}

						}
						if (getValueAt(row, 5) instanceof LeaveTypeCategory) {
							LeaveTypeCategory leaveType = (LeaveTypeCategory) getValueAt(
									row, 5);
							if (!leaveType.equals(LeaveTypeCategory.EMPTY)) {
								c.setBackground(Color.decode("#FFCB05"));
								c.setForeground(Color.BLACK);
								return c;
							}

						}
						if (getValueAt(row, 6) instanceof LeaveTypeCategory) {
							LeaveTypeCategory leaveType = (LeaveTypeCategory) getValueAt(
									row, 6);
							if (!leaveType.equals(LeaveTypeCategory.EMPTY)) {
								c.setBackground(Color.decode("#FFCB05"));
								c.setForeground(Color.BLACK);
								return c;
							}

						}
						if (getValueAt(row, 7) instanceof LeaveTypeCategory) {
							LeaveTypeCategory leaveType = (LeaveTypeCategory) getValueAt(
									row, 7);
							if (!leaveType.equals(LeaveTypeCategory.EMPTY)) {
								c.setBackground(Color.decode("#FFCB05"));
								c.setForeground(Color.BLACK);
								return c;
							}

						}
						if (getValueAt(row, 8) instanceof LeaveTypeCategory) {
							LeaveTypeCategory leaveType = (LeaveTypeCategory) getValueAt(
									row, 8);
							if (!leaveType.equals(LeaveTypeCategory.EMPTY)) {
								c.setBackground(Color.decode("#FFCB05"));
								c.setForeground(Color.BLACK);
								return c;
							}

						}
						if (getValueAt(row, 9) instanceof LeaveTypeCategory) {
							LeaveTypeCategory leaveType = (LeaveTypeCategory) getValueAt(
									row, 9);
							if (!leaveType.equals(LeaveTypeCategory.EMPTY)) {
								c.setBackground(Color.decode("#FFCB05"));
								c.setForeground(Color.BLACK);
								return c;
							}

						}

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

			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				if (value instanceof Date) {
					value = df.formatEMMMddyyyy((Date) value);
				}
				return super.getTableCellRendererComponent(table, value,
						isSelected, hasFocus, row, column);
			}
		};

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		weekDailyTable.setDefaultRenderer(LeaveTypeCategory.class,
				new LeaveTypeCategoryRenderer());
		weekDailyTable.setDefaultEditor(LeaveTypeCategory.class,
				new LeaveTypeCategoryEditor());

		weekDailyTable.setDefaultRenderer(BillCategory.class,
				new BillCategoryRenderer());
		weekDailyTable.setDefaultEditor(BillCategory.class,
				new BillCategoryEditor());
		weekDailyTable.setRowHeight(30);
		weekDailyTable.getTableHeader().setBackground(Color.LIGHT_GRAY);
		weekDailyTable.getTableHeader().setPreferredSize(
				new Dimension(10000, 30));
		weekDailyTable.setSelectionBackground(Color.LIGHT_GRAY);
		weekDailyTable.setGridColor(Color.BLACK);
		weekDailyTable.getColumnModel().getColumn(0).setCellRenderer(
				tableCellRenderer);
		weekDailyTable.getColumnModel().getColumn(0).setPreferredWidth(150);
		weekDailyTable.getColumnModel().getColumn(0).setMinWidth(25);
		weekDailyTable.getColumnModel().getColumn(0).setMaxWidth(200);

		weekDailyTable.getColumnModel().getColumn(1).setCellRenderer(
				centerRenderer);
		weekDailyTable.getColumnModel().getColumn(1).setPreferredWidth(85);
		weekDailyTable.getColumnModel().getColumn(1).setMinWidth(25);
		weekDailyTable.getColumnModel().getColumn(1).setMaxWidth(150);

		weekDailyTable.getColumnModel().getColumn(2).setCellRenderer(
				centerRenderer);

		weekDailyTable.getColumnModel().getColumn(2).setPreferredWidth(90);
		weekDailyTable.getColumnModel().getColumn(2).setMinWidth(25);
		weekDailyTable.getColumnModel().getColumn(2).setMaxWidth(150);

		weekDailyTable.getColumnModel().getColumn(3).setCellRenderer(
				centerRenderer);
		weekDailyTable.getColumnModel().getColumn(3).setPreferredWidth(70);
		weekDailyTable.getColumnModel().getColumn(3).setMinWidth(25);
		weekDailyTable.getColumnModel().getColumn(3).setMaxWidth(150);

		weekDailyTable.getColumnModel().getColumn(4).setPreferredWidth(90);
		weekDailyTable.getColumnModel().getColumn(4).setMinWidth(100);
		weekDailyTable.getColumnModel().getColumn(4).setMaxWidth(175);

		weekDailyTable.getColumnModel().getColumn(5).setPreferredWidth(100);
		weekDailyTable.getColumnModel().getColumn(5).setMinWidth(75);
		weekDailyTable.getColumnModel().getColumn(5).setMaxWidth(175);

		weekDailyTable.getColumnModel().getColumn(6).setPreferredWidth(120);
		weekDailyTable.getColumnModel().getColumn(6).setMinWidth(25);
		weekDailyTable.getColumnModel().getColumn(6).setMaxWidth(150);

		weekDailyTable.getColumnModel().getColumn(7).setCellRenderer(
				centerRenderer);

		weekDailyTable.getColumnModel().getColumn(7).setPreferredWidth(50);
		weekDailyTable.getColumnModel().getColumn(7).setMinWidth(25);
		weekDailyTable.getColumnModel().getColumn(7).setMaxWidth(150);

		weekDailyTable.getColumnModel().getColumn(8).setPreferredWidth(250);
		weekDailyTable.getColumnModel().getColumn(8).setMinWidth(25);
		weekDailyTable.getColumnModel().getColumn(8).setMaxWidth(1500);

		weekDailyTable.getColumnModel().getColumn(9).setCellRenderer(
				centerRenderer);
		weekDailyTable.getColumnModel().getColumn(9).setPreferredWidth(90);
		weekDailyTable.getColumnModel().getColumn(9).setMinWidth(25);
		weekDailyTable.getColumnModel().getColumn(9).setMaxWidth(150);

		exPanel = new DailyExportPanel();
		exPanel.setDailyExportPaneListener(new ExportPanelListener() {

			@Override
			public void export() {
				if (weekDailyListener != null) {
					weekDailyListener.export(getWeekDailyList());
				}
			}

			@Override
			public void sendMail() {
				
			}
		});

		setLayout(new BorderLayout());
		add(new JScrollPane(weekDailyTable), BorderLayout.CENTER);
		add(exPanel, BorderLayout.SOUTH);
	}

	public void setTableListener(WeekDailyListener weeklDailyListener) {
		this.weekDailyListener = weeklDailyListener;

	}

	public void setWeekDailyList(List<Timesheet> weekDailyList) {

		weekDailyModel.setWeekDailyList(weekDailyList);

		TableRowSorterTotal<WeeklyDailyModel> sorter = new TableRowSorterTotal<WeeklyDailyModel>(
				weekDailyModel);
		weekDailyTable.setRowSorter(sorter);
		refreshWeekDailyModel();
	}

	public List<Timesheet> getWeekDailyList() {

		refreshWeekDailyModel();
		return weekDailyModel.getWeekDailyList();

	}

	public void refreshWeekDailyModel() {
		weekDailyModel.fireTableDataChanged();
	}

}
