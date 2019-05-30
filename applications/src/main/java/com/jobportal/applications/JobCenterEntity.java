package com.jobportal.applications;

public class JobCenterEntity {

	private long id;
	private String name;
	private String username;
	private String email;

	public JobCenterEntity() {
	}
	
	public JobCenterEntity(String name, String username, String email) {
		this.name = name;
		this.username = username;
		this.email = email;
	}
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	public long getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
}