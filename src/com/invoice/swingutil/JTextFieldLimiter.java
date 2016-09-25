package com.invoice.swingutil;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * @author Arshad
 *
 */
public class JTextFieldLimiter extends PlainDocument {

	private static final long serialVersionUID = 1720574312377734771L;
	private int limit;
	public  JTextFieldLimiter(int limit) {
	    super();
	    this.limit = limit;
	  }

	 public JTextFieldLimiter(int limit, boolean upper) {
	    super();
	    this.limit = limit;
	  }

	  public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
	    if (str == null)
	      return;

	    if ((getLength() + str.length()) <= limit) {
	      super.insertString(offset, str, attr);
	    }
	  }
	

	

}
