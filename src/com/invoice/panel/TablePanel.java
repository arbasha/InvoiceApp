package com.invoice.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
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
import com.invoice.listener.SavePanelListener;
import com.invoice.listener.TimeTableListener;
import com.invoice.model.WeeklyTableModel;
import com.invoice.pojo.BillCategory;
import com.invoice.pojo.Employee;
import com.invoice.pojo.LeaveTypeCategory;
import com.invoice.renderer.BillCategoryRenderer;
import com.invoice.renderer.LeaveTypeCategoryRenderer;

/**
 * @author Arshad
 *
 */
public class TablePanel extends JPanel {

	private static final long serialVersionUID = -4664946484013621217L;
	private WeeklyTableModel weekModel;
	private JTable weeklyTable;
	private JPopupMenu jpopMenu;
	private SaveButtonPanel sbPanel;
	private ExportPanel exPanel;
	private String currentWeekDate;
	private TimeTableListener timeTableListener;
	private BufferedImage image;

	public TablePanel() {

		weekModel = new WeeklyTableModel();

		weeklyTable = new JTable(weekModel) {

			private static final long serialVersionUID = -6793871256228479559L;
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
				if (row == 7) {
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
					if (row == 7) {

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

		weeklyTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		weeklyTable.getTableHeader().setBackground(Color.LIGHT_GRAY);
		weeklyTable.getTableHeader().setPreferredSize(new Dimension(10000, 30));
		UIManager.getDefaults().put("TableHeader.cellBorder",
				BorderFactory.createLineBorder(Color.GRAY));
		weeklyTable.setSelectionBackground(Color.LIGHT_GRAY);
		weeklyTable.setGridColor(Color.BLACK);
		weeklyTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		/*
		 * weekModel.addRow(new Object[]{"Date", "Hours Worked",
		 * "Regular Hours", "Off Hours", "Leave Type", "Status", "Remarks",
		 * "Reported Hours" });
		 */

		weeklyTable.setDefaultRenderer(LeaveTypeCategory.class,
				new LeaveTypeCategoryRenderer());
		weeklyTable.setDefaultEditor(LeaveTypeCategory.class,
				new LeaveTypeCategoryEditor());

		weeklyTable.setDefaultRenderer(BillCategory.class,
				new BillCategoryRenderer());
		weeklyTable.setDefaultEditor(BillCategory.class,
				new BillCategoryEditor());
		weeklyTable.setRowHeight(30);
		weeklyTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		// set column sizes
		weeklyTable.getColumnModel().getColumn(0).setPreferredWidth(150);
		weeklyTable.getColumnModel().getColumn(0).setMinWidth(25);
		weeklyTable.getColumnModel().getColumn(0).setMaxWidth(200);

		weeklyTable.getColumnModel().getColumn(1)
				.setCellRenderer(centerRenderer);
		weeklyTable.getColumnModel().getColumn(1).setPreferredWidth(85);
		weeklyTable.getColumnModel().getColumn(1).setMinWidth(25);
		weeklyTable.getColumnModel().getColumn(1).setMaxWidth(150);

		weeklyTable.getColumnModel().getColumn(2)
				.setCellRenderer(centerRenderer);
		weeklyTable.getColumnModel().getColumn(2).setPreferredWidth(90);
		weeklyTable.getColumnModel().getColumn(2).setMinWidth(25);
		weeklyTable.getColumnModel().getColumn(2).setMaxWidth(150);

		weeklyTable.getColumnModel().getColumn(3)
				.setCellRenderer(centerRenderer);
		weeklyTable.getColumnModel().getColumn(3).setPreferredWidth(70);
		weeklyTable.getColumnModel().getColumn(3).setMinWidth(25);
		weeklyTable.getColumnModel().getColumn(3).setMaxWidth(150);

		weeklyTable.getColumnModel().getColumn(4).setPreferredWidth(90);
		weeklyTable.getColumnModel().getColumn(4).setMinWidth(100);
		weeklyTable.getColumnModel().getColumn(4).setMaxWidth(175);

		weeklyTable.getColumnModel().getColumn(5).setPreferredWidth(100);
		weeklyTable.getColumnModel().getColumn(5).setMinWidth(75);
		weeklyTable.getColumnModel().getColumn(5).setMaxWidth(175);

		weeklyTable.getColumnModel().getColumn(6).setPreferredWidth(90);
		weeklyTable.getColumnModel().getColumn(6).setMinWidth(25);
		weeklyTable.getColumnModel().getColumn(6).setMaxWidth(150);

		weeklyTable.getColumnModel().getColumn(7)
				.setCellRenderer(centerRenderer);
		weeklyTable.getColumnModel().getColumn(7).setPreferredWidth(50);
		weeklyTable.getColumnModel().getColumn(7).setMinWidth(25);
		weeklyTable.getColumnModel().getColumn(7).setMaxWidth(150);

		weeklyTable.getColumnModel().getColumn(8).setPreferredWidth(250);
		weeklyTable.getColumnModel().getColumn(8).setMinWidth(25);
		weeklyTable.getColumnModel().getColumn(8).setMaxWidth(1500);

		weeklyTable.getColumnModel().getColumn(9)
				.setCellRenderer(centerRenderer);
		weeklyTable.getColumnModel().getColumn(9).setPreferredWidth(90);
		weeklyTable.getColumnModel().getColumn(9).setMinWidth(25);
		weeklyTable.getColumnModel().getColumn(9).setMaxWidth(150);

		jpopMenu = new JPopupMenu();
		JMenuItem deleteRow = new JMenuItem("Delete Row");
		jpopMenu.add(deleteRow);
		add(jpopMenu);
		/*
		 * table.addMouseListener(new MouseAdapter() {
		 * 
		 * @Override public void mousePressed(MouseEvent e) { if (e.getButton()
		 * == MouseEvent.BUTTON3) {
		 * 
		 * int row = table.rowAtPoint(e.getPoint());
		 * 
		 * int row[]= table.getSelectedRows();
		 * 
		 * table.getSelectionModel().setSelectionInterval(row, row);
		 * 
		 * jpopMenu.show(table, e.getX(), e.getY()); }
		 * 
		 * }
		 * 
		 * }); deleteRow.addActionListener(new ActionListener() {
		 * 
		 * @Override public void actionPerformed(ActionEvent e) {
		 * 
		 * int row = table.getSelectedRow(); if (employeeTableListener != null)
		 * { employeeTableListener.removeRow(row); }
		 * empModel.fireTableRowsDeleted(row, row); table.clearSelection();
		 * 
		 * } });
		 */
		sbPanel = new SaveButtonPanel();

		sbPanel.setSavePaneListener(new SavePanelListener() {

			@Override
			public void save() {

				if (weeklyTable.isEditing())
					weeklyTable.getCellEditor().stopCellEditing();

				if (timeTableListener != null) {

					weekModel.fireTableDataChanged();
					timeTableListener.save(weekModel.getTimeList());
				}
			}

			@Override
			public void copy() {

			}

		});

		exPanel = new ExportPanel();
		exPanel.setExportPaneListener(new ExportPanelListener() {

			@Override
			public void export() {

			}

			@Override
			public void sendMail() {

			}
		});

		setLayout(new BorderLayout());
		add(new JScrollPane(weeklyTable), BorderLayout.CENTER);
		add(sbPanel, BorderLayout.SOUTH);
	}

	public void setEmployeeList(List<Employee> emplist) {
		/* empModel.setEmpList(emplist); */
	}

	public void setWeeklyList(List<Timesheet> timelist) {
		weekModel.setTimeList(timelist);
		updateRowHeights();
		refreshWeekModel();
	}

	public void refreshWeekModel() {
		weekModel.fireTableDataChanged();

	}

	public void setTableListener(TimeTableListener timeTableListener) {
		this.timeTableListener = timeTableListener;

	}

	public void setWeekDates(String currentWeekDate) {

		this.currentWeekDate = currentWeekDate;
	}

	public String getWeekDates() {

		return currentWeekDate;
	}

	private void updateRowHeights() {
		try {
			for (int row = 0; row < weeklyTable.getRowCount(); row++) {
				int rowHeight = weeklyTable.getRowHeight();

				for (int column = 0; column < weeklyTable.getColumnCount(); column++) {
					Component comp = weeklyTable.prepareRenderer(
							weeklyTable.getCellRenderer(row, column), row,
							column);
					rowHeight = Math.max(rowHeight,
							comp.getPreferredSize().height);
				}

				weeklyTable.setRowHeight(row, rowHeight);
			}
		} catch (ClassCastException e) {
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null); // see javadoc
		// for more
		// info on
		// the
		// parameters
	}

}
