package com.invoice.panel;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;

import org.japura.gui.CheckComboBox;
import org.japura.gui.model.ListCheckModel;
import org.japura.gui.renderer.CheckListRenderer;

import com.invoice.frame.MainFrame;
import com.invoice.listener.DisApproveUserListener;

/**
 * @author Arshad
 *
 */
public class DisApproveUserPanel extends JDialog {

	private static final long serialVersionUID = 2713127531544564249L;
	private JButton okBtn;
	private JButton delBtn;
	private JButton cancelBtn;
	private DisApproveUserListener disAppUseristener;
	private ListCheckModel listModel;
	private CheckComboBox ccb;

	public DisApproveUserPanel() {
	}

	public DisApproveUserPanel(JFrame parent) {
		super(parent, "Change Password", false);

		okBtn = new JButton("Disable User");
		delBtn = new JButton("Delete user");
		cancelBtn = new JButton("Cancel");

		ccb = new CheckComboBox();
		ccb.setTextFor(CheckComboBox.NONE, "* No Item selected *");
		ccb.setTextFor(CheckComboBox.MULTIPLE, "* Multiple Items *");
		ccb.setTextFor(CheckComboBox.ALL, "* All selected *");

		listModel = ccb.getModel();

		if (MainFrame.getDropdwnList() != null) {
			List<Object> userNames = MainFrame.getDropdwnList();

			for (int i = 0; i < userNames.size(); i++) {
				listModel.addElement(userNames.get(i).toString());
			}
		}

		ccb.setRenderer(new CheckListRenderer() {

			private static final long serialVersionUID = -5914220185407284138L;

			@SuppressWarnings("rawtypes")
			@Override
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				Component c = super.getListCellRendererComponent(list, value,
						index, isSelected, cellHasFocus);

				c.setBackground(isSelected ? Color.red : Color.white);
				return c;
			}
		});
		ccb.setEnabled(true);

		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();

		gc.gridy = 0;
		gc.weightx = 1;
		gc.weighty = 1;
		gc.anchor = GridBagConstraints.CENTER;
		gc.fill = GridBagConstraints.NONE;
		gc.gridx = 0;
		add(new JLabel("Select the User to Disable or Delete"), gc);

		// Next Row
		gc.gridy++;
		gc.gridx = 0;
		gc.anchor = GridBagConstraints.PAGE_START;
		add(new JLabel("User Name:"), gc);
		gc.insets = new Insets(0, 0, 0, 0);
		gc.anchor = GridBagConstraints.CENTER;
		add(ccb, gc);

		// Next Row

		gc.gridy++;
		gc.gridx = 0;
		gc.insets = new Insets(0, 30, 0, 0);
		gc.anchor = GridBagConstraints.WEST;
		/* okBtn.setSize(new Dimension(40,40)); */
		add(okBtn, gc);

		gc.insets = new Insets(0, 0, 0, 40);
		gc.anchor = GridBagConstraints.EAST;
		add(cancelBtn, gc);

		gc.insets = new Insets(0, 20, 0, 0);
		gc.anchor = GridBagConstraints.CENTER;
		add(delBtn, gc);

		okBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				List<String> listVal = new ArrayList<String>();
				for (Object obj : listModel.getCheckeds()) {
					listVal.add(obj.toString());
				}

				if (disAppUseristener != null) {
					disAppUseristener.disableUser(listVal);
				}

			}
		});
		cancelBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				setVisible(false);
			}
		});
		delBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				List<String> listVal = new ArrayList<String>();
				for (Object obj : listModel.getCheckeds()) {
					listVal.add(obj.toString());
				}

				if (disAppUseristener != null) {
					disAppUseristener.delUser(listVal);
				}

			}
		});

		setSize(400, 300);
		setLocationRelativeTo(parent);
	}

	public void setDisApproveUserListener(
			DisApproveUserListener disAppUseristener) {
		this.disAppUseristener = disAppUseristener;
	}

}
