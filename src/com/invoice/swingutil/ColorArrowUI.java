package com.invoice.swingutil;

import java.awt.Color;
import java.awt.LayoutManager;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;

/**
 * @author Arshad
 *
 */
class ColorArrowUI extends BasicComboBoxUI {

	@Override
	protected LayoutManager createLayoutManager() {
		
		return super.createLayoutManager();
	}

	public static ComboBoxUI createUI(JComponent c) {
		return new ColorArrowUI();
	}

	@Override
	protected JButton createArrowButton() {
		return new BasicArrowButton(BasicArrowButton.SOUTH, Color.GRAY,
				Color.LIGHT_GRAY, Color.BLACK, Color.LIGHT_GRAY);
	}
	
}