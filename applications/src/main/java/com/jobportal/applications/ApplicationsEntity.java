package com.jobportal.applications;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ApplicationsEntity {
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private long id;
	private String username;
	private Date dateCreation;
	private long jobId;
	private String centerUsername;
	
	
	public ApplicationsEntity() {
		
	}
	public ApplicationsEntity(long id, String username, Date dateCreation, long jobId, String centerUsername) {
		super();
		this.id = id;
		this.username = username;
		this.dateCreation = dateCreation;
		this.jobId = jobId;
		this.centerUsername = centerUsername;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Date getDateCreation() {
		return dateCreation;
	}
	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}
	public long getJobId() {
		return jobId;
	}
	public void setJobId(long jobId) {
		this.jobId = jobId;
	}
	public String getCenterUsername() {
		return centerUsername;
	}
	public void setCenterUsername(String centerUsername) {
		this.centerUsername = centerUsername;
	}
	
	
	
	
}
