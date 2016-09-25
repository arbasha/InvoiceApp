package com.invoice.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;

import com.invoice.listener.ChangePWDListener;

/**
 * @author Arshad
 *
 */
public class ChangePwdDialog extends JDialog {

	private static final long serialVersionUID = 6235185266020293423L;
	private JButton okBtn;
	private JButton cancelBtn;
	private JPasswordField oldPWD;
	private JPasswordField newPWD;
	private JPasswordField conPWD;
	private ChangePWDListener changePWDListener;

	public ChangePwdDialog() {
	}

	public ChangePwdDialog(JFrame parent) {
		super(parent, "Change Password", false);

		okBtn = new JButton("Change Password");
		cancelBtn = new JButton("Cancel");
		oldPWD = new JPasswordField(15);
		newPWD = new JPasswordField(15);
		conPWD = new JPasswordField(15);

		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();

		gc.gridy = 0;
		gc.weightx = 1;
		gc.weighty = 1;
		gc.fill = GridBagConstraints.NONE;
		gc.gridx = 0;
		gc.anchor = GridBagConstraints.EAST;
		gc.insets = new Insets(0, 0, 0, 0);
		add(new JLabel("Old Password:"), gc);
		gc.anchor = GridBagConstraints.WEST;
		gc.insets = new Insets(0, 20, 0, 0);
		gc.gridx++;
		add(oldPWD, gc);

		// Next Row
		gc.gridy++;
		gc.gridx = 0;
		gc.anchor = GridBagConstraints.EAST;
		add(new JLabel("New Password:"), gc);
		gc.gridx++;
		gc.insets = new Insets(0, 20, 0, 0);
		gc.anchor = GridBagConstraints.WEST;
		add(newPWD, gc);

		// Next Row

		gc.gridy++;
		gc.gridx = 0;
		gc.anchor = GridBagConstraints.EAST;
		add(new JLabel("Confirm Password:"), gc);
		gc.gridx++;
		gc.anchor = GridBagConstraints.WEST;
		gc.insets = new Insets(0, 20, 0, 0);
		add(conPWD, gc);

		gc.gridy++;
		gc.gridx = 1;
		gc.insets = new Insets(0, 0, 0, 30);
		gc.anchor = GridBagConstraints.CENTER;
		add(okBtn, gc);

		gc.gridy++;
		gc.gridx = 1;
		gc.insets = new Insets(0, 0, 0, 20);
		gc.anchor = GridBagConstraints.PAGE_START;
		add(cancelBtn, gc);

		okBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String oldPWDValue = new String(oldPWD.getPassword());
				String newPWDValue = new String(newPWD.getPassword());
				String conPWDValue = new String(conPWD.getPassword());
				if (changePWDListener != null) {
					changePWDListener.changePWD(oldPWDValue, newPWDValue,
							conPWDValue);
				}

			}
		});
		cancelBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				oldPWD.setText("");
				newPWD.setText("");
				conPWD.setText("");
				setVisible(false);
			}
		});

		setSize(400, 300);
		setLocationRelativeTo(parent);
	}

	public void setChangePWDListener(ChangePWDListener changePWDListener) {
		this.changePWDListener = changePWDListener;
	}

}
