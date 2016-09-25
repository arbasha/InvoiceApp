package com.invoice.panel;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.invoice.listener.ExportPanelListener;

/**
 * @author Arshad
 *
 */
public class DailyExportPanel extends JPanel {

	private static final long serialVersionUID = 3891669414881149949L;
	private JButton ExportBtn;
	private ExportPanelListener exportPanelListener;

	public DailyExportPanel() {

		ExportBtn = new JButton("Export");
		ExportBtn.setFocusable(false);
		setLayout(new FlowLayout());
		add(ExportBtn);

		ExportBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (exportPanelListener != null) {
					exportPanelListener.export();
				}

			}
		});

		Border innerborder = BorderFactory.createEtchedBorder();
		Border outerborder = BorderFactory.createEmptyBorder();

		setBorder(BorderFactory.createCompoundBorder(outerborder, innerborder));
	}

	public void setDailyExportPaneListener(
			ExportPanelListener exportPanelListener) {
		this.exportPanelListener = exportPanelListener;

	}

}
