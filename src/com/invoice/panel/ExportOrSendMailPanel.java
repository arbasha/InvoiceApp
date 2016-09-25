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
public class ExportOrSendMailPanel extends JPanel {

	private static final long serialVersionUID = -5744673432951433990L;
	private JButton exportBtn;
	private JButton sendMailBtn;
	private ExportPanelListener exportPanelListener;

	public ExportOrSendMailPanel() {

		exportBtn = new JButton("Export");
		exportBtn.setFocusable(false);

		sendMailBtn = new JButton("Send Mail");
		exportBtn.setFocusable(false);

		setLayout(new FlowLayout());
		add(exportBtn);
		add(sendMailBtn);

		exportBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (exportPanelListener != null) {
					exportPanelListener.export();
				}

			}
		});

		sendMailBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (exportPanelListener != null) {
					exportPanelListener.export();
					exportPanelListener.sendMail();

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

	public void showMail(boolean flag) {

		sendMailBtn.setVisible(flag);
	}

}
