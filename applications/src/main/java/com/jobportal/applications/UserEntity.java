package com.jobportal.applications;


public class UserEntity {

    private long id;
    private String email;
    
    private String username;

    private String password;

    private Role role;

    public enum Role {
        ADMIN,
        SEEKER,
        JOB_CENTER
    }

    public String getEmail() {
    	return email;
    }
    
    public void setEmail(String email) {
    	this.email = email;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() { return role; }

    public void setRole(Role role) { this.role = role; }


}