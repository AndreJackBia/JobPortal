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
import com.jobportal.auth.model.Account;
import com.jobportal.auth.model.Account.Role;
import com.jobportal.auth.model.UserCenter;
import com.jobportal.auth.model.UserGeneral;
import com.jobportal.auth.model.UserSeeker;
import com.jobportal.auth.proxy.JobCenterProxy;
import com.jobportal.auth.proxy.JobCenterEntity;
import com.jobportal.auth.proxy.NotificationEntity;
import com.jobportal.auth.proxy.NotificationProxy;
import com.jobportal.auth.proxy.SeekerEntity;
import com.jobportal.auth.proxy.SeekerProxy;
import com.jobportal.auth.service.UserService;

@Service(value = "userService")
public class UserServiceImpl implements UserDetailsService, UserService {

	@Autowired
	private JobCenterProxy centerProxy;

	@Autowired
	private SeekerProxy seekerProxy;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private BCryptPasswordEncoder bcryptEncoder;

	@Autowired
	private NotificationProxy notificationProxy;

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Account user = userRepo.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				getAuthority());
	}

	private List<SimpleGrantedAuthority> getAuthority() {
		return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
	}

	public List<Account> findAll() {
		List<Account> list = new ArrayList<>();
		userRepo.findAll().iterator().forEachRemaining(list::add);
		return list;
	}

	@Override
	public ResponseEntity delete(String username) {
		Account user = userRepo.findByUsername(username);
		userRepo.delete(user);
		if (Account.Role.SEEKER.equals(user.getRole()))
			seekerProxy.deleteSeeker(username, user.getUsername());
		else if (Account.Role.JOB_CENTER.equals(user.getRole()))
			centerProxy.deleteCenter(username, user.getUsername());
		return ResponseEntity.ok().build();
	}

	@Override
	public Account findOne(String username) {
		return userRepo.findByUsername(username);
	}

	@Override
	public Account findById(long id) {
		Optional<Account> optionalUser = userRepo.findById(id);
		return optionalUser.isPresent() ? optionalUser.get() : null;
	}

	@Override
	public ResponseEntity update(String loggedUser, UserGeneral newUser) {
		String username = newUser.getUsername();
		Account user = findByUsername(username);
		if(!loggedUser.equals(newUser.getUsername())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		if (newUser.getPassword() != null) {
			user.setPassword(bcryptEncoder.encode(newUser.getPassword()));
		}

		user.setEmail(newUser.getEmail());

		if (newUser instanceof UserSeeker) {
			user.setRole(Role.SEEKER);
			if (user.getRole() != Role.SEEKER)
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			UserSeeker newUs = (UserSeeker) newUser;
			seekerProxy.changeSeeker(loggedUser, username,
					new SeekerEntity(newUs.getUsername(), newUs.getFirstName(), newUs.getLastName(), newUs.getEmail(),
							newUs.getCity(), newUs.getBirth(), newUs.getSkills()));
		} else if (newUser instanceof UserCenter) {
			user.setRole(Role.JOB_CENTER);
			if (user.getRole() != Role.JOB_CENTER)
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			UserCenter newUc = (UserCenter) newUser;
			centerProxy.changeCenter(loggedUser, username,
					new JobCenterEntity(newUc.getCenterName(), newUc.getUsername(), newUc.getEmail()));

		}

		userRepo.save(user);
		return ResponseEntity.ok().build();
	}

	@Override
	public Account findByUsername(String username) {
		return userRepo.findByUsername(username);
	}

	@Override
	public ResponseEntity<Account> save(UserGeneral user) {
		Account newUser = new Account();
		newUser.setUsername(user.getUsername());
		newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
		if (user instanceof UserSeeker)
			newUser.setRole(Role.SEEKER);
		else if (user instanceof UserCenter)
			newUser.setRole(Role.JOB_CENTER);
		newUser.setEmail(user.getEmail());
		
		if (!userRepo.existsByUsername(user.getUsername())) {
			dispatchUser(user);
			Account newUserSave = userRepo.save(newUser);
			return ResponseEntity.ok().body(newUserSave);
		} else {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
	}

	public void dispatchUser(UserGeneral user) {
		if (user instanceof UserSeeker) {
			UserSeeker us = (UserSeeker) user;
			SeekerEntity newSeeker = new SeekerEntity(us.getUsername(), us.getFirstName(), us.getLastName(),
					us.getEmail(), us.getCity(), us.getBirth(), us.getSkills());
			seekerProxy.createSeeker(newSeeker);
			sendEmail(new NotificationEntity(us.getEmail(), "Benvenuto nel portale di lavoro più famoso al mondo!",
					"Benvenuto caro Seeker " + us.getFirstName(), null));
		} else if (user instanceof UserCenter) {
			UserCenter uc = (UserCenter) user;
			centerProxy.createCenter(new JobCenterEntity(uc.getCenterName(), uc.getUsername(), uc.getEmail()));
			sendEmail(new NotificationEntity(uc.getEmail(), "Benvenuto nel portale di lavoro più famoso al mondo!",
					"Benvenuto caro Center " + uc.getCenterName(), null));
		}
		/*
		 * else if(user instanceof UserAdmin) { //TODO IN FUTURE RELEASES
		 */
	}

	public void sendEmail(NotificationEntity notificationEntity) {
		notificationProxy.sendNotification(notificationEntity);
	}

}