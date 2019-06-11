package com.jobportal.jobs;

import java.util.Date;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
public class JobEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@NotNull
	@NotEmpty
	private String username;
	
	@NotNull
	@NotEmpty
	private String position;
	
	@NotNull
	@NotEmpty
	private String jobDescription;
	
	@NotNull
	@NotEmpty
	private String location;
	
	@NotNull
	@Temporal(TemporalType.DATE)
	private Date dateCreation;
	
	@NotNull
	@NotEmpty
	private String companyName;
	
	@ElementCollection(fetch = FetchType.EAGER)
	@NotNull
	private List<String> skills;

	public JobEntity() {
	}
	
	public JobEntity(long id, String username, String position, String jobDescription, String location,
			Date dateCreation, String companyName, List<String> skills) {
		super();
		this.id = id;
		this.username = username;
		this.position = position;
		this.jobDescription = jobDescription;
		this.location = location;
		this.dateCreation = dateCreation;
		this.companyName = companyName;
		this.skills = skills;
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
	
	public List<String> getSkills() {
		return skills;
	}

	public void setSkills(List<String> skills) {
		this.skills = skills;
	}


}
