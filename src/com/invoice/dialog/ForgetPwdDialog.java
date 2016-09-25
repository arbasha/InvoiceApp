package com.invoice.dialog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.invoice.listener.ForgetPWDListener;
import com.invoice.swingutil.JTextFieldLimiter;

/**
 * @author Arshad
 *
 */
public class ForgetPwdDialog extends JDialog {

	private static final long serialVersionUID = -683063498224636363L;
	private JButton okBtn;
	private JButton cancelBtn;
	private JTextField userID;
	private JLabel title;

	private ForgetPWDListener changePWDListener;

	public ForgetPwdDialog() {
	}

	public ForgetPwdDialog(JFrame parent) {
		super(parent, "Change Password", false);
		title = new JLabel("Enter user ID to retrieve login details.");
		okBtn = new JButton("Submit");
		cancelBtn = new JButton("Cancel");
		userID = new JTextField(15);
		userID.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		userID.setDocument(new JTextFieldLimiter(10));
		userID.setPreferredSize(new Dimension(25, 25));

		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();

		gc.gridy = 0;
		gc.weightx = 1;
		gc.weighty = 1;
		gc.fill = GridBagConstraints.NONE;
		gc.gridx = 0;
		gc.anchor = GridBagConstraints.CENTER;
		gc.insets = new Insets(0, 50, 0, 0);
		add(title, gc);

		// Next Row
		gc.gridy++;
		gc.gridx = 0;
		gc.anchor = GridBagConstraints.PAGE_START;
		gc.insets = new Insets(0, 0, 0, 0);
		add(new JLabel("P.S. NO: "), gc);
		gc.insets = new Insets(0, 0, 0, 0);
		gc.anchor = GridBagConstraints.CENTER;
		add(userID, gc);

		gc.gridy++;
		gc.gridx = 0;
		gc.insets = new Insets(0, 0, 0, 0);
		gc.anchor = GridBagConstraints.CENTER;
		add(okBtn, gc);

		gc.gridy++;
		gc.gridx = 0;
		gc.insets = new Insets(0, 0, 0, 0);
		gc.anchor = GridBagConstraints.PAGE_START;
		add(cancelBtn, gc);

		okBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (changePWDListener != null) {
					changePWDListener.sendForgetMail(userID.getText());
				}

			}
		});
		cancelBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				userID.setText("");
				setVisible(false);
			}
		});

		setSize(400, 300);
		setLocationRelativeTo(parent);
	}

	public void setChangePWDListener(ForgetPWDListener changePWDListener) {
		this.changePWDListener = changePWDListener;
	}

}
