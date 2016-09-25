package com.invoice.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Arshad
 *
 */
@Entity
@Table(name="APPOVER_NAME_TABLE")
public class Approver {
	
	@Id
	@Column(name="Approver_ID")
	private String apprId;
	
	@Column(name="APPROVER_NAME")
	private String apprName;

	public Approver(String apprId, String apprName) {
		super();
		this.apprId = apprId;
		this.apprName = apprName;
	}

	public Approver() {
		super();
	}

	public String getApprId() {
		return apprId;
	}

	public void setApprId(String apprId) {
		this.apprId = apprId;
	}

	public String getApprName() {
		return apprName;
	}

	public void setApprName(String apprName) {
		this.apprName = apprName;
	}

}
