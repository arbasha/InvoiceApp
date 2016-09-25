package com.invoice.swingutil;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Arshad
 *
 */
public class MessageHolder {
	private List<String> errorMessage = null;
	private List<String> infoMessage = null;

	public void setErrorMessage(String message) {
		if (this.errorMessage == null) {
			this.errorMessage = new LinkedList<String>();
		}
		this.errorMessage.add(message);
	}

	public List<String> getErrorMessageList() {
		return errorMessage;
	}
	public List<String> getInfoMessageList() {
		return infoMessage;
	}

	public void setInfoMessage(String message) {
		if (this.infoMessage == null) {
			this.infoMessage = new LinkedList<String>();
		}
		this.infoMessage.add(message);
	}


}
