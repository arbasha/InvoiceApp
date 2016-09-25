package com.invoice.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import org.apache.log4j.Logger;

import com.invoice.controller.Controller;
import com.invoice.entity.Registration;
import com.invoice.listener.ApproveTableListener;
import com.invoice.listener.DisApproveUserListener;
import com.invoice.main.InvoiceApp;
import com.invoice.panel.ApproveTabelPanel;
import com.invoice.panel.DisApproveUserPanel;
import com.invoice.panel.ErrorMessagePanel;

/**
 * @author Arshad
 *
 */
public class ApproverFrame extends JFrame {
	private static final long serialVersionUID = -2388400275851084717L;
	private static Logger logger = Logger.getLogger(ApproverFrame.class);
	private ApproveTabelPanel approveTabel;
	private JLabel approvingList;
	private JPanel approveLable;
	private Controller controller;
	private ErrorMessagePanel erroPanel;
	private JButton logOutButton;
	private LoginFrame loginFrame;
	private DisApproveUserPanel disApproveDialog;

	public ApproverFrame() {
		super(InvoiceApp.VERSION.substring(0, InvoiceApp.VERSION.lastIndexOf(".")));

		setLayout(new BorderLayout());

		approveTabel = new ApproveTabelPanel();

		erroPanel = new ErrorMessagePanel();

		setJMenuBar(createMenu());

		disApproveDialog = new DisApproveUserPanel(this);

		controller = new Controller();
		approveTabel.setApprovingList(controller.getIntialApproverList());

		approveTabel.setApproveTableListener(new ApproveTableListener() {

			@Override
			public void saveAccessList(List<Registration> approveList) {
				erroPanel.clearEditorPane();
				String msg = controller.saveAccessList(approveList);
				addSingleMsgToErrorPanel(msg);
				if (msg.toLowerCase().startsWith("s:")) {
					approveTabel.setApprovingList(controller
							.getIntialApproverList());
				}
			}

			@Override
			public void delete(List<Registration> approveList, int[] row) {
				erroPanel.clearEditorPane();
				List<Registration> deletList = new LinkedList<Registration>();
				for (int i = 0; i < row.length; i++) {
					deletList.add(approveList.get(row[i]));
				}
				approveList.removeAll(deletList);
				addSingleMsgToErrorPanel(controller.deleteDecline(deletList));

			}

			@Override
			public void getAccessList() {

				erroPanel.clearEditorPane();
				approveTabel.setApprovingList(controller
						.getIntialApproverList());

			}
		});

		disApproveDialog
				.setDisApproveUserListener(new DisApproveUserListener() {

					@Override
					public void disableUser(List<String> listVal) {

						String msg = controller.disApproveUser(listVal);
						addSingleMsgToErrorPanel(msg);
					}

					@Override
					public void delUser(List<String> listVal) {

					}
				});

		logOutButton = new JButton("Log out");
		logOutButton.setOpaque(false);
		logOutButton.setFocusable(false);
		logOutButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				loginFrame.setVisible(true);
				repaint();
				revalidate();
			}
		});
		logOutButton.setFocusable(false);

		approveLable = new JPanel();
		approveLable.setBorder(BorderFactory.createEtchedBorder());
		approveLable.setBackground(Color.BLACK);
		approvingList = new JLabel(
				"<html><pre><font color='white' size=6>        Approving List</font></pre></html>",
				SwingConstants.CENTER);
		approveLable.setLayout(new BorderLayout());
		approveLable.add(approvingList, BorderLayout.CENTER);
		approveLable.add(logOutButton, BorderLayout.EAST);
		approvingList.setForeground(Color.BLACK);
		add(approveLable, BorderLayout.NORTH);
		add(erroPanel, BorderLayout.SOUTH);

		add(approveTabel, BorderLayout.CENTER);

	}

	public ApproverFrame(Dimension size, LoginFrame loginFrame) {
		this();
		setSize(size);
		setLocationRelativeTo(null);
		setMinimumSize(new Dimension(620, 620));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		this.loginFrame = loginFrame;
	}

	protected void addSingleMsgToErrorPanel(String messgaes) {

		// clearing out existing text in error message panel
		if (erroPanel.getText() != "") {
			erroPanel.clearEditorPane();
		}

		if (messgaes.toLowerCase().startsWith("s:")) {
			String msg = messgaes.substring(2, messgaes.length());
			erroPanel
					.addMessage(new String(
							"<html><font color='Green' size=5> Success&nbsp:&nbsp&nbsp</font><font color='white' size=5> "
									+ msg.trim() + "</font></html>"));
			logger.info(msg);
		} else {
			erroPanel
					.addMessage(new String(
							"<html><font color='red' size=5> Error&nbsp:&nbsp&nbsp</font><font color='white' size=5> "
									+ messgaes.trim() + "</font></html>"));
			logger.info(messgaes);
		}
	}

	private JMenuBar createMenu() {
		JMenuBar jmenuBar = new JMenuBar();

		JMenu jFilemenu = new JMenu("File");

		JMenuItem fileExit = new JMenuItem("Exit");

		jFilemenu.addSeparator();
		jFilemenu.add(fileExit);

		// set the Mnemonic for exit
		jFilemenu.setMnemonic(KeyEvent.VK_F);
		fileExit.setMnemonic(KeyEvent.VK_X);
		fileExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
				ActionEvent.CTRL_MASK));

		JMenu jWinmenu = new JMenu("Window");
		JMenuItem disableuser = new JMenuItem("Disable User");

		fileExit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int action = JOptionPane.showConfirmDialog(ApproverFrame.this,
						"Do You Want to Exit?", "Confirm Exit",
						JOptionPane.OK_CANCEL_OPTION);
				if (action == JOptionPane.OK_OPTION) {
					System.exit(0);
				}

			}
		});

		disableuser.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				disApproveDialog.setVisible(true);
			}
		});

		jWinmenu.add(disableuser);
		jmenuBar.add(jFilemenu);
		jmenuBar.add(jWinmenu);
		return jmenuBar;

	}

}