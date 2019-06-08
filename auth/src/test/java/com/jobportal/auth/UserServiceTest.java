package com.jobportal.auth;

import static org.mockito.BDDMockito.given;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.jobportal.auth.dao.UserRepo;
import com.jobportal.auth.model.Account;
import com.jobportal.auth.proxy.CenterEntityProxy;
import com.jobportal.auth.proxy.NotificationEntityProxy;
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
		given(userRepository.findByUsername(null)).willThrow(new BadCredentialsException("LOL"));
	}

	@Test(expected = BadCredentialsException.class)
	public void whenInvalidUsername_thenExceptionShouldBeThrown() {
		String username = null;
		Account account = userService.findOne(username);

		//assertThat(account.getUsername(), equalToIgnoringCase(username));
	}
}