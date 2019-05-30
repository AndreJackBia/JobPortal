package com.jobportal.applications;


import java.util.Date;

public class JobEntity {
	
	private long id;
	private String username;
	private String position;
	private String jobDescription;
	private String location;
	private Date dateCreation;
	private String companyName;

	public JobEntity(long id, String username, String position, String jobDescription, String location,
			Date dateCreation, String companyName) {
		super();
		this.id = id;
		this.username = username;
		this.position = position;
		this.jobDescription = jobDescription;
		this.location = location;
		this.dateCreation = dateCreation;
		this.companyName = companyName;
	}
	
	public JobEntity() {
		
	}	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getJobDescription() {
		return jobDescription;
	}
	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public Date getDateCreation() {
		return dateCreation;
	}
	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}


}