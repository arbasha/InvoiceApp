package com.invoice.dialog;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.invoice.controller.Controller;
import com.invoice.entity.Approver;
import com.invoice.entity.PmNames;
import com.invoice.entity.ProjectNames;
import com.invoice.entity.Registration;
import com.invoice.listener.ChangeInfoListener;
import com.invoice.panel.RegFormPanel;
import com.invoice.pojo.BillCategory;
import com.invoice.pojo.UserDetailHolder;
import com.invoice.swingutil.JTextFieldLimiter;

/**
 * @author Arshad
 *
 */
public class ChangeInfoDialog extends JDialog {

	private static final long serialVersionUID = -240679115607264784L;
	private JLabel userNameLabel;
	private JLabel pmNameLabel;
	private JLabel approverNameLabel;
	private JLabel roleLabel;
	private JTextField userNameField;
	private JComboBox<String> pmNameField;
	private JComboBox<String> approverNameField;
	private JComboBox<String> projectNameField;
	private JComboBox<String> roleField;
	@SuppressWarnings("rawtypes")
	private JComboBox billStatusCom;
	private List<PmNames> pmNameList;
	private List<Approver> apprNameList;
	private List<ProjectNames> proNameList;
	private JButton okBtn;
	private JButton cancelBtn;
	private ChangeInfoListener changeInfoListener;
	private Controller controller;
	private List<Object> dropdwnList;
	private Registration reg;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ChangeInfoDialog(JFrame parent) {
		super(parent, "Change Password", false);
		// Load old values from db
		if (reg == null) {
			if (controller == null)
				controller = new Controller();
			reg = controller.getInfo(UserDetailHolder.getUserID());
		}

		// Creating Instances
		// Labels

		userNameLabel = new JLabel("User Name:");
		roleLabel = new JLabel("User Role:");
		pmNameLabel = new JLabel("PM Name:");
		approverNameLabel = new JLabel("Approver:");
		// Fields
		userNameField = new JTextField(15);
		userNameField.setDocument(new JTextFieldLimiter(35));

		pmNameField = new JComboBox<String>();
		approverNameField = new JComboBox<String>();
		projectNameField = new JComboBox<String>();
		roleField = new JComboBox<String>();
		billStatusCom = new JComboBox(BillCategory.values());
		// Get the drop downs

		dropdwnList = RegFormPanel.getDropdwnList();

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

		cancelBtn = new JButton("Cancel");
		cancelBtn.setFocusable(false);
		okBtn = new JButton("Change");
		okBtn.setFocusable(false);
		if (reg != null) {
			userNameField.setText(reg.getUserName());
			pmNameField.setSelectedItem(reg.getPmName());
			approverNameField.setSelectedItem(reg.getApprover());
			projectNameField.setSelectedItem(reg.getProjectID());
			roleField.setSelectedItem(reg.getRole());
			billStatusCom.setSelectedItem(reg.getBillStatus());
		}

		// Load layout - GridBag
		loadLayout();

		// Action Listener for button Login

		okBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String oldValue = reg.getUserName().trim()
						+ reg.getPmName().trim() + reg.getProjectID().trim()
						+ reg.getRole().trim()
						+ reg.getBillStatus().toString().trim();
				String newValue = userNameField.getText().trim()
						+ pmNameField.getSelectedItem().toString()
						+ projectNameField.getSelectedItem().toString()
						+ roleField.getSelectedItem().toString()
						+ billStatusCom.getSelectedItem().toString();

				if (oldValue.equals(newValue)) {
					JOptionPane.showMessageDialog(ChangeInfoDialog.this,
							"No changes made in th existing information", "",
							JOptionPane.ERROR_MESSAGE);
				} else {
					int action = JOptionPane
							.showConfirmDialog(
									ChangeInfoDialog.this,
									"Changing Personal Information will send a mail for approval",
									"Confirm Change",
									JOptionPane.OK_CANCEL_OPTION);
					if (action == JOptionPane.OK_OPTION) {
						if (changeInfoListener != null) {
							reg.setUserName(userNameField.getText());
							reg.setPmName(pmNameField.getSelectedItem()
									.toString());
							reg.setProjectID(projectNameField.getSelectedItem()
									.toString());
							reg.setRole(roleField.getSelectedItem().toString());
							reg.setBillStatus((BillCategory) billStatusCom
									.getSelectedItem());
							changeInfoListener.changePersonalInfo(reg);
						}
					}

				}

			}

		});

		cancelBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ev) {
				setVisible(false);
			}

		});

		setSize(400, 350);
		setMinimumSize(new Dimension(450, 350));
		setLocationRelativeTo(parent);
	}

	public void loadLayout() {

		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();

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
		gc.anchor = GridBagConstraints.LINE_START;
		gc.insets = new Insets(20, 0, 0, 0);
		add(okBtn, gc);
		gc.gridx = 1;
		gc.anchor = GridBagConstraints.LINE_END;
		gc.insets = new Insets(20, 10, 0, 0);
		add(cancelBtn, gc);

	}

	public void setChangeInfoListener(ChangeInfoListener changeInfoListener) {
		this.changeInfoListener = changeInfoListener;
	}

}
