package com.invoice.panel;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.invoice.listener.SavePanelListener;

/**
 * @author Arshad
 *
 */
public class SaveButtonPanel extends JPanel {

	private static final long serialVersionUID = -7727269952996846451L;
	private JButton saveBtn;
	private JButton cpyBtn;
	private SavePanelListener savePanelListener;

	public SaveButtonPanel() {

		saveBtn = new JButton("Save");
		cpyBtn = new JButton("Copy - Last Week Timesheet");
		setLayout(new FlowLayout());
		saveBtn.setFocusable(false);
		cpyBtn.setFocusable(false);
		add(saveBtn);
		add(cpyBtn);

		saveBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (savePanelListener != null) {
					savePanelListener.save();
				}

			}
		});

		cpyBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (savePanelListener != null) {
					savePanelListener.copy();
				}

			}
		});

		Border innerborder = BorderFactory.createEtchedBorder();
		Border outerborder = BorderFactory.createEmptyBorder();

		setBorder(BorderFactory.createCompoundBorder(outerborder, innerborder));
	}

	public void setSavePaneListener(SavePanelListener savePanelListener) {
		this.savePanelListener = savePanelListener;

	}

}
