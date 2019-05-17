package com.jobportal.auth.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.jobportal.auth.model.Account;
import com.jobportal.auth.model.UserGeneral;

public interface UserService {

    ResponseEntity<Account> save(UserGeneral user);
    List<Account> findAll();
    ResponseEntity delete(String username);
    Account findOne(String username);
    Account findById(long id);
    ResponseEntity update(String loggedUser, UserGeneral newUser);
    Account findByUsername(String username);
    
}