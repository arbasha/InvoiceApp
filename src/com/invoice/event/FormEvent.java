package com.invoice.event;

import java.util.EventObject;

/**
 * @author Arshad
 *
 */
public class FormEvent extends EventObject {

	private static final long serialVersionUID = -7821453183935077193L;
	public String name;
	public String comp;
	public String billability;
	public int ageCategory;
	private boolean usCitizen;
	private String taxName;
	private String gender;

	public String getName() {
		return name;
	}

	public String getComp() {
		return comp;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setComp(String comp) {
		this.comp = comp;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBillability() {
		return billability;
	}

	public void setBillability(String billability) {
		this.billability = billability;
	}

	public int getAgeCategory() {
		return ageCategory;
	}

	public void setAgeCategory(int ageCategory) {
		this.ageCategory = ageCategory;
	}

	public boolean isUsCitizen() {
		return usCitizen;
	}

	public void setUsCitizen(boolean usCitizen) {
		this.usCitizen = usCitizen;
	}

	public String getTaxName() {
		return taxName;
	}

	public void setTaxName(String taxName) {
		this.taxName = taxName;
	}

	public FormEvent(Object source) {
		super(source);

	}

	public FormEvent(Object source, String comp) {
		super(source);
		this.comp = comp;

	}
}
