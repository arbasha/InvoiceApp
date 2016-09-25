package com.invoice.dialog;

import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 * @author Arshad
 *
 */
public class PrefDialog extends JDialog	{
	
	private static final long serialVersionUID = 9011112210678303074L;

	PrefDialog(JFrame parent)
	{
	super(parent,"Preferences",false);
	setSize(400,300);
	}

}
