package com.jobportal.auth.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.jobportal.auth.model.User;
import com.jobportal.auth.model.UserGeneral;

public interface UserService {

    ResponseEntity<User> save(UserGeneral user);
    List<User> findAll();
    ResponseEntity delete(String username);
    User findOne(String username);
    User findById(long id);
    ResponseEntity update(String loggedUser, UserGeneral newUser);
    User findByUsername(String username);
    
}