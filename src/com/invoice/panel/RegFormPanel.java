package com.invoice.panel;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.invoice.controller.Controller;
import com.invoice.entity.Approver;
import com.invoice.entity.PmNames;
import com.invoice.entity.ProjectNames;
import com.invoice.event.RegEvent;
import com.invoice.listener.RegisterListener;
import com.invoice.pojo.BillCategory;
import com.invoice.swingutil.JTextFieldLimiter;

/**
 * @author Arshad
 *
 */
public class RegFormPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	static Logger logger = Logger.getLogger(RegFormPanel.class);
	private JLabel idLabel;
	private JLabel passwordLabel;
	private JLabel conPassLabel;
	private JLabel userNameLabel;
	private JLabel pmNameLabel;
	private JLabel approverNameLabel;
	private JLabel roleLabel;
	private JTextField idField;
	private JPasswordField passField;
	private JPasswordField conField;
	private JTextField userNameField;
	private JComboBox<String> pmNameField;
	private JComboBox<String> approverNameField;
	private JComboBox<String> projectNameField;
	private JComboBox<String> roleField;
	@SuppressWarnings("rawtypes")
	private JComboBox billStatusCom;
	private static List<Object> dropdwnList;

	public static List<Object> getDropdwnList() {
		return dropdwnList;
	}

	public static void setDropdwnList(List<Object> dropdwnList) {
		RegFormPanel.dropdwnList = dropdwnList;
	}

	private List<PmNames> pmNameList;
	private List<Approver> apprNameList;
	private List<ProjectNames> proNameList;
	private JButton btnLogin;
	private JButton btnRegister;
	private BufferedImage image;
	private RegisterListener registerListener;
	private Controller controller;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public RegFormPanel() {
		Dimension dim = getPreferredSize();
		dim.width = 250;
		setPreferredSize(dim);
		try {
			image = ImageIO.read(getClass().getResource("/Images/ui.jpg"));
		} catch (IOException ex) {
			logger.error("Error while loading Image", ex);
		}
		// Creating Instances
		// Labels
		idLabel = new JLabel("P.S NO :");
		passwordLabel = new JLabel("Password :");
		conPassLabel = new JLabel("Confirm Password:");
		userNameLabel = new JLabel("User Name:");
		roleLabel = new JLabel("User Role:");
		pmNameLabel = new JLabel("PM Name:");
		approverNameLabel = new JLabel("Approver:");
		// Fields
		idField = new JTextField(15);
		idField.setDocument(new JTextFieldLimiter(10));
		passField = new JPasswordField(15);
		passField.setDocument(new JTextFieldLimiter(12));
		conField = new JPasswordField(15);
		userNameField = new JTextField(15);
		userNameField.setDocument(new JTextFieldLimiter(35));
		pmNameField = new JComboBox<String>();
		approverNameField = new JComboBox<String>();
		projectNameField = new JComboBox<String>();
		roleField = new JComboBox<String>();
		billStatusCom = new JComboBox(BillCategory.values());
		// Get the drop downs
		if (controller == null) {
			controller = new Controller();
		}

		if (dropdwnList == null) {
			dropdwnList = controller.getRegDropDowns();
		}

		pmNameList = (List<PmNames>) dropdwnList.get(0);
		apprNameList = (List<Approver>) dropdwnList.get(1);
		proNameList = (List<ProjectNames>) dropdwnList.get(2);

		DefaultComboBoxModel<String> comModelPm = new DefaultComboBoxModel<String>();
		if (pmNameList != null) {
			for (int i = 0; i < pmNameList.size(); i++) {
				PmNames pmNames = pmNameList.get(i);
				comModelPm.addElement(pmNames.getPmName());
			}
		}
		pmNameField.setModel(comModelPm);
		pmNameField.setEditable(false);

		// list Approver Name

		DefaultComboBoxModel<String> comModelAppr = new DefaultComboBoxModel<String>();
		if (apprNameList != null) {
			for (int i = 0; i < apprNameList.size(); i++) {
				Approver apprNames = apprNameList.get(i);
				comModelAppr.addElement(apprNames.getApprName());
			}
		}
		approverNameField.setModel(comModelAppr);
		approverNameField.setEditable(false);

		// Project IDs
		DefaultComboBoxModel<String> comModelPro = new DefaultComboBoxModel<String>();
		if (proNameList != null) {
			for (int i = 0; i < proNameList.size(); i++) {
				ProjectNames proNames = proNameList.get(i);
				comModelPro.addElement(proNames.getProjectId());
			}
		}
		projectNameField.setModel(comModelPro);
		projectNameField.setEditable(false);

		DefaultComboBoxModel<String> comModelRole = new DefaultComboBoxModel<String>();
		comModelRole.addElement("TM");
		comModelRole.addElement("PL");
		comModelRole.addElement("PM");
		roleField.setModel(comModelRole);
		roleField.setEditable(false);

		// Login button
		btnLogin = new JButton("Login");
		btnLogin.setFocusable(false);
		btnRegister = new JButton("Register");
		btnRegister.setFocusable(false);
		// Load layout - GridBag
		loadLayout();

		// Action Listener for button Login

		btnLogin.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);

				if (registerListener != null) {

					registerListener.showLoginPanel(true);
				}

			}

		});

		btnRegister.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ev) {

				RegEvent regEvent = new RegEvent(this, idField.getText(),
						new String(passField.getPassword()), new String(
								conField.getPassword()), userNameField
								.getText(), (String) pmNameField
								.getSelectedItem(), (String) roleField
								.getSelectedItem(), (String) approverNameField
								.getSelectedItem(),
						(BillCategory) billStatusCom.getSelectedItem(), false,
						(String) projectNameField.getSelectedItem());
				if (registerListener != null) {

					registerListener.insertRegDetails(regEvent);
				}

			}

		});
	}

	public void loadLayout() {

		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();

		// First Row
		gc.gridy++;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.gridx = 0;
		gc.fill = GridBagConstraints.NONE;
		gc.insets = new Insets(5, 5, 5, 5);
		gc.anchor = GridBagConstraints.LINE_END;
		add(idLabel, gc);

		gc.gridx = 1;
		gc.anchor = GridBagConstraints.LINE_START;
		add(idField, gc);

		// Second Row

		gc.gridy++;
		gc.gridx = 0;

		gc.fill = GridBagConstraints.NONE;
		gc.insets = new Insets(5, 5, 5, 5);
		gc.anchor = GridBagConstraints.LINE_END;
		add(passwordLabel, gc);

		gc.gridx = 1;
		gc.anchor = GridBagConstraints.LINE_START;
		add(passField, gc);

		// Third Row
		gc.gridy++;
		gc.gridx = 0;

		gc.fill = GridBagConstraints.NONE;
		gc.insets = new Insets(5, 5, 5, 5);
		gc.anchor = GridBagConstraints.LINE_END;
		add(conPassLabel, gc);

		gc.gridx = 1;
		gc.anchor = GridBagConstraints.LINE_START;
		add(conField, gc);

		// Fourth Row
		gc.gridy++;
		gc.gridx = 0;

		gc.fill = GridBagConstraints.NONE;
		gc.insets = new Insets(5, 5, 5, 5);
		gc.anchor = GridBagConstraints.LINE_END;
		add(userNameLabel, gc);

		gc.gridx = 1;
		gc.anchor = GridBagConstraints.LINE_START;
		add(userNameField, gc);

		// Bill Status Row
		gc.gridy++;
		gc.gridx = 0;

		gc.fill = GridBagConstraints.NONE;
		gc.insets = new Insets(5, 5, 5, 5);
		gc.anchor = GridBagConstraints.LINE_END;
		add(new JLabel("Bill Status:"), gc);

		gc.gridx = 1;
		gc.anchor = GridBagConstraints.LINE_START;
		add(billStatusCom, gc);

		// Role row
		gc.gridy++;
		gc.gridx = 0;

		gc.fill = GridBagConstraints.NONE;
		gc.insets = new Insets(5, 5, 5, 5);
		gc.anchor = GridBagConstraints.LINE_END;
		add(roleLabel, gc);

		gc.gridx = 1;
		gc.anchor = GridBagConstraints.LINE_START;
		add(roleField, gc);

		// Fifth Row
		gc.gridy++;
		gc.gridx = 0;

		gc.fill = GridBagConstraints.NONE;
		gc.insets = new Insets(5, 5, 5, 5);
		gc.anchor = GridBagConstraints.LINE_END;
		add(pmNameLabel, gc);

		gc.gridx = 1;
		gc.anchor = GridBagConstraints.LINE_START;
		add(pmNameField, gc);

		// project ID
		gc.gridy++;
		gc.gridx = 0;

		gc.fill = GridBagConstraints.NONE;
		gc.insets = new Insets(5, 5, 5, 5);
		gc.anchor = GridBagConstraints.LINE_END;
		add(new JLabel("Project ID:"), gc);

		gc.gridx = 1;
		gc.anchor = GridBagConstraints.LINE_START;
		add(projectNameField, gc);

		// Sixth Row
		gc.gridy++;
		gc.gridx = 0;

		gc.fill = GridBagConstraints.NONE;
		gc.insets = new Insets(5, 5, 5, 5);
		gc.anchor = GridBagConstraints.LINE_END;
		add(approverNameLabel, gc);

		gc.gridx = 1;
		gc.anchor = GridBagConstraints.LINE_START;
		add(approverNameField, gc);

		// Seventh Row
		gc.gridy++;
		gc.gridx = 1;
		gc.anchor = GridBagConstraints.CENTER;
		add(btnRegister, gc);

		// Eight row
		gc.gridy = gc.gridy + 2;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.gridx = 0;
		gc.fill = GridBagConstraints.NONE;
		gc.insets = new Insets(5, 5, 5, 5);
		gc.anchor = GridBagConstraints.CENTER;
		add(new JLabel("Already a User Click Login:"), gc);

		// Ninth Row
		gc.gridy++;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.gridx = 0;
		gc.fill = GridBagConstraints.NONE;
		gc.insets = new Insets(0, 0, 0, 0);
		gc.anchor = GridBagConstraints.CENTER;
		add(btnLogin, gc);

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

	public void setRegisterListener(RegisterListener registerListener) {
		this.registerListener = registerListener;

	}

}
