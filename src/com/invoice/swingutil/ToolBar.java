package com.invoice.swingutil;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.invoice.listener.ButtonListener;
import com.invoice.pojo.UserDetailHolder;

public class ToolBar extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3911787068370433760L;
	private JButton logOutButton;
	private ButtonListener buttonListener;

	public ToolBar() {
		setBorder(BorderFactory.createEtchedBorder());

		logOutButton = new JButton("Log out");
		logOutButton.addActionListener(this);
		logOutButton.setFocusable(false);
		setLayout(new FlowLayout(FlowLayout.RIGHT));

		add(new JLabel("<html><FONT COLOR=RED> P.S NO: </FONT>"
				+ "<FONT COLOR=white>" + UserDetailHolder.getUserID()
				+ "</FONT></html>"));
		add(new JLabel(""));
		add(new JLabel(""));
		add(new JLabel("<html><FONT COLOR=RED> Status: </FONT>"
				+ "<FONT COLOR=white>" + UserDetailHolder.getBillStatus()
				+ "</FONT></html>"));
		add(new JLabel(""));
		add(new JLabel(""));
		add(logOutButton);
		setBackground(Color.BLACK);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton clicked = (JButton) e.getSource();

		if (clicked == logOutButton) {

			if (buttonListener != null) {

				buttonListener.logoutAction();
			}
		}

	}

	public void setActionistener(ButtonListener buttonListener) {
		this.buttonListener = buttonListener;
	}

}
