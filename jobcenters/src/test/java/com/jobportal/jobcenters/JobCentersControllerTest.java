package com.jobportal.jobcenters;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.HashSet;

import javax.validation.ConstraintViolationException;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.util.NestedServletException;

import static org.mockito.BDDMockito.given;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobportal.jobcenters.JobCenterController;

@RunWith(SpringRunner.class)
@WebMvcTest(JobCenterController.class)
@EnableAutoConfiguration
public class JobCentersControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	JobCenterRepository jobCenterRepository;

	@MockBean
	JobsProxy jobsProxy;

	private JobCenterEntity jobEntity;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		jobEntity = new JobCenterEntity();
		jobEntity.setId(1);
		jobEntity.setName("luca");
		jobEntity.setUsername("loluca");
		jobEntity.setEmail("luca@luca.it");
	}

	/**
	 * TEST 10
	 * 
	 * Sign up with username null
	 * 
	 */
	@Test(expected = NestedServletException.class)
	public void test10_CreateJobCenter() throws Exception {

		JobCenterEntity jobEntity = this.jobEntity;
		jobEntity.setUsername(null);
		JSONObject jsonObject = new JSONObject().put("username", null).put("name", "luca").put("email", "luca@luca.it")
				.put("id", 1);

		given(jobCenterRepository.save(jobEntity)).willThrow(ConstraintViolationException.class);

		mvc.perform(MockMvcRequestBuilders.post("/api/centers").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(jsonObject.toString())).andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
				.andDo(MockMvcResultHandlers.print()).andReturn();
	}

	/**
	 * TEST 11
	 * 
	 * Sign up with username empty
	 * 
	 */
	@Test(expected = NestedServletException.class)
	public void test11_CreateJobCenter() throws Exception {
		JobCenterEntity jobEntity = this.jobEntity;
		jobEntity.setUsername("");

		JSONObject jsonObject = new JSONObject().put("username", "").put("name", "luca").put("email", "luca@luca.it")
				.put("id", 1);

		given(jobCenterRepository.save(jobEntity)).willThrow(ConstraintViolationException.class);

		mvc.perform(MockMvcRequestBuilders.post("/api/centers").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(jsonObject.toString())).andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
				.andExpect(MockMvcResultMatchers.forwardedUrl(null));
	}

	/**
	 * TEST 12
	 * 
	 * Sign up with invalid username
	 * 
	 */
//	@Test(expected = NestedServletException.class)
//	public void test12_CreateJobCenter() throws Exception {
//		JSONObject jsonObject = new JSONObject().put("username", "").put("name", "luca").put("email", "luca@luca.it")
//				.put("id", 1);
//
//		given(jobCenterRepository.save(jobEntity)).willThrow(ConstraintViolationException.class);
//
//		mvc.perform(MockMvcRequestBuilders.post("/api/centers").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
//				.content(jsonObject.toString())).andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
//				.andExpect(MockMvcResultMatchers.forwardedUrl(null));
//	}

	/**
	 * TEST 13
	 * 
	 * Sign up with name null
	 * 
	 */
	@Test(expected = NestedServletException.class)
	public void test13_CreateJobCenter() throws Exception {
		JobCenterEntity jobEntity = this.jobEntity;
		jobEntity.setName(null);

		JSONObject jsonObject = new JSONObject().put("username", "loluca").put("name", null)
				.put("email", "luca@luca.it").put("id", 1);

		given(jobCenterRepository.save(jobEntity)).willThrow(ConstraintViolationException.class);

		mvc.perform(MockMvcRequestBuilders.post("/api/centers").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(jsonObject.toString())).andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
				.andExpect(MockMvcResultMatchers.forwardedUrl(null));
	}
	
	/**
	 * TEST 14
	 * 
	 * Sign up with name empty
	 * 
	 */
	@Test(expected = NestedServletException.class)
	public void test14_CreateJobCenter() throws Exception {
		JobCenterEntity jobEntity = this.jobEntity;
		jobEntity.setName("");

		JSONObject jsonObject = new JSONObject().put("username", "loluca").put("name", "")
				.put("email", "luca@luca.it").put("id", 1);

		given(jobCenterRepository.save(jobEntity)).willThrow(ConstraintViolationException.class);

		mvc.perform(MockMvcRequestBuilders.post("/api/centers").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(jsonObject.toString())).andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
				.andExpect(MockMvcResultMatchers.forwardedUrl(null));
	}
	/**
	 * TEST 15
	 * 
	 * Sign up with email null
	 * 
	 */
	@Test(expected = NestedServletException.class)
	public void test15_CreateJobCenter() throws Exception {
		JobCenterEntity jobEntity = this.jobEntity;
		jobEntity.setEmail(null);

		JSONObject jsonObject = new JSONObject().put("username", "loluca").put("name", "luca")
				.put("email", null).put("id", 1);

		given(jobCenterRepository.save(jobEntity)).willThrow(ConstraintViolationException.class);

		mvc.perform(MockMvcRequestBuilders.post("/api/centers").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(jsonObject.toString())).andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
				.andExpect(MockMvcResultMatchers.forwardedUrl(null));
	}
	
	/**
	 * TEST 16
	 * 
	 * Sign up with email empty
	 * 
	 */
	@Test(expected = NestedServletException.class)
	public void test16_CreateJobCenter() throws Exception {
		JobCenterEntity jobEntity = this.jobEntity;
		jobEntity.setEmail("");

		JSONObject jsonObject = new JSONObject().put("username", "loluca").put("name", "luca")
				.put("email", "").put("id", 1);

		given(jobCenterRepository.save(jobEntity)).willThrow(ConstraintViolationException.class);

		mvc.perform(MockMvcRequestBuilders.post("/api/centers").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(jsonObject.toString())).andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
				.andExpect(MockMvcResultMatchers.forwardedUrl(null));
	}
	/**
	 * TEST 17
	 * 
	 * Sign up with invalid email
	 * 
	 */
	@Test(expected = NestedServletException.class)
	public void test17_CreateJobCenter() throws Exception {
		JobCenterEntity jobEntity = this.jobEntity;
		jobEntity.setEmail("invalid");

		JSONObject jsonObject = new JSONObject().put("username", "loluca").put("name", "luca")
				.put("email", "invalid").put("id", 1);

		given(jobCenterRepository.save(jobEntity)).willThrow(ConstraintViolationException.class);

		mvc.perform(MockMvcRequestBuilders.post("/api/centers").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(jsonObject.toString())).andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
				.andExpect(MockMvcResultMatchers.forwardedUrl(null));
	}
	
	/**
	 * TEST 18
	 * 
	 * Sign up with valid fields
	 * 
	 */

	@Test
	public void test18_CreateJobCenter() throws Exception {
		JobCenterEntity jobEntity = this.jobEntity;

		JSONObject jsonObject = new JSONObject().put("username", "loluca").put("name", "luca")
				.put("email", "luca@luca.it").put("id", 1);

		given(jobCenterRepository.save(jobEntity)).willReturn(jobEntity);

		MvcResult result = mvc
				.perform(MockMvcRequestBuilders.post("/api/centers").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.content(jsonObject.toString()))
				.andExpect(MockMvcResultMatchers.status().is(201)).andDo(MockMvcResultHandlers.print()).andReturn();
		System.out.println(result.getResponse().getContentAsString());
	}
}
