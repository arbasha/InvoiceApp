package com.invoice.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;

import com.invoice.listener.DefaulterListTableListener;
import com.invoice.listener.ExportPanelListener;
import com.invoice.model.DefaulterListTableModel;
import com.invoice.pojo.DefaulterPojo;
import com.invoice.swingutil.TableRowSorterTotal;

public class DefaulterListTablePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(DefaulterListTablePanel.class);
	private DefaulterListTableModel defaulterModel;
	private JTable defaulterListTable;
	private ExportOrSendMailPanel exPanel;
	private String currentWeekDate;
	private DefaulterListTableListener defaulterTableListener;
	private BufferedImage image;

	@SuppressWarnings("serial")
	public DefaulterListTablePanel() {

		defaulterModel = new DefaulterListTableModel();

		defaulterListTable = new JTable(defaulterModel) {
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

		defaulterListTable.putClientProperty("terminateEditOnFocusLost",
				Boolean.TRUE);

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		defaulterListTable.getTableHeader().setBackground(Color.LIGHT_GRAY);
		defaulterListTable.getTableHeader().setPreferredSize(
				new Dimension(10000, 30));
		UIManager.getDefaults().put("TableHeader.cellBorder",
				BorderFactory.createLineBorder(Color.GRAY));
		defaulterListTable.setSelectionBackground(Color.LIGHT_GRAY);
		defaulterListTable.setGridColor(Color.BLACK);
		defaulterListTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		defaulterListTable.setRowHeight(30);

		defaulterListTable.getColumnModel().getColumn(0).setCellRenderer(
				centerRenderer);
		defaulterListTable.getColumnModel().getColumn(0).setPreferredWidth(90);
		defaulterListTable.getColumnModel().getColumn(0).setMinWidth(25);

		defaulterListTable.getColumnModel().getColumn(1).setCellRenderer(
				centerRenderer);
		defaulterListTable.getColumnModel().getColumn(1).setPreferredWidth(80);
		defaulterListTable.getColumnModel().getColumn(1).setMinWidth(25);

		defaulterListTable.getColumnModel().getColumn(2).setCellRenderer(
				centerRenderer);
		defaulterListTable.getColumnModel().getColumn(2).setPreferredWidth(80);
		defaulterListTable.getColumnModel().getColumn(2).setMinWidth(25);

		exPanel = new ExportOrSendMailPanel();
		exPanel.setExportPaneListener(new ExportPanelListener() {

			@Override
			public void export() {
				if (defaulterTableListener != null) {
					defaulterTableListener.export(defaulterModel
							.getDefaulterList());
				}
			}

			@Override
			public void sendMail() {
				if (defaulterTableListener != null) {
					String filePath = defaulterTableListener
							.export(defaulterModel.getDefaulterList());
					defaulterTableListener.sendMail(defaulterModel
							.getDefaulterList(), filePath);
				}

			}
		});

		setLayout(new BorderLayout());
		add(new JScrollPane(defaulterListTable), BorderLayout.CENTER);
		add(exPanel, BorderLayout.SOUTH);
	}

	public void setDefaulterList(List<DefaulterPojo> defaulterlist) {
		defaulterModel.setDefaulterList(defaulterlist);
		updateRowHeights();
		TableRowSorterTotal<DefaulterListTableModel> sorter = new TableRowSorterTotal<DefaulterListTableModel>(
				defaulterModel);
		defaulterListTable.setRowSorter(sorter);
		refreshWeekModel();
	}

	public void refreshWeekModel() {
		defaulterModel.fireTableDataChanged();

	}

	public void setTableListener(
			DefaulterListTableListener defaulterTableListener) {
		this.defaulterTableListener = defaulterTableListener;

	}

	public void setWeekDates(String currentWeekDate) {

		this.currentWeekDate = currentWeekDate;
	}

	public String getWeekDates() {

		return currentWeekDate;
	}

	private void updateRowHeights() {
		try {
			for (int row = 0; row < defaulterListTable.getRowCount(); row++) {
				int rowHeight = defaulterListTable.getRowHeight();

				for (int column = 0; column < defaulterListTable
						.getColumnCount(); column++) {
					Component comp = defaulterListTable.prepareRenderer(
							defaulterListTable.getCellRenderer(row, column),
							row, column);
					rowHeight = Math.max(rowHeight,
							comp.getPreferredSize().height);
				}

				defaulterListTable.setRowHeight(row, rowHeight);
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
