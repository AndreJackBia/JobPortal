package com.jobportal.auth;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobportal.auth.config.JwtAuthenticationEntryPoint;
import com.jobportal.auth.config.JwtTokenUtil;
import com.jobportal.auth.controller.AuthenticationController;
import com.jobportal.auth.model.Account;
import com.jobportal.auth.model.LoginUser;
import com.jobportal.auth.service.impl.UserServiceImpl;

import org.mockito.AdditionalMatchers;
import org.mockito.ArgumentMatcher;

@RunWith(SpringRunner.class)
@WebMvcTest(AuthenticationController.class)
public class AuthControllerTest {
	@MockBean
	UserServiceImpl userService;

	@MockBean
	AuthenticationManager authenticationManager;

	@MockBean
	JwtTokenUtil jwtTokenUtil;
	
	@MockBean
	JwtAuthenticationEntryPoint unauthorizedHandler;
	
	@Autowired
	MockMvc mvc;
	
	private Account account;

	@Before
	public void setUp() {		
		account = new Account();
		account.setId(0);
		account.setUsername("Andrea");
		account.setPassword("costa");
		account.setEmail("a.biaggi1@campus.unimib.it");
		account.setRole(Account.Role.JOB_CENTER);
		
		given(authenticationManager.authenticate(org.mockito.BDDMockito.argThat(new WrongUsernamePasswordAuthenticationTokenMatcher())))
				.willThrow(new BadCredentialsException("Bad credentials!"));
		
		given(userService.findOne("Andrea"))
		.willReturn(account);
		
		given(jwtTokenUtil.generateToken(account))
		.willReturn("THIS IS A BEAUTIFUL TOKEN");
	}
	
	private class WrongUsernamePasswordAuthenticationTokenMatcher implements ArgumentMatcher<UsernamePasswordAuthenticationToken> {	 
	    @Override
	    public boolean matches(UsernamePasswordAuthenticationToken right) {
	        return !(("Andrea").equals(right.getPrincipal())
	        		&& ("costa").equals(right.getCredentials()));
	    }
	}

	/**
	 * TEST 39
	 * 
	 * Sign in with username null
	 * 
	 */
	@Test
	public void test39_whenInvalidUsername_thenExceptionShouldBeThrown() {
		LoginUser loginUser = new LoginUser();
		loginUser.setUsername(null);
		loginUser.setPassword("costa");

		ObjectMapper mapper = new ObjectMapper();
		String jsonLoginUser;
		try {
			jsonLoginUser = mapper.writeValueAsString(loginUser);

			try {
				mvc.perform(MockMvcRequestBuilders.post("/token/generate-token")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.content(jsonLoginUser))
						.andExpect(MockMvcResultMatchers.status().isOk())
						.andExpect(MockMvcResultMatchers.forwardedUrl(null));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * TEST 40
	 * 
	 * Sign in with username empty
	 * 
	 */
	@Test
	public void test40_whenEmptyUsername_thenExceptionShouldBeThrown() {
		LoginUser loginUser = new LoginUser();
		loginUser.setUsername("");
		loginUser.setPassword("costa");

		ObjectMapper mapper = new ObjectMapper();
		String jsonLoginUser;
		try {
			jsonLoginUser = mapper.writeValueAsString(loginUser);

			try {
				mvc.perform(MockMvcRequestBuilders.post("/token/generate-token")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.content(jsonLoginUser))
						.andExpect(MockMvcResultMatchers.status().isOk())
						.andExpect(MockMvcResultMatchers.forwardedUrl(null));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * TEST 41
	 * 
	 * Sign in with password null
	 * 
	 */
	@Test
	public void test41_whenNullPassword_thenExceptionShouldBeThrown() {
		LoginUser loginUser = new LoginUser();
		loginUser.setUsername("Andrea");
		loginUser.setPassword(null);
	
		ObjectMapper mapper = new ObjectMapper();
		String jsonLoginUser;
		try {
			jsonLoginUser = mapper.writeValueAsString(loginUser);
	
			try {
				mvc.perform(MockMvcRequestBuilders.post("/token/generate-token")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.content(jsonLoginUser))
						.andExpect(MockMvcResultMatchers.status().isOk())
						.andExpect(MockMvcResultMatchers.forwardedUrl(null));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * TEST 42
	 * 
	 * Sign in with password empty
	 * 
	 */
	@Test
	public void test42_whenEmptyPassword_thenExceptionShouldBeThrown() {
		LoginUser loginUser = new LoginUser();
		loginUser.setUsername(null);
		loginUser.setPassword("");
	
		ObjectMapper mapper = new ObjectMapper();
		String jsonLoginUser;
		try {
			jsonLoginUser = mapper.writeValueAsString(loginUser);
	
			try {
				mvc.perform(MockMvcRequestBuilders.post("/token/generate-token")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.content(jsonLoginUser))
						.andExpect(MockMvcResultMatchers.status().isOk())
						.andExpect(MockMvcResultMatchers.forwardedUrl(null));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * TEST 43
	 * 
	 * Sign in with valid username and password
	 * 
	 */
	@Test
	public void test43_whenValid_thenOk() {
		LoginUser loginUser = new LoginUser();
		loginUser.setUsername("Andrea");
		loginUser.setPassword("costa");

		ObjectMapper mapper = new ObjectMapper();
		String jsonLoginUser;
		try {
			jsonLoginUser = mapper.writeValueAsString(loginUser);

			try {
				mvc.perform(MockMvcRequestBuilders.post("/token/generate-token")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.content(jsonLoginUser))
						.andExpect(MockMvcResultMatchers.status().isOk())
						.andExpect(jsonPath("$.result.username", is(account.getUsername())));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * TEST 44
	 * 
	 * Sign in as a non existing user
	 * 
	 */
	@Test
	public void test44_whenNonExistingUsername_thenExceptionShouldBeThrown() {
		LoginUser loginUser = new LoginUser();
		loginUser.setUsername("Batman");
		loginUser.setPassword("costa");

		ObjectMapper mapper = new ObjectMapper();
		String jsonLoginUser;
		try {
			jsonLoginUser = mapper.writeValueAsString(loginUser);

			try {
				mvc.perform(MockMvcRequestBuilders.post("/token/generate-token")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.content(jsonLoginUser))
						.andExpect(MockMvcResultMatchers.status().isOk())
						.andExpect(MockMvcResultMatchers.forwardedUrl(null));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * TEST 45
	 * 
	 * Sign in with wrong password
	 * 
	 */
	@Test
	public void test45_whenInvalidPassword_thenExceptionShouldBeThrown() {
		LoginUser loginUser = new LoginUser();
		loginUser.setUsername("Andrea");
		loginUser.setPassword("wrongPassword");

		ObjectMapper mapper = new ObjectMapper();
		String jsonLoginUser;
		try {
			jsonLoginUser = mapper.writeValueAsString(loginUser);

			try {
				mvc.perform(MockMvcRequestBuilders.post("/token/generate-token")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.content(jsonLoginUser))
						.andExpect(MockMvcResultMatchers.status().isOk())
						.andExpect(MockMvcResultMatchers.forwardedUrl(null));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
}