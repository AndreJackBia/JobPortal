package com.jobportal.auth.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.jobportal.auth.dao.UserRepo;
import com.jobportal.auth.model.User;
import com.jobportal.auth.model.UserGeneral;
import com.jobportal.auth.proxy.CenterEntityProxy;
import com.jobportal.auth.proxy.JobCenterEntity;
import com.jobportal.auth.proxy.SeekerEntity;
import com.jobportal.auth.proxy.SeekerEntityProxy;
import com.jobportal.auth.service.UserService;


@Service(value = "userService")
public class UserServiceImpl implements UserDetailsService, UserService {

	@Autowired
	private CenterEntityProxy centerEntityProxy;

	@Autowired
	private SeekerEntityProxy seekerEntityProxy;

	@Autowired
	private UserRepo userRepo;


	@Autowired
	private BCryptPasswordEncoder bcryptEncoder;

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.findByUsername(username);
		if(user == null){
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getAuthority());
	}

	private List<SimpleGrantedAuthority> getAuthority() {
		return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
	}

	public List<User> findAll() {
		List<User> list = new ArrayList<>();
		userRepo.findAll().iterator().forEachRemaining(list::add);
		return list;
	}

	@Override
	public ResponseEntity delete(String username) {
		User user = userRepo.findByUsername(username);
		userRepo.delete(user);
		if (User.Role.SEEKER.equals(user.getRole()))
			seekerEntityProxy.deleteSeeker(username, user.getUsername());
		else if (User.Role.JOB_CENTER.equals(user.getRole()))
			centerEntityProxy.deleteCenter(username, user.getUsername());
		return ResponseEntity.ok().build();
	}

	@Override
	public User findOne(String username) {
		return userRepo.findByUsername(username);
	}

	@Override
	public User findById(long id) {
		Optional<User> optionalUser = userRepo.findById(id);
		return optionalUser.isPresent() ? optionalUser.get() : null;
	}

    @Override
    public ResponseEntity update(String loggedUser, UserGeneral newUser) {
        String username = newUser.getUsername();
    	User user = findByUsername(username);
        user.setUsername(newUser.getUsername());
        if(newUser.getPassword() != null) {
        	user.setPassword(bcryptEncoder.encode(newUser.getPassword()));
        }
        user.setRole(newUser.getRole());
        user.setEmail(newUser.getEmail());
        if(!(user.getRole().equals(newUser.getRole()))) {
        	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (User.Role.SEEKER.equals(newUser.getRole())) {
        	seekerEntityProxy.changeSeeker(loggedUser, username,
        			new SeekerEntity(newUser.getUsername(), newUser.getFirstName(), newUser.getLastName(), newUser.getEmail(), newUser.getCity(), newUser.getBirth(), newUser.getSkills()));
        }

        if (User.Role.JOB_CENTER.equals(newUser.getRole())) {
        	centerEntityProxy.changeCenter(loggedUser, username,
        			new JobCenterEntity(newUser.getCenterName(), newUser.getUsername(), newUser.getEmail()));
        }
        userRepo.save(user);
        return ResponseEntity.ok().build();
    }

    @Override
    public User findByUsername(String username) {
    	return userRepo.findByUsername(username);
    }

    @Override
    public ResponseEntity<User> save(UserGeneral user) {
	    User newUser = new User();
	    newUser.setUsername(user.getUsername());
	    newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
		newUser.setRole(user.getRole());
		newUser.setEmail(user.getEmail());
		if (!userRepo.existsByUsername(user.getUsername())) {
			dispatchUser(user);
			User newUserSave = userRepo.save(newUser);
			return ResponseEntity.ok().body(newUserSave);
		} else {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
    }

    public void dispatchUser(UserGeneral user) {
    	if(user.getRole().equals(User.Role.JOB_CENTER)) {
    		centerEntityProxy.createCenter(new JobCenterEntity(user.getCenterName(), user.getUsername(), user.getEmail()));
    		//TODO SEND MAIL 
    	}
    	else if(user.getRole().equals(User.Role.SEEKER)) {
    		SeekerEntity newSeeker = new SeekerEntity(user.getUsername(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getCity(), user.getBirth(), user.getSkills());
    		seekerEntityProxy.createSeeker(newSeeker);
    		//TODO SEND MAIL
    	}
    	else if(user.getRole().equals(User.Role.ADMIN)) {

    	}
    }

 

}