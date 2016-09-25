package com.invoice.panel;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.invoice.listener.SaveRefreshListener;

/**
 * @author Arshad
 *
 */
public class SaveRefreshPanel extends JPanel {

	private static final long serialVersionUID = 3510445717185416582L;
	private JButton saveBtn;
	private JButton refreshBtn;
	private JButton declineBtn;
	private SaveRefreshListener saveRefreshListener;

	public SaveRefreshPanel() {

		saveBtn = new JButton("Approve");
		saveBtn.setFocusable(false);
		refreshBtn = new JButton("Refresh");
		refreshBtn.setFocusable(false);
		declineBtn = new JButton("Decline");
		declineBtn.setFocusable(false);
		setLayout(new FlowLayout());
		add(saveBtn);
		add(refreshBtn);
		add(declineBtn);

		saveBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (saveRefreshListener != null) {
					saveRefreshListener.saveAccessState();
				}

			}
		});
		declineBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (saveRefreshListener != null) {
					saveRefreshListener.delete();
				}

			}
		});

		refreshBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (saveRefreshListener != null) {
					saveRefreshListener.getAccessList();
				}

			}
		});

		Border innerborder = BorderFactory.createEtchedBorder();
		Border outerborder = BorderFactory.createEmptyBorder();

		setBorder(BorderFactory.createCompoundBorder(outerborder, innerborder));
	}

	public void setSaveRefreshListener(SaveRefreshListener saveRefreshListener) {
		this.saveRefreshListener = saveRefreshListener;

	}
}
