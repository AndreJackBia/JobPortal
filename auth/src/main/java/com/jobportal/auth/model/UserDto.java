package com.jobportal.auth.model;

import com.jobportal.auth.model.User.Role;

public class UserDto {

    private long id;
    private String username;
    private String password;
    private Role role;


    public UserDto(String username, String password, Role role) {
    	this.username = username;
    	this.password = password;
    	this.role = role;
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