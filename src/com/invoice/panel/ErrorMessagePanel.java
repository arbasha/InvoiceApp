package com.invoice.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.Reader;
import java.io.StringReader;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;

import org.apache.log4j.Logger;

/**
 * @author Arshad
 *
 */
public class ErrorMessagePanel extends JPanel {

	private static final long serialVersionUID = 303704561824708142L;
	private JEditorPane jedit;
	private static Logger logger = Logger.getLogger(ErrorMessagePanel.class);

	public ErrorMessagePanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		Dimension dim = getPreferredSize();
		dim.width = 250;
		dim.height = 100;
		setPreferredSize(dim);
		jedit = new JEditorPane();
		jedit.setContentType("text/html");
		jedit.setEditable(false);
		jedit.setBackground(Color.BLACK);
		jedit.setBorder(null);
		JScrollPane jscp = new JScrollPane(jedit);
		jscp.setBorder(null);
		add(jscp);
		// Adding Border
		setBorder(BorderFactory.createTitledBorder(null, "Messages",
				TitledBorder.DEFAULT_POSITION,
				TitledBorder.DEFAULT_JUSTIFICATION, new Font("times new roman",
						Font.PLAIN, 15), Color.WHITE));
		setBackground(Color.BLACK);
	}

	public void addMessage(String message) {

		Document doc = jedit.getDocument();
		try {
			Reader reader = new StringReader(message);
			EditorKit kit = jedit.getEditorKit();
			kit.read(reader, doc, doc.getLength());
		} catch (Exception e) {
			logger.error(e);
		}
		jedit.setCaretPosition(doc.getLength());

	}

	public void clearEditorPane() {
		jedit.setText("");
	}

	public String getText() {
		return jedit.getText();
	}

}
