package com.jobportal.applications;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Entity
public class ApplicationEntity {
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private long id;
	
	/** Seeker who applies for the Job. */
	@NotNull
	@NotEmpty
	private String username;
	
	@NotNull
	@Temporal(TemporalType.DATE)
	private Date dateCreation;
	
	@NotNull
	private long jobId;
	
	/** JobCenter publisher of the Job. */
	@NotNull
	@NotEmpty
	private String centerUsername;
	
	
	public ApplicationEntity() {
		
	}
	public ApplicationEntity(long id, String username, Date dateCreation, long jobId, String centerUsername) {
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
