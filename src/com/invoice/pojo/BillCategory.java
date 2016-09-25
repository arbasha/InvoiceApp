package com.invoice.pojo;

/**
 * @author Arshad
 *
 */
public enum BillCategory {

	Buffer("Buffer"), Billable("Billed");
	private String text;

	private BillCategory(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return text;
	}

}
