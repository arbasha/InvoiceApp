package com.invoice.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.io.Reader;
import java.io.StringReader;

import javax.swing.BoxLayout;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;

import org.apache.log4j.Logger;

/**
 * @author Arshad
 *
 */
public class TitlePanel extends JPanel {


	private static final long serialVersionUID = -8190961997017200748L;
	private JEditorPane jedit;
	private static Logger logger = Logger.getLogger(TitlePanel.class);
	public TitlePanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		Dimension dim = getPreferredSize();
		dim.width = 250;
		dim.height = 50;
		setPreferredSize(dim);
		jedit = new JEditorPane();
		jedit.setContentType("text/html");
		jedit.setEditable(false);
		jedit.setBackground(Color.BLACK);
		jedit.setBorder(null);
		JScrollPane jscp= new JScrollPane(jedit);
		jscp.setBorder(null);
		/*jedit.setForeground();;*/
		add(jscp);
		// Adding Border
		setBackground(Color.BLACK);
	}

	public void addMessage(String message) {

		Document doc = jedit.getDocument();
		    try
		    {
		        Reader reader = new StringReader(message);
		        EditorKit kit = jedit.getEditorKit();
		        kit.read(reader, doc, doc.getLength());
		    }
		    catch (Exception e)
		    {
		     
		    	logger.error(e);
		    }
		    jedit.setCaretPosition(doc.getLength());
		    

	}

}
