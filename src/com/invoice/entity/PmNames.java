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
@Table(name="PM_NAMES_TABLE")
public class PmNames {
	
@Id
@Column(name="PM_ID")
private String pmId;

@Column(name="PN_NAME")
private String pmName;

public PmNames(String pmId, String pmName) {
	super();
	this.pmId = pmId;
	this.pmName = pmName;
}

public PmNames() {
	super();
}

public String getPmId() {
	return pmId;
}

public void setPmId(String pmId) {
	this.pmId = pmId;
}

public String getPmName() {
	return pmName;
}

public void setPmName(String pmName) {
	this.pmName = pmName;
}



}
