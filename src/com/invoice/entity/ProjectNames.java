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
@Table(name = "PROJECT_NAMES_TABLE")
public class ProjectNames {

	@Id
	@Column(name = "Project_ID")
	private String projectId;

	@Column(name = "Project_NAME")
	private String projectName;

	@Column(name = "PM_Name")
	private String pmName;

	public ProjectNames() {
		super();
	}

	public ProjectNames(String projectId, String projectName, String pmName) {
		super();
		this.projectId = projectId;
		this.projectName = projectName;
		this.pmName = pmName;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getPmName() {
		return pmName;
	}

	public void setPmName(String pmName) {
		this.pmName = pmName;
	}

}
