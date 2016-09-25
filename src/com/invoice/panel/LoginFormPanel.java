package com.invoice.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import com.invoice.dialog.ForgetPwdDialog;
import com.invoice.event.LoginEvent;
import com.invoice.listener.ForgetPWDListener;
import com.invoice.listener.LoginListener;
import com.invoice.swingutil.JTextFieldLimiter;

/**
 * @author Arshad
 *
 */
public class LoginFormPanel extends JPanel {

	private static final long serialVersionUID = -5715240969551734005L;
	private static Logger logger = Logger.getLogger(LoginFormPanel.class);
	private JLabel idLabel;
	private JLabel passwordLabel;
	private JTextField idField;
	private JPasswordField passField;
	private JButton btnLogin;
	private JButton btnForgotPassword;
	private JButton btnRegister;
	private JCheckBox rememberCheck;
	private LoginListener loginListener;
	private JRadioButton userRadio;
	private JRadioButton approverRadio;
	private ButtonGroup accessGroup;
	private BufferedImage image;
	private ForgetPwdDialog pwdDialog;

	public LoginFormPanel() {
		Dimension dim = getPreferredSize();
		dim.width = 250;
		setPreferredSize(dim);

		try {
			image = ImageIO.read(getClass().getResource("/Images/ui.jpg"));
		} catch (IOException ex) {
			logger.error(ex);
		}
		// Creating Instances
		idLabel = new JLabel("P.S NO :");
		passwordLabel = new JLabel("Password :");
		idField = new JTextField(15);
		idField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		idField.setDocument(new JTextFieldLimiter(10));
		idField.setPreferredSize(new Dimension(25, 25));
		passField = new JPasswordField(15);
		passField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		passField.setDocument(new JTextFieldLimiter(16));
		passField.setPreferredSize(new Dimension(25, 25));
		btnLogin = new JButton("Login");
		btnLogin.setFocusable(false);
		btnForgotPassword = new JButton("Forgot Password?");
		btnForgotPassword.setFocusable(false);
		rememberCheck = new JCheckBox();
		rememberCheck.setSelected(true);
		rememberCheck.setBackground(Color.BLACK);

		userRadio = new JRadioButton("User");
		approverRadio = new JRadioButton("Approver");
		userRadio.setSelected(true);
		userRadio.setFocusable(false);
		userRadio.setActionCommand("user");
		userRadio.setOpaque(false);
		userRadio.setBorder(null);
		approverRadio.setActionCommand("approver");
		approverRadio.setOpaque(false);
		approverRadio.setBorder(null);
		approverRadio.setFocusable(false);
		accessGroup = new ButtonGroup();
		accessGroup.add(userRadio);
		accessGroup.add(approverRadio);

		btnRegister = new JButton("Register");
		btnRegister.setFocusable(false);
		Dimension btnDim = btnRegister.getPreferredSize();
		btnLogin.setPreferredSize(btnDim);
		
		pwdDialog = new ForgetPwdDialog((JFrame)SwingUtilities.getRoot(this));
		
		// Load layout - GridBag
		loadLayout();

		// Action Listener for button Login
		btnLogin.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String id = idField.getText();
				char[] password = passField.getPassword();

				LoginEvent loginEvent = new LoginEvent(this, id, new String(
						password));
				loginEvent.setAccessType(accessGroup.getSelection()
						.getActionCommand());
				if (loginListener != null) {
					if (rememberCheck.isSelected()) {
						loginListener.setRememberDetails(loginEvent);
					} else {
						loginListener.clearDefaults();
					}
					loginListener.setloginDetails(loginEvent);
				}
			}

		});

		btnRegister.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				/*
				 * JFrame loginFrame =
				 * (JFrame)LoginFormPanel.this.getTopLevelAncestor();
				 */
				setVisible(false);
				if (loginListener != null) {

					loginListener.showRegPanel(true);
				}

			}

		});
		
		btnForgotPassword.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) {
			
				pwdDialog.setVisible(true);
			}
			
		});
		
		pwdDialog.setChangePWDListener(new ForgetPWDListener() {
			
			@Override
			public void sendForgetMail(String userid) {
				
				loginListener.sendMail(userid,pwdDialog);
			}
		});

	}

	public void loadLayout() {
		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();

		// First Row
		gc.gridy = 0;
		gc.gridy++;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.gridx = 0;
		gc.fill = GridBagConstraints.NONE;
		gc.insets = new Insets(10, 5, 20, 5);
		gc.anchor = GridBagConstraints.LINE_END;
		add(idLabel, gc);

		gc.gridx = 1;
		gc.anchor = GridBagConstraints.LINE_START;
		add(idField, gc);

		// Second Row

		gc.gridy++;
		gc.gridx = 0;

		gc.fill = GridBagConstraints.NONE;
		gc.insets = new Insets(10, 5, 20, 5);
		gc.anchor = GridBagConstraints.LINE_END;
		add(passwordLabel, gc);

		gc.gridx = 1;
		gc.anchor = GridBagConstraints.LINE_START;
		add(passField, gc);

		// check box
		gc.gridy++;
		gc.gridx = 1;

		gc.fill = GridBagConstraints.NONE;
		gc.insets = new Insets(0, 40, 0, 0);
		gc.anchor = GridBagConstraints.WEST;
		add(rememberCheck, gc);

		gc.gridx = 1;
		gc.anchor = GridBagConstraints.EAST;
		gc.insets = new Insets(0, 0, 0, 30);
		add(new JLabel("Remember Me"), gc);

		// Approver radio button
		// Access
		gc.gridy++;
		gc.gridx = 0;
		gc.insets = new Insets(0, 0, 0, 5);
		gc.anchor = GridBagConstraints.LINE_END;
		add(new JLabel("Login As:"), gc);

		gc.gridx = 1;
		gc.anchor = GridBagConstraints.WEST;
		gc.insets = new Insets(10, 0, 10, 5);
		add(userRadio, gc);
		gc.insets = new Insets(10, 25, 10, 0);
		gc.gridx = 1;
		gc.anchor = GridBagConstraints.EAST;
		add(approverRadio, gc);

		// Third Row
		gc.gridy++;
		gc.gridx = 1;
		gc.fill = GridBagConstraints.NONE;
		gc.insets = new Insets(0, 5, 20, 10);
		gc.anchor = GridBagConstraints.CENTER;
		add(btnLogin, gc);

		// Fourth Row
		gc.gridy++;
		gc.gridx = 1;
		gc.fill = GridBagConstraints.NONE;
		gc.insets = new Insets(0, 0, 20, 5);
		gc.anchor = GridBagConstraints.CENTER;
		add(btnForgotPassword, gc);

		// Fifth Row
		gc.gridy = gc.gridy + 4;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.gridx = 0;
		gc.fill = GridBagConstraints.NONE;
		gc.insets = new Insets(0, 0, 0, 0);
		gc.anchor = GridBagConstraints.LINE_END;
		add(new JLabel("New User?"), gc);
		gc.gridx = 1;
		gc.anchor = GridBagConstraints.CENTER;
		add(btnRegister, gc);

	}

	public void setLoginListener(LoginListener loginListener) {
		this.loginListener = loginListener;
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

	public void setDefaults(String userid, String password) {
		idField.setText(userid);
		passField.setText(password);
	}

}
