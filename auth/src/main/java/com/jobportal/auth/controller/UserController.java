package com.jobportal.auth.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jobportal.auth.model.ApiResponse;
import com.jobportal.auth.model.Account;
import com.jobportal.auth.model.UserGeneral;
import com.jobportal.auth.service.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController

public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ApiResponse<List<Account>> listUser(){
        return new ApiResponse<>(HttpStatus.OK.value(), "User list fetched successfully.", userService.findAll());
    }

    @GetMapping("/users/{username}")
    public ResponseEntity getOne(@PathVariable String username){
        return ResponseEntity.ok(userService.findByUsername(username));
    }

    @PutMapping("/users/{username}")
    public ResponseEntity update(@RequestHeader("X-User-Header") String loggedUser, @RequestBody UserGeneral newUser, @PathVariable String username) {
    	System.out.println(newUser.getUsername());
    	return userService.update(loggedUser,newUser);
    }

    @DeleteMapping("/users/{username}")
    public ResponseEntity delete(@PathVariable String username) {
        return userService.delete(username);
    }

    @RequestMapping(value="/signup", method = RequestMethod.POST)
    public ResponseEntity<Account> saveNewUser(@RequestBody UserGeneral user){
        return userService.save(user);
    }

    @RequestMapping(value="/recover-email/{username}", method = RequestMethod.GET)
    public String recoverEmail(@PathVariable String username) {
    	Account user = userService.findByUsername(username);
    	if(user != null) {
    		return user.getEmail();
    	}
    	return null;
    }


}