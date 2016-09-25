package com.invoice.swingutil;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import com.invoice.util.Utils;

/**
 * @author Arshad
 *
 */
public class PersonalFileFilter extends FileFilter {

	@Override
	public boolean accept(File file) {
		
		if(file.isDirectory())
		{
			return true;
		}
		
		String extension = Utils.getExtension(file);
		
		if(extension == null)
		{
			return false;
		}
		if(extension.equalsIgnoreCase("txt"))
		{
			return true;	
		}
		return false;
	}

	@Override
	public String getDescription() {
		return "Text File (.txt)";
	}

}
