package com.jobportal.auth;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import javax.validation.ConstraintViolationException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.jobportal.auth.dao.UserRepo;
import com.jobportal.auth.model.Account;
import com.jobportal.auth.model.Account.Role;
import com.jobportal.auth.model.UserGeneral;
import com.jobportal.auth.model.UserSeeker;
import com.jobportal.auth.proxy.CenterEntityProxy;
import com.jobportal.auth.proxy.NotificationEntityProxy;
import com.jobportal.auth.proxy.SeekerEntity;
import com.jobportal.auth.proxy.SeekerEntityProxy;
import com.jobportal.auth.service.UserService;
import com.jobportal.auth.service.impl.UserServiceImpl;

@RunWith(SpringRunner.class)
public class UserServiceTest {

	@TestConfiguration
	static class EmployeeServiceImplTestContextConfiguration {

		@Bean
		public UserService employeeService() {
			return new UserServiceImpl();
		}
	}

	@Autowired
	private UserService userService;

	@MockBean
	private UserRepo userRepository;

	@MockBean
	CenterEntityProxy centerProxy;

	@MockBean
	SeekerEntityProxy seekerProxy;

	@MockBean
	NotificationEntityProxy	notificationProxy;

	@MockBean
	BCryptPasswordEncoder bcryptEncoder;

	@Before
	public void setUp() {
			
		//[id=0, email=email@email.com, username=andrejackbia, password=costa, role=SEEKER]
		
		Account sa = new Account();
		
		sa.setId(0);
		sa.setUsername("andrejackbia");
		sa.setPassword("costa");
		sa.setEmail("email@email.com");
		sa.setRole(Role.SEEKER);
		
		given(userRepository.existsByUsername("andrejackbia")).willReturn(false);		
		given(bcryptEncoder.encode("costa")).willReturn("costa");
		given(userRepository.findByUsername(null)).willThrow(new BadCredentialsException("LOL"));
		given(userRepository.save(sa)).willReturn(sa);
	}

	@Test(expected = BadCredentialsException.class)
	public void whenInvalidUsername_thenExceptionShouldBeThrown() {
		String username = null;
		Account account = userService.findOne(username);

		//assertThat(account.getUsername(), equalToIgnoringCase(username));
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void test58_saveUserSeeker() throws Exception {
		
		Account sa = new Account();
		
		sa.setId(0);
		sa.setUsername(null);
		sa.setPassword("costa");
		sa.setEmail("email@email.com");
		sa.setRole(Role.SEEKER);
		
		given(seekerProxy.createSeeker(any(SeekerEntity.class))).willThrow(ConstraintViolationException.class);
		given(userRepository.save(sa)).willThrow(ConstraintViolationException.class);
		
		UserSeeker ug = new UserSeeker();
		
		ug.setUsername(null);
		ug.setPassword("costa");
		ug.setEmail("email@email.com");
		ug.setFirstName("andrea");
		
		ResponseEntity<Account> r = userService.save(ug);
		Account sug = r.getBody();
		assertEquals(sug.getUsername(), "andrejackbia");

	}
	
	@Test(expected = ConstraintViolationException.class)
	public void test75_saveUserSeeker() throws Exception {
		
		given(seekerProxy.createSeeker(any(SeekerEntity.class))).willThrow(ConstraintViolationException.class);
		
		UserSeeker ug = new UserSeeker();
		
		ug.setUsername("andrejackbia");
		ug.setPassword("costa");
		ug.setEmail("email@email.com");
		ug.setFirstName("andrea");
		
		ResponseEntity<Account> r = userService.save(ug);
		Account sug = r.getBody();
		assertEquals(sug.getUsername(), "andrejackbia");

	}
}