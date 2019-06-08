package com.jobportal.auth;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.hamcrest.Matchers;
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
import org.springframework.security.core.Authentication;
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

@RunWith(SpringRunner.class)
@WebMvcTest(AuthenticationController.class)
//@AutoConfigureMockMvc(secure = false)
//@ContextConfiguration(exclude=WebSecurityConfig.class)
public class AuthControllerCenterTest {
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
		
		given(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(null, "costa")))
		.willThrow(new BadCredentialsException("Bad credentials!"));
		
		given(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("", "costa")))
		.willThrow(new BadCredentialsException("Bad credentials!"));
		
		given(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("Batman", "costa")))
		.willThrow(new BadCredentialsException("Bad credentials!"));
		
		given(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("Andrea", null)))
		.willThrow(new BadCredentialsException("Bad credentials!"));
		
		given(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("Andrea", "")))
		.willThrow(new BadCredentialsException("Bad credentials!"));
		
		given(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("Andrea", "wrongPassword")))
		.willThrow(new BadCredentialsException("Bad credentials!"));
		
		given(userService.findOne("Andrea"))
		.willReturn(account);
		
		given(jwtTokenUtil.generateToken(account))
		.willReturn("THIS IS A BEAUTIFUL TOKEN");

		//given(authenticationManager.authenticate(org.mockito.BDDMockito.any()))
		//.willThrow(new BadCredentialsException("Bad credentials!"));
	}

	@Test
	public void whenInvalidUsername_thenExceptionShouldBeThrown() {
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
	
	@Test
	public void whenValid_thenOk() {
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
}