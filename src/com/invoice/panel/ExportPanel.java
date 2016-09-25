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
public class ExportPanel extends JPanel {

	private static final long serialVersionUID = -3201284383336624689L;
	private JButton exportBtn;
	private ExportPanelListener exportPanelListener;

	public ExportPanel() {

		exportBtn = new JButton("Export");
		exportBtn.setFocusable(false);
		setLayout(new FlowLayout());
		add(exportBtn);

		exportBtn.addActionListener(new ActionListener() {

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

	public void setExportPaneListener(ExportPanelListener exportPanelListener) {
		this.exportPanelListener = exportPanelListener;

	}

}
