package com.invoice.pojo;

/**
 * @author Arshad
 *
 */
public enum LeaveTypeCategory {

	EMPTY("NA"),UnPlanned("UnPlanned"), CompOff("Comp Off"), Planned("Planned"), Holiday("L&T Holiday"), SplDay("Spl Day");
	private String text;

	private LeaveTypeCategory(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return text;
	}
}
