package com.invoice.swingutil;

/**
 * @author Arshad
 *
 */
public class CheckComboStore {
	String id;
	Boolean state;

	public CheckComboStore(String id, Boolean state) {
		this.id = id;
		this.state = state;
	}
	
	public CheckComboStore() {
	
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Boolean getState() {
		return state;
	}

	public void setState(Boolean state) {
		this.state = state;
	}
	
	

}