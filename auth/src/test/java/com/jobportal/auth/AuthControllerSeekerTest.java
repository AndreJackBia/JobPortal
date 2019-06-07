package com.jobportal.auth;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.codehaus.jettison.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobportal.auth.config.JwtAuthenticationEntryPoint;
import com.jobportal.auth.config.JwtTokenUtil;
import com.jobportal.auth.controller.UserController;
import com.jobportal.auth.dao.UserRepo;
import com.jobportal.auth.model.Account;
import com.jobportal.auth.model.UserGeneral;
import com.jobportal.auth.proxy.CenterEntityProxy;
import com.jobportal.auth.proxy.NotificationEntityProxy;
import com.jobportal.auth.proxy.SeekerEntityProxy;
import com.jobportal.auth.service.UserService;
import com.jobportal.auth.service.impl.UserServiceImpl;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.*;


@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
@EnableAutoConfiguration
public class AuthControllerSeekerTest {

	@TestConfiguration
    static class UserServiceImplTestContextConfiguration {
  
        @Bean
        public UserService userService() {
            return new UserServiceImpl();
        }
    }
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	UserRepo userRepo;
	
	@Autowired
    UserService userService;

	@MockBean
	CenterEntityProxy centerProxy;
	
	@MockBean
	SeekerEntityProxy seekerProxy;
	
	@MockBean
	NotificationEntityProxy	notificationProxy;
	
	@MockBean 
	JwtAuthenticationEntryPoint jwt;
	
	@MockBean
	JwtTokenUtil jwtu;
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);

	}

	@Test
	public void test58_saveUserSeeker() throws Exception {
		
		String user = "{\"type\":\"SEEKER\",\"username\":\"andrejackbia\",\"email\":\"biaggi.jack@gmail.com\",\"firstName\":\"Andrea\",\"password\":\"costa\",\"lastName\":\"Biaggi\",\"city\":\"Milano\",\"birth\":\"1995-10-27\"}";

		Account sa = new Account();
		sa.setId(0);
		sa.setUsername(null);
		sa.setPassword("costa");
		sa.setEmail("a.biaggi1@campus.unimib.it");
		
		given(userRepo.save(any(Account.class))).willReturn(sa);
				
		mvc.perform(MockMvcRequestBuilders.post("/signup")
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(user.toString()))
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
				.andExpect(jsonPath("$.username").doesNotExist());
	}
	
	@Test
	public void test67_saveUserSeeker() throws Exception {
		
		String user = "{\"type\":\"SEEKER\",\"username\":\"andrejackbia\",\"email\":\"biaggi.jack@gmail.com\",\"firstName\":\"Andrea\",\"password\":\"costa\",\"lastName\":\"Biaggi\",\"birth\":\"1995-10-27\"}";

		Account sa = new Account();
		sa.setId(0);
		sa.setUsername("andrejackbia");
		sa.setPassword("costa");
		sa.setEmail("a.biaggi1@campus.unimib.it");
		
		given(userRepo.save(any(Account.class))).willReturn(sa);
		
		mvc.perform(MockMvcRequestBuilders.post("/signup")
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(user.toString()))
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
				.andExpect(jsonPath("$.username", is("andrejackbia")));	
	}
	
	@Test
	public void test75_saveUserSeeker() throws Exception {
		
		String user = "{\"type\":\"SEEKER\",\"username\":\"andrejackbia\",\"email\":\"biaggi.jack@gmail.com\",\"firstName\":\"Andrea\",\"password\":\"costa\",\"lastName\":\"Biaggi\",\"city\":\"Milano\",\"birth\":\"1995-10-27\", \"skills\": [\"Java\"]}";
		
		Account sa = new Account();
		
		sa.setId(0);
		sa.setUsername("andrejackbia");
		sa.setPassword("costa");
		sa.setEmail("a.biaggi1@campus.unimib.it");

		given(userRepo.save(any(Account.class))).willReturn(sa);

		
		mvc.perform(MockMvcRequestBuilders.post("/signup")
				.content(user)
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
				.andExpect(jsonPath("$.username", is("andrejackbia")));

	}
}
