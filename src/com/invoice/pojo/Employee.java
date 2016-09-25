package com.invoice.pojo;

import java.io.Serializable;

import com.invoice.entity.AgeCategory;

/**
 * @author Arshad
 *
 */
public class Employee implements Serializable{

	private static final long serialVersionUID = -5927713073749687835L;
	public String name;
	public String comp;
	public String billability;
	public AgeCategory ageCategory;
	private boolean usCitizen;
	private String taxName;
	private String gender;
	
	public Employee(String name, String comp, String billability,
			AgeCategory ageCategory, boolean usCitizen, String taxName,
			String gender) {
		super();
		this.name = name;
		this.comp = comp;
		this.billability = billability;
		this.ageCategory = ageCategory;
		this.usCitizen = usCitizen;
		this.taxName = taxName;
		this.gender = gender;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getComp() {
		return comp;
	}
	public void setComp(String comp) {
		this.comp = comp;
	}
	public String getBillability() {
		return billability;
	}
	public void setBillability(String billability) {
		this.billability = billability;
	}
	public AgeCategory getAgeCategory() {
		return ageCategory;
	}
	public void setAgeCategory(AgeCategory ageCategory) {
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
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
}
