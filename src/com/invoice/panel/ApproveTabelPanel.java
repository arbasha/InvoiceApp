package com.invoice.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

import com.invoice.entity.Registration;
import com.invoice.listener.ApproveTableListener;
import com.invoice.listener.SaveRefreshListener;
import com.invoice.model.ApproverTableModel;
import com.invoice.swingutil.TableRowSorterTotal;

/**
 * @author Arshad
 *
 */
public class ApproveTabelPanel extends JPanel {

	private static final long serialVersionUID = -6242196784198125469L;

	private ApproverTableModel approverModel;

	private JTable approverTable;

	private SaveRefreshPanel sbPanel;

	private ApproveTableListener approveTableListener;

	public ApproveTabelPanel() {
		approverModel = new ApproverTableModel();

		approverTable = new JTable(approverModel) {
			private static final long serialVersionUID = 5558247665008191846L;
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
		approverTable.setRowHeight(30);
		approverTable.putClientProperty("terminateEditOnFocusLost",Boolean.TRUE);
		approverTable.getTableHeader().setBackground(Color.LIGHT_GRAY);
		approverTable.getTableHeader().setPreferredSize(new Dimension(10000, 30));
		UIManager.getDefaults().put("TableHeader.cellBorder", BorderFactory.createLineBorder(Color.GRAY));
		approverTable.setSelectionBackground(Color.LIGHT_GRAY);
		approverTable.setGridColor(Color.BLACK);
		approverTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		sbPanel = new SaveRefreshPanel();
		sbPanel.setSaveRefreshListener(new SaveRefreshListener() {

			@Override
			public void saveAccessState() {

				if (approveTableListener != null) {

					approverModel.fireTableDataChanged();
					approveTableListener.saveAccessList(approverModel
							.getApproveList());
					approverModel.fireTableDataChanged();
				}
			}

			@Override
			public void delete() {

				if (approveTableListener != null) {

					int row[] = approverTable.getSelectedRows();
					if (row.length == 0) {

						JOptionPane.showMessageDialog(ApproveTabelPanel.this,
								"Select the row(s) for decline!",
								"No row selected",
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						int action = JOptionPane.showConfirmDialog(
								ApproveTabelPanel.this,
								"Declining will delete the user?",
								"Confirm Delete", JOptionPane.OK_CANCEL_OPTION);
						if (action == JOptionPane.OK_OPTION) {

							approveTableListener.delete(
									approverModel.getApproveList(), row);
							approverModel.fireTableRowsDeleted(row[0],
									row[row.length - 1]);
						}
					}
				}
			}

			@Override
			public void getAccessList() {
				if (approveTableListener != null) {
					approveTableListener.getAccessList();
					approverModel.fireTableDataChanged();
				}

			}

		});

		setLayout(new BorderLayout());

		add(new JScrollPane(approverTable), BorderLayout.CENTER);
		add(sbPanel, BorderLayout.SOUTH);
	}

	public void setApprovingList(List<Registration> approvingList) {
		approverModel.setApproveList(approvingList);
		TableRowSorterTotal<ApproverTableModel> sorter = new TableRowSorterTotal<ApproverTableModel>(
				approverModel);
		approverTable.setRowSorter(sorter);
		refresh();
	}

	public void refresh() {
		approverModel.fireTableDataChanged();

	}

	public void setApproveTableListener(
			ApproveTableListener approverTableListener) {
		this.approveTableListener = approverTableListener;

	}

}
