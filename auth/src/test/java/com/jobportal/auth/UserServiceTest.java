package com.jobportal.auth;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

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
import com.jobportal.auth.model.UserCenter;
import com.jobportal.auth.model.UserSeeker;
import com.jobportal.auth.proxy.CenterEntityProxy;
import com.jobportal.auth.proxy.JobCenterEntity;
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

	private Account accountSeeker;
	private UserSeeker seeker;
	
	private Account accountCenter;
	private UserCenter center;
	
	@Before
	public void setUp() {
			
		accountSeeker = new Account();
		accountSeeker.setId(0);
		accountSeeker.setUsername("andrejackbia");
		accountSeeker.setPassword("costa");
		accountSeeker.setEmail("a.biaggi1@campus.unimib.it");
		accountSeeker.setRole(Role.SEEKER);
		
		seeker = new UserSeeker();
		seeker.setUsername("andrejackbia");
		seeker.setPassword("costa");
		seeker.setEmail("a.biaggi1@campus.unimib.it");
		seeker.setFirstName("andrea");
		seeker.setLastName("biaggi");
		seeker.setCity("Milano");
		seeker.setBirth(new Date());
		seeker.setSkills(Arrays.asList("Java", "OOP", "SQL"));
		
		accountCenter = new Account();
		accountCenter.setId(0);
		accountCenter.setUsername("matteo94");
		accountCenter.setPassword("campana");
		accountCenter.setEmail("mc94@gmail.it");
		accountCenter.setRole(Role.JOB_CENTER);
		
		center = new UserCenter();
		center.setUsername("matteo94");
		center.setPassword("campana");
		center.setEmail("mc94@gmail.it");
		center.setCenterName("Incredible Center");
		
		given(userRepository.existsByUsername("andrejackbia")).willReturn(false);		
		given(bcryptEncoder.encode(accountSeeker.getPassword())).willReturn(accountSeeker.getPassword());
		given(bcryptEncoder.encode(accountCenter.getPassword())).willReturn(accountCenter.getPassword());
		given(userRepository.findByUsername(null)).willThrow(new BadCredentialsException("Bad Credentials!"));
	}

	/*@Test(expected = BadCredentialsException.class)
	public void whenInvalidUsername_thenExceptionShouldBeThrown() {
		String username = null;
		Account account = userService.findOne(username);

		//assertThat(account.getUsername(), equalToIgnoringCase(username));
	}*/
	
	/**
	 * TEST 54
	 * 
	 * Sign up with username null
	 * 
	 */
	@Test(expected = ConstraintViolationException.class)
	public void test54_saveUserCenter() throws Exception {
		
		Account sa = accountCenter;
		sa.setUsername(null);
		
		given(userRepository.existsByUsername("matteo94")).willReturn(false);	
		given(centerProxy.createCenter(any(JobCenterEntity.class))).willThrow(ConstraintViolationException.class);
		given(userRepository.save(sa)).willThrow(ConstraintViolationException.class);
		
		UserCenter uc = center;
		uc.setUsername(null);
		
		userService.save(uc);
		
		assertEquals(null, uc.getUsername());
	}
	
	/**
	 * TEST 55
	 * 
	 * Sign up with username empty
	 * 
	 */
	@Test(expected = ConstraintViolationException.class)
	public void test55_saveUserCenter() throws Exception {
		
		Account sa = accountCenter;
		sa.setUsername("");
		
		given(userRepository.existsByUsername("matteo94")).willReturn(false);	
		given(centerProxy.createCenter(any(JobCenterEntity.class))).willThrow(ConstraintViolationException.class);
		given(userRepository.save(sa)).willThrow(ConstraintViolationException.class);
		
		UserCenter uc = center;
		uc.setUsername("");
		
		userService.save(uc);
		
		assertEquals(null, uc.getUsername());
	}
	
	/**
	 * TEST 56
	 * 
	 * Sign up with an existing username
	 * 
	 */
	@Test
	public void test56_saveUserCenter() throws Exception {
		
		Account sa = accountCenter;
		
		given(userRepository.existsByUsername("matteo94")).willReturn(true);
		given(userRepository.save(sa)).willReturn(sa);
		
		UserCenter uc = center;
		
		ResponseEntity<Account> r = userService.save(uc);
		
		assertEquals(409, r.getStatusCodeValue());

	}
	
	/**
	 * TEST 57
	 * 
	 * Sign up with password null
	 * 
	 */
	@Test(expected = ConstraintViolationException.class)
	public void test57_saveUserCenter() throws Exception {
		
		Account sa = accountCenter;
		sa.setPassword(null);
		
		given(userRepository.existsByUsername("matteo94")).willReturn(false);	
		given(centerProxy.createCenter(any(JobCenterEntity.class))).willThrow(ConstraintViolationException.class);
		given(userRepository.save(sa)).willThrow(ConstraintViolationException.class);
		
		UserCenter uc = center;
		uc.setPassword(null);
		
		userService.save(uc);
		
		assertEquals(null, uc.getPassword());

	}
	
	/**
	 * TEST 58
	 * 
	 * Sign up with password empty
	 * 
	 */
	@Test(expected = ConstraintViolationException.class)
	public void test58_saveUserCenter() throws Exception {
		
		Account sa = accountCenter;
		sa.setPassword("");
		
		given(userRepository.existsByUsername("matteo94")).willReturn(false);	
		given(centerProxy.createCenter(any(JobCenterEntity.class))).willThrow(ConstraintViolationException.class);
		given(userRepository.save(sa)).willThrow(ConstraintViolationException.class);
		
		UserCenter uc = center;
		uc.setPassword("");
		
		userService.save(uc);
		
		assertEquals("", uc.getPassword());
	}
	
	/**
	 * TEST 59
	 * 
	 * Sign up with email null
	 * 
	 */
	@Test(expected = ConstraintViolationException.class)
	public void test59_saveUserCenter() throws Exception {
		
		Account sa = accountCenter;
		sa.setEmail(null);
		
		given(userRepository.existsByUsername("matteo94")).willReturn(false);	
		given(centerProxy.createCenter(any(JobCenterEntity.class))).willThrow(ConstraintViolationException.class);
		given(userRepository.save(sa)).willThrow(ConstraintViolationException.class);
		
		UserCenter uc = center;
		uc.setEmail(null);
		
		userService.save(uc);
		
		assertEquals(null, uc.getEmail());
	}
	
	/**
	 * TEST 60
	 * 
	 * Sign up with email empty
	 * 
	 */
	@Test(expected = ConstraintViolationException.class)
	public void test60_saveUserCenter() throws Exception {
		
		Account sa = accountCenter;
		sa.setEmail("");
		
		given(userRepository.existsByUsername("matteo94")).willReturn(false);	
		given(centerProxy.createCenter(any(JobCenterEntity.class))).willThrow(ConstraintViolationException.class);
		given(userRepository.save(sa)).willThrow(ConstraintViolationException.class);
		
		UserCenter uc = center;
		uc.setEmail("");
		
		userService.save(uc);
		
		assertEquals("", uc.getEmail());
	}
	
	/**
	 * TEST 61
	 * 
	 * Sign up with email invalid
	 * 
	 */
	@Test(expected = ConstraintViolationException.class)
	public void test61_saveUserCenter() throws Exception {
		
		Account sa = accountCenter;
		sa.setEmail("crke938");
		
		given(userRepository.existsByUsername("matteo94")).willReturn(false);	
		given(centerProxy.createCenter(any(JobCenterEntity.class))).willThrow(ConstraintViolationException.class);
		given(userRepository.save(sa)).willThrow(ConstraintViolationException.class);
		
		UserCenter uc = center;
		uc.setEmail("crke938");
		
		userService.save(uc);
	}
	
	/**
	 * TEST 62
	 * 
	 * Sign up with name null
	 * 
	 */
	@Test(expected = ConstraintViolationException.class)
	public void test62_saveUserCenter() throws Exception {
		
		Account sa = accountCenter;
		
		given(userRepository.existsByUsername("matteo94")).willReturn(false);	
		given(centerProxy.createCenter(any(JobCenterEntity.class))).willThrow(ConstraintViolationException.class);
		given(userRepository.save(sa)).willThrow(ConstraintViolationException.class);
		
		UserCenter uc = center;
		uc.setCenterName(null);
		
		userService.save(uc);
	}
	
	/**
	 * TEST 63
	 * 
	 * Sign up with name empty
	 * 
	 */
	@Test(expected = ConstraintViolationException.class)
	public void test63_saveUserCenter() throws Exception {
		
		Account sa = accountCenter;
		
		given(userRepository.existsByUsername("matteo94")).willReturn(false);	
		given(centerProxy.createCenter(any(JobCenterEntity.class))).willThrow(ConstraintViolationException.class);
		given(userRepository.save(sa)).willThrow(ConstraintViolationException.class);
		
		UserCenter uc = center;
		uc.setCenterName("");
		
		userService.save(uc);
	}
	
	/**
	 * TEST 64
	 * 
	 * Sign up with a valid input
	 * 
	 */
	@Test
	public void test64_saveUserSeeker() throws Exception {
		
		Account sa = accountCenter;
		
		given(userRepository.existsByUsername("matteo94")).willReturn(false);
		given(userRepository.save(sa)).willReturn(sa);
		
		UserCenter uc = center;
		
		ResponseEntity<Account> r = userService.save(uc);
		
		Account sus = r.getBody();

		assertEquals(sa, sus);

	}
	
	/**
	 * TEST 65
	 * 
	 * Sign up with username null
	 * 
	 */
	@Test(expected = ConstraintViolationException.class)
	public void test65_saveUserSeeker() throws Exception {
		
		Account sa = accountSeeker;
		sa.setUsername(null);
		
		given(userRepository.existsByUsername("andrejackbia")).willReturn(false);	
		given(seekerProxy.createSeeker(any(SeekerEntity.class))).willThrow(ConstraintViolationException.class);
		given(userRepository.save(sa)).willThrow(ConstraintViolationException.class);
		
		UserSeeker us = seeker;
		us.setUsername(null);
		
		userService.save(us);
		
		assertEquals(null, us.getUsername());

	}
	
	/**
	 * TEST 66
	 * 
	 * Sign up with username empty
	 * 
	 */
	@Test(expected = ConstraintViolationException.class)
	public void test66_saveUserSeeker() throws Exception {
		
		Account sa = accountSeeker;
		sa.setUsername("");
		
		given(userRepository.existsByUsername("andrejackbia")).willReturn(false);	
		given(seekerProxy.createSeeker(any(SeekerEntity.class))).willThrow(ConstraintViolationException.class);
		given(userRepository.save(sa)).willThrow(ConstraintViolationException.class);
		
		UserSeeker us = seeker;
		us.setUsername("");
		
		userService.save(us);
		
		assertEquals("", us.getUsername());

	}
	
	/**
	 * TEST 67
	 * 
	 * Sign up with an existing username
	 * 
	 */
	@Test
	public void test67_saveUserSeeker() throws Exception {
		
		Account sa = accountSeeker;
		
		given(userRepository.existsByUsername("andrejackbia")).willReturn(true);
		given(userRepository.save(sa)).willReturn(sa);
		
		UserSeeker us = seeker;
		
		ResponseEntity<Account> r = userService.save(us);
		
		assertEquals(409, r.getStatusCodeValue());

	}
	
	/**
	 * TEST 68
	 * 
	 * Sign up with password null
	 * 
	 */
	@Test(expected = ConstraintViolationException.class)
	public void test68_saveUserSeeker() throws Exception {
		
		Account sa = accountSeeker;
		sa.setPassword(null);
		
		given(userRepository.existsByUsername("andrejackbia")).willReturn(false);	
		given(seekerProxy.createSeeker(any(SeekerEntity.class))).willThrow(ConstraintViolationException.class);
		given(userRepository.save(sa)).willThrow(ConstraintViolationException.class);
		
		UserSeeker us = seeker;
		us.setPassword(null);
		
		userService.save(us);
		
		assertEquals(null, us.getPassword());

	}
	
	/**
	 * TEST 69
	 * 
	 * Sign up with password empty
	 * 
	 */
	@Test(expected = ConstraintViolationException.class)
	public void test69_saveUserSeeker() throws Exception {
		
		Account sa = accountSeeker;
		sa.setPassword("");
		
		given(userRepository.existsByUsername("andrejackbia")).willReturn(false);	
		given(seekerProxy.createSeeker(any(SeekerEntity.class))).willThrow(ConstraintViolationException.class);
		given(userRepository.save(sa)).willThrow(ConstraintViolationException.class);
		
		UserSeeker us = seeker;
		us.setPassword("");
		
		userService.save(us);
		
		assertEquals("", us.getPassword());

	}
	
	/**
	 * TEST 70
	 * 
	 * Sign up with first name null
	 * 
	 */
	@Test(expected = ConstraintViolationException.class)
	public void test70_saveUserSeeker() throws Exception {
		
		Account sa = accountSeeker;
		
		given(userRepository.existsByUsername("andrejackbia")).willReturn(false);	
		given(seekerProxy.createSeeker(any(SeekerEntity.class))).willThrow(ConstraintViolationException.class);
		given(userRepository.save(sa)).willReturn(sa);
		
		UserSeeker us = seeker;
		us.setFirstName(null);
		
		Account sus = userService.save(us).getBody();
		
		assertEquals(sus.getUsername(), us.getUsername());
		assertEquals(null, us.getFirstName());

	}
	
	/**
	 * TEST 71
	 * 
	 * Sign up with first name empty
	 * 
	 */
	@Test(expected = ConstraintViolationException.class)
	public void test71_saveUserSeeker() throws Exception {
		
		Account sa = accountSeeker;
		
		given(userRepository.existsByUsername("andrejackbia")).willReturn(false);	
		given(seekerProxy.createSeeker(any(SeekerEntity.class))).willThrow(ConstraintViolationException.class);
		given(userRepository.save(sa)).willReturn(sa);
		
		UserSeeker us = seeker;
		us.setFirstName("");
		
		Account sus = userService.save(us).getBody();
		
		assertEquals(sus.getUsername(), us.getUsername());
		assertEquals("", us.getFirstName());

	}
	
	/**
	 * TEST 72
	 * 
	 * Sign up with last name null
	 * 
	 */
	@Test(expected = ConstraintViolationException.class)
	public void test72_saveUserSeeker() throws Exception {
		
		Account sa = accountSeeker;
		
		given(userRepository.existsByUsername("andrejackbia")).willReturn(false);	
		given(seekerProxy.createSeeker(any(SeekerEntity.class))).willThrow(ConstraintViolationException.class);
		given(userRepository.save(sa)).willReturn(sa);
		
		UserSeeker us = seeker;
		us.setLastName(null);
		
		Account sus = userService.save(us).getBody();
		
		assertEquals(sus.getUsername(), us.getUsername());
		assertEquals(null, us.getLastName());

	}
	
	/**
	 * TEST 73
	 * 
	 * Sign up with first name empty
	 * 
	 */
	@Test(expected = ConstraintViolationException.class)
	public void test73_saveUserSeeker() throws Exception {
		
		Account sa = accountSeeker;
		
		given(userRepository.existsByUsername("andrejackbia")).willReturn(false);	
		given(seekerProxy.createSeeker(any(SeekerEntity.class))).willThrow(ConstraintViolationException.class);
		given(userRepository.save(sa)).willReturn(sa);
		
		UserSeeker us = seeker;
		us.setLastName("");
		
		Account sus = userService.save(us).getBody();
		
		assertEquals(sus.getUsername(), us.getUsername());
		assertEquals("", us.getLastName());

	}
	
	/**
	 * TEST 74
	 * 
	 * Sign up with city null
	 * 
	 */
	@Test(expected = ConstraintViolationException.class)
	public void test74_saveUserSeeker() throws Exception {
		
		Account sa = accountSeeker;
		
		given(userRepository.existsByUsername("andrejackbia")).willReturn(false);	
		given(seekerProxy.createSeeker(any(SeekerEntity.class))).willThrow(ConstraintViolationException.class);
		given(userRepository.save(sa)).willReturn(sa);
		
		UserSeeker us = seeker;
		us.setCity(null);
		
		Account sus = userService.save(us).getBody();
		
		assertEquals(sus.getUsername(), us.getUsername());
		assertEquals(null, us.getCity());

	}
	
	/**
	 * TEST 75
	 * 
	 * Sign up with city empty
	 * 
	 */
	@Test(expected = ConstraintViolationException.class)
	public void tes75_saveUserSeeker() throws Exception {
		
		Account sa = accountSeeker;
		
		given(userRepository.existsByUsername("andrejackbia")).willReturn(false);	
		given(seekerProxy.createSeeker(any(SeekerEntity.class))).willThrow(ConstraintViolationException.class);
		given(userRepository.save(sa)).willReturn(sa);
		
		UserSeeker us = seeker;
		us.setCity("");
		
		Account sus = userService.save(us).getBody();
		
		assertEquals(sus.getUsername(), us.getUsername());
		assertEquals("", us.getCity());

	}
	
	/**
	 * TEST 76
	 * 
	 * Sign up with skills null
	 * 
	 */
	@Test(expected = ConstraintViolationException.class)
	public void test76_saveUserSeeker() throws Exception {
		
		Account sa = accountSeeker;
		
		given(userRepository.existsByUsername("andrejackbia")).willReturn(false);	
		given(seekerProxy.createSeeker(any(SeekerEntity.class))).willThrow(ConstraintViolationException.class);
		given(userRepository.save(sa)).willReturn(sa);
		
		UserSeeker us = seeker;
		us.setSkills(null);
		
		Account sus = userService.save(us).getBody();
		
		assertEquals(sus.getUsername(), us.getUsername());
		assertEquals(null, us.getSkills());

	}
	
	/**
	 * TEST 77
	 * 
	 * Sign up with skills empty
	 * 
	 */
	@Test
	public void test77_saveUserSeeker() throws Exception {
		
		Account sa = accountSeeker;
		
		given(userRepository.existsByUsername("andrejackbia")).willReturn(false);	
		given(seekerProxy.createSeeker(any(SeekerEntity.class))).willReturn(ResponseEntity.ok(accountSeeker));
		given(userRepository.save(sa)).willReturn(sa);
		
		UserSeeker us = seeker;
		us.setSkills(new ArrayList<String>());
		
		ResponseEntity<Account> r = userService.save(us);
		
		System.out.println(r);
		
		Account sus = r.getBody();
		assertEquals("andrejackbia", sus.getUsername());
		assertEquals(0, us.getSkills().size());
		
	}
	
	
	/**
	 * TEST 78
	 * 
	 * Sign up with birth null
	 * 
	 */
	@Test(expected = ConstraintViolationException.class)
	public void test78_saveUserSeeker() throws Exception {
		
		Account sa = accountSeeker;
		
		given(userRepository.existsByUsername("andrejackbia")).willReturn(false);	
		given(seekerProxy.createSeeker(any(SeekerEntity.class))).willThrow(ConstraintViolationException.class);
		given(userRepository.save(sa)).willReturn(sa);
		
		UserSeeker us = seeker;
		us.setBirth(null);
		
		Account sus = userService.save(us).getBody();
		
		assertEquals(sus.getUsername(), us.getUsername());
		assertEquals(null, us.getBirth());
	}
	
	/**
	 * TEST 79
	 * 
	 * Sign up with email null
	 * 
	 */
	@Test(expected = ConstraintViolationException.class)
	public void test79_saveUserSeeker() throws Exception {
		
		Account sa = accountSeeker;
		sa.setEmail(null);
		
		given(userRepository.existsByUsername("andrejackbia")).willReturn(false);	
		given(seekerProxy.createSeeker(any(SeekerEntity.class))).willThrow(ConstraintViolationException.class);
		given(userRepository.save(sa)).willReturn(sa);
		
		UserSeeker us = seeker;
		us.setEmail(null);
		
		Account sus = userService.save(us).getBody();
		
		assertEquals(sus.getUsername(), us.getUsername());
		assertEquals(null, us.getEmail());
	}
	
	/**
	 * TEST 80
	 * 
	 * Sign up with email empty
	 * 
	 */
	@Test(expected = ConstraintViolationException.class)
	public void test80_saveUserSeeker() throws Exception {
		
		Account sa = accountSeeker;
		sa.setEmail("");
		
		given(userRepository.existsByUsername("andrejackbia")).willReturn(false);	
		given(seekerProxy.createSeeker(any(SeekerEntity.class))).willThrow(ConstraintViolationException.class);
		given(userRepository.save(sa)).willReturn(sa);
		
		UserSeeker us = seeker;
		us.setEmail("");
		
		Account sus = userService.save(us).getBody();
		
		assertEquals(sus.getUsername(), us.getUsername());
		assertEquals("", us.getEmail());
	}
	
	/**
	 * TEST 81
	 * 
	 * Sign up with non valid email
	 * 
	 */
	@Test
	public void test81_saveUserSeeker() throws Exception {
		
		Account sa = accountSeeker;
		sa.setEmail("a@a.com");
		
		given(userRepository.existsByUsername("andrejackbia")).willReturn(false);
		given(seekerProxy.createSeeker(any(SeekerEntity.class))).willReturn(ResponseEntity.ok().build());
		given(userRepository.save(sa)).willReturn(sa);
		
		UserSeeker us = seeker;
		us.setEmail("a@a.com");
		
		Account sus = userService.save(us).getBody();
		
		assertEquals(sus.getUsername(), us.getUsername());
		assertEquals("a@a.com", us.getEmail());
	}
	
	/**
	 * TEST 82
	 * 
	 * Sign up with a valid input
	 * 
	 */
	@Test
	public void test82_saveUserSeeker() throws Exception {
		
		Account sa = accountSeeker;
		
		given(userRepository.existsByUsername("andrejackbia")).willReturn(false);
		given(userRepository.save(sa)).willReturn(sa);
		
		UserSeeker us = seeker;
		
		ResponseEntity<Account> r = userService.save(us);
		
		Account sus = r.getBody();

		assertEquals(sa, sus);

	}
	
}