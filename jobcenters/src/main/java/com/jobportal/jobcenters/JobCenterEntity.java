package com.jobportal.jobcenters;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class JobCenterEntity {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private long id;
	private String username;
	private String name;
	private String email;

	public JobCenterEntity(String name, String username, String email) {
		this.name = name;
		this.username = username;
		this.email = email;
	}

	public JobCenterEntity() {

	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	public JobCenterEntity(long id) {
		this.id = id;
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