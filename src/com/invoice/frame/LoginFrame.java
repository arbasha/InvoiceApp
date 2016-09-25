package com.invoice.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.IOException;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import com.invoice.controller.Controller;
import com.invoice.crypt.PasswordService;
import com.invoice.dialog.ForgetPwdDialog;
import com.invoice.event.LoginEvent;
import com.invoice.event.RegEvent;
import com.invoice.listener.LoginListener;
import com.invoice.listener.RegisterListener;
import com.invoice.main.InvoiceApp;
import com.invoice.panel.ErrorMessagePanel;
import com.invoice.panel.LoginFormPanel;
import com.invoice.panel.RegFormPanel;
import com.invoice.panel.TextPanel;
import com.invoice.swingutil.Marquee;
import com.invoice.swingutil.MessageHolder;
import com.invoice.util.ValidationUtil;

public class LoginFrame extends JFrame {

	private static final long serialVersionUID = 5380983327358493300L;
	private static Logger logger = Logger.getLogger(LoginFrame.class);
	private LoginFormPanel loginFormPanel;
	private RegFormPanel regPanel;
	private ErrorMessagePanel erroPanel;
	private Controller controller;
	private Marquee titlemqlp;
	private Marquee regmqlp;
	private JLabel regTitleLabel;
	private JPanel regTitlePanel;
	private JLabel logintTitleLabel;
	private JPanel logintTitlePanel;
	@SuppressWarnings("unused")
	private TextPanel text;
	private ValidationUtil validater;
	private MessageHolder msgHolder;
	private Preferences pref;

	public LoginFrame() {
		super(InvoiceApp.VERSION.substring(0, InvoiceApp.VERSION.lastIndexOf(".")));
		// Set layout property
		setLayout(new BorderLayout());

		loginFormPanel = new LoginFormPanel();
		erroPanel = new ErrorMessagePanel();
		text = new TextPanel();
		regPanel = new RegFormPanel();
		pref = Preferences.userRoot().node("remember");
		controller = new Controller();

		// Title Label + Marquee
		logintTitleLabel = new JLabel();
		logintTitlePanel = new JPanel();
		logintTitlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		logintTitlePanel.add(logintTitleLabel);
		logintTitlePanel.setBackground(Color.BLACK);

		// Adding Marquee
		titlemqlp = new Marquee(logintTitleLabel,
				"Invoicing Tool - Login Screen", 32);
		titlemqlp.start();

		// Title Label + Marquee
		regTitleLabel = new JLabel();
		regTitlePanel = new JPanel();
		regTitlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		regTitlePanel.add(regTitleLabel);
		regTitlePanel.setBackground(Color.BLACK);

		// Adding Marquee
		regmqlp = new Marquee(regTitleLabel,
				"Invocing Tool - Registration Screen", 32);
		regmqlp.start();

		// Adding panels to layout
		add(loginFormPanel, BorderLayout.CENTER);
		add(erroPanel, BorderLayout.SOUTH);
		add(logintTitlePanel, BorderLayout.NORTH);

		// Listener
		regPanel.setRegisterListener(new RegisterListener() {

			public List<Object> getRegDropDowns() {
				return controller.getRegDropDowns();

			}

			public void showLoginPanel(boolean flag) {
				if (flag) {
					// use the below to setup login page again
					regTitlePanel.setVisible(false);
					logintTitlePanel.setVisible(true);
					loginFormPanel.setVisible(flag);
					add(loginFormPanel, BorderLayout.CENTER);
					add(logintTitlePanel, BorderLayout.NORTH);

					/*
					 * repaint(); validate();
					 */
				}

			}

			@Override
			public void insertRegDetails(RegEvent regEvent) {

				boolean success = registrationValidation(regEvent);
				if (success) {
					// Encrypt
					regEvent.setPassword(PasswordService.encrypt(regEvent
							.getPassword().toLowerCase()));
					String msg = controller.insertinsertRegDetails(regEvent, msgHolder);
					/*
					 * List<String> messgaes = msgHolder.getInfoMessageList();
					 * // Iterate through the messages if (messgaes != null &&
					 * !messgaes.isEmpty()) { addMsgToErrorPanel(messgaes); }
					 */
					// use the below to setup login page again
					if (msg.contains("S:")) {
						regPanel.setVisible(false);
						regTitlePanel.setVisible(false);
						logintTitlePanel.setVisible(true);
						loginFormPanel.setVisible(true);
						add(loginFormPanel, BorderLayout.CENTER);
						add(logintTitlePanel, BorderLayout.NORTH);
						addMsgToErrorPanel(msg);
					}

				} else {
					List<String> messgaes = msgHolder.getErrorMessageList();
					// Iterate through the messages
					if (messgaes != null && !messgaes.isEmpty()) {
						addMsgToErrorPanel(messgaes);
					}
				}
			}

		});

		loginFormPanel.setLoginListener(new LoginListener() {

			@Override
			public void setloginDetails(LoginEvent loginEvent) {

				// Validation 
				boolean success = loginvalidation(loginEvent);
				if (success) {
					logger.info("Validation Successful "
							+ loginEvent.getUserID() + " Version info: "
							+ InvoiceApp.VERSION);
					loginEvent = controller.checkAuthentication(loginEvent,
							msgHolder);

					List<String> messgaes = msgHolder.getErrorMessageList();
					// Iterate through the messages
					if (messgaes != null && !messgaes.isEmpty()) {
						addMsgToErrorPanel(messgaes);
					} else {

						// User authenticated hide login frame
						setVisible(false);
						// approver page
						if (loginEvent.getAccessType() == "approver") {
							erroPanel.clearEditorPane();
							new ApproverFrame(getSize(), LoginFrame.this);
						} else {
							// Create main panel
							new MainFrame(getSize(), loginEvent.getRole(),
									LoginFrame.this);
						}
					}

				} else {
					List<String> messgaes = msgHolder.getErrorMessageList();
					// Iterate through the messages
					if (messgaes != null && !messgaes.isEmpty()) {
						addMsgToErrorPanel(messgaes);
					}

				}
			}

			@Override
			public void showRegPanel(boolean flag) {

				if (flag) {
					logintTitlePanel.setVisible(false);
					regTitlePanel.setVisible(true);
					regPanel.setVisible(flag);
					add(regTitlePanel, BorderLayout.NORTH);
					add(regPanel, BorderLayout.CENTER);
				}
			}

			@Override
			public void setRememberDetails(LoginEvent loginEvent) {

				pref.put("userid", loginEvent.getUserID());
				pref.put("password", loginEvent.getPassword());
			}

			@Override
			public void clearDefaults() {

				pref.put("userid", "");
				pref.put("password", "");
			}

			@SuppressWarnings("unused")
			@Override
			public void sendMail(String userid, ForgetPwdDialog pwdDialog) {
				boolean success = ValidationuserID(userid);
				if (success) {
/*					String systemUserID = System.getProperty("user.home")
							.split("\\\\")[2];

					if (systemUserID.equalsIgnoreCase(userid)) {*/

						String pwd = controller.getPassword(userid);
						if (pwd.contains("E:")) {
							addMsgToErrorPanel(pwd.replace("E:", "")
									.trim());
						} else {
							String domain = InvoiceApp.prop.getProperty("mail_domain");
							String scriptFileName = InvoiceApp.prop.getProperty("mail_script_name");
							String mailScriptPath = InvoiceApp.prop.getProperty("mail_vbs").replace("${user.name}", System.getProperty("user.name"))  + scriptFileName;
							String[] mailDetails = {
									"cscript",
									mailScriptPath,
									userid + domain,
									"Invoicing Tool - Password Mail",
									"Hi,<br/><br/>Password for Invoicing Tool is : "
											+ pwd + "<br/><br/>Thanks!",
									"empty" };
							try {
								Process ps = Runtime.getRuntime().exec(
										mailDetails);
							} catch (IOException e) {

								e.printStackTrace();
							}
							pwdDialog.setVisible(false);
							addMsgToErrorPanel("S: password sent to "
									+ userid + domain);
						}
			/*		} else {
						JOptionPane
								.showMessageDialog(
										null,
										"System's User ID and Password Requested User ID Needs to Match",
										"", JOptionPane.ERROR_MESSAGE);
					}*/
				} else {
					List<String> messgaes = msgHolder.getErrorMessageList();
					// Iterate through the messages
					if (messgaes != null && !messgaes.isEmpty()) {
						addMsgToErrorPanel(messgaes);
					}
				}

			}

		});
		/* "#FFCB05" */
		// Set defaults
		loginFormPanel.setDefaults(pref.get("userid", ""),
				pref.get("password", ""));
		setSize(600, 550);
		setMinimumSize(new Dimension(620, 620));
		repaint();
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	protected void addMsgToErrorPanel(List<String> messgaes) {

		// clearing out existing text in error message panel
		if (erroPanel.getText() != "") {
			erroPanel.clearEditorPane();
		}

		for (int i = 0; i < messgaes.size(); i++) {
			if (messgaes.get(i).toLowerCase().startsWith("s:")) {
				String msg = messgaes.get(i).substring(2,
						messgaes.get(i).length());
				erroPanel
						.addMessage(new String(
								"<html><font color='Green' size=5> Success&nbsp:&nbsp&nbsp</font><font color='white' size=5> "
										+ msg.trim() + "</font></html>"));
				logger.error(msg);

			} else {
				erroPanel
						.addMessage(new String(
								"<html><font color='red' size=5> Error&nbsp:&nbsp&nbsp</font><font color='white' size=5> "
										+ messgaes.get(i).trim()
										+ "</font></html>"));
				logger.error(messgaes.get(i).trim());

			}
		}
		// clearing out existing list
		messgaes.clear();
	}

	protected boolean loginvalidation(LoginEvent loginEvent) {

		// Validate Login field
		if (validater == null) {
			validater = new ValidationUtil();
		}

		if (msgHolder == null) {
			msgHolder = new MessageHolder();
		}

		if (validater.isEmpty(loginEvent.getUserID())
				|| validater.isEmpty(loginEvent.getPassword())) {
			msgHolder.setErrorMessage("P.S NO or Password cannot be empty");

		}
		if (!validater.isOnlyNumber(loginEvent.getUserID())) {

			msgHolder.setErrorMessage("P.S NO can contain only Numbers");

		}
		if (msgHolder.getErrorMessageList() != null
				&& !msgHolder.getErrorMessageList().isEmpty()) {
			return false;
		}
		return true;
	}

	protected boolean registrationValidation(RegEvent regEvent) {

		// Validate Login field
		if (validater == null) {
			validater = new ValidationUtil();
		}
		if (msgHolder == null) {
			msgHolder = new MessageHolder();
		}

		if (validater.isEmpty(regEvent.getUserId())) {
			msgHolder.setErrorMessage("P.S NO cannot be empty");

		}
		if (!validater.isEmpty(regEvent.getUserId())
				&& !validater.isOnlyNumber(regEvent.getUserId())) {

			msgHolder.setErrorMessage("P.S NO can contain only Numbers");

		}
		if (validater.isEmpty(regEvent.getPassword())) {
			msgHolder.setErrorMessage("Password cannot be empty");

		}

		if (!regEvent.getPassword().equals(regEvent.getConfirmPassword())) {

			msgHolder
					.setErrorMessage("Password does not match the confirm password");

		}
		if (validater.isEmpty(regEvent.getUserName())) {
			msgHolder.setErrorMessage("User name cannot be empty");

		}
		if (!validater.isEmpty(regEvent.getUserName())
				&& !validater.validateUserName(regEvent.getUserName())) {
			msgHolder
					.setErrorMessage("User name can contain only alphabets, numbers, . and _ ");

		}

		if (msgHolder.getErrorMessageList() != null
				&& !msgHolder.getErrorMessageList().isEmpty()) {
			return false;
		}
		return true;
	}

	protected boolean ValidationuserID(String userID) {

		// Validate Login field
		if (validater == null) {
			validater = new ValidationUtil();
		}
		if (msgHolder == null) {
			msgHolder = new MessageHolder();
		}

		if (validater.isEmpty(userID)) {
			msgHolder.setErrorMessage("P.S NO cannot be empty");

		}
		if (!validater.isEmpty(userID) && !validater.isOnlyNumber(userID)) {

			msgHolder.setErrorMessage("P.S NO can contain only Numbers");

		}

		if (msgHolder.getErrorMessageList() != null
				&& !msgHolder.getErrorMessageList().isEmpty()) {
			return false;
		}
		return true;
	}

	protected void addMsgToErrorPanel(String messages) {

		// clearing out existing text in error message panel
		if (erroPanel.getText() != "") {
			erroPanel.clearEditorPane();
		}

		if (messages.toLowerCase().startsWith("s:")) {
			String msg = messages.substring(2, messages.length());
			erroPanel
					.addMessage(new String(
							"<html><font color='Green' size=5> Success&nbsp:&nbsp&nbsp</font><font color='white' size=5> "
									+ msg.trim() + "</font></html>"));
		} else {
			erroPanel
					.addMessage(new String(
							"<html><font color='red' size=5> Error&nbsp:&nbsp&nbsp</font><font color='white' size=5> "
									+ messages.trim() + "</font></html>"));
		}
	}
}
