package com.invoice.swingutil;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

@SuppressWarnings("rawtypes")
public class JComboCheckBoxDis extends JComboBox {

	private static final long serialVersionUID = -630468717927645740L;
	private CheckComboDisRenderer renderer;
	public static List<String> selectedDisList = new ArrayList<String>();

	@SuppressWarnings("unchecked")
	public JComboCheckBoxDis() {
		renderer = new CheckComboDisRenderer();
		setRenderer(renderer);
		addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {

		CheckComboStore store = (CheckComboStore) getSelectedItem();
		// CheckComboRenderer ccr = (CheckComboRenderer) getRenderer();
		store.state = !store.state;
		// ccr.checkBox.setSelected(store.state);
		repaint();
	}

	public void setPopupVisible(boolean flag) {
	}

}

@SuppressWarnings("rawtypes")
class CheckComboDisRenderer implements ListCellRenderer {
	JCheckBox checkBox;

	public CheckComboDisRenderer() {
		checkBox = new JCheckBox();
		// checkBox.addActionListener(new java.awt.event.ActionListener() {
		// public void actionPerformed(ActionEvent e) {
		// System.out.println("here");
		// }
		// }
		// );
	}

	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		CheckComboStore store = (CheckComboStore) value;
		if (store.state.booleanValue() == true) {

			if (!JComboCheckBoxDis.selectedDisList.contains(store.id)) {
				JComboCheckBoxDis.selectedDisList.add(store.id);
			}
		} else {
			if (JComboCheckBoxDis.selectedDisList.contains(store.id)) {
				JComboCheckBoxDis.selectedDisList.remove(store.id);
			}

		}

		checkBox.setText(store.id);
		checkBox.setSelected(((Boolean) store.state).booleanValue());
		checkBox.setBackground(isSelected ? Color.red : Color.white);
		checkBox.setForeground(isSelected ? Color.white : Color.black);
		return checkBox;
	}
}
