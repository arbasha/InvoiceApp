package com.invoice.panel;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * @author Arshad
 *
 */
public class TextPanel extends JPanel {

	private static final long serialVersionUID = 3125067612245756766L;
	private JTextArea textArea;

	public TextPanel() {
		Dimension dim = getPreferredSize();
		dim.width = 200;
		dim.height=100;
		setPreferredSize(dim);
		textArea = new JTextArea();
		setLayout(new BorderLayout());
		add(new JScrollPane(textArea), BorderLayout.CENTER);
	}

	public void appendText(String text) {

		textArea.append(text);
	}

}
