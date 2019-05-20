package com.jobportal.applications;


public class NotificationEntity {

	private String destination;
	private String subject;
	private String body;
	
	/**
	 * 
	 * This field refers to the username of the seeker that is applying to this job.
	 * If this is not null, then we have to attach the curriculum.
	 * 
	 */
	private String username;
	
	public NotificationEntity(String destination, String subject, String body, String username) {
		super();
		this.destination = destination;
		this.subject = subject;
		this.body = body;
		this.username = username;
		
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	
}