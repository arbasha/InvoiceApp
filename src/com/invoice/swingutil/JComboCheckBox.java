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
public class JComboCheckBox extends JComboBox {

	private static final long serialVersionUID = -758365688721617577L;
	private CheckComboRenderer renderer;
	public static List<String> selectedList = new ArrayList<String>();

	@SuppressWarnings("unchecked")
	public JComboCheckBox() {
		renderer = new CheckComboRenderer();
		setRenderer(renderer);
		addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {

		CheckComboStore store = (CheckComboStore) getSelectedItem();
		// CheckComboRenderer ccr = (CheckComboRenderer) getRenderer();
		store.state = !store.state;
		// ccr.checkBox.setSelected(store.state);
		repaint();
		revalidate();
		repaint();
	}

	public void setPopupVisible(boolean flag) {

	}

}

@SuppressWarnings("rawtypes")
class CheckComboRenderer implements ListCellRenderer {
	JCheckBox checkBox;

	public CheckComboRenderer() {
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

			if (!JComboCheckBox.selectedList.contains(store.id)) {
				JComboCheckBox.selectedList.add(store.id);
			}
		} else {
			if (JComboCheckBox.selectedList.contains(store.id)) {
				JComboCheckBox.selectedList.remove(store.id);
			}

		}

		checkBox.setText(store.id);
		checkBox.setSelected(((Boolean) store.state).booleanValue());
		checkBox.setBackground(isSelected ? Color.red : Color.white);
		checkBox.setForeground(isSelected ? Color.white : Color.black);
		return checkBox;
	}
}
