package com.jobportal.jobcenters;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.util.NestedServletException;

@RunWith(SpringRunner.class)
@WebMvcTest(JobCentersController.class)
@EnableAutoConfiguration
public class JobCentersControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	JobCentersRepository jobCenterRepository;

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
	 * TEST 1
	 * 
	 * Get an existing job center
	 * 
	 */
	@Test
	public void test1_getExistingJobCenterByUsername() throws Exception {
		JobCenterEntity jobCenter0 = new JobCenterEntity(0, "Adecco", "adec", "adecco.adec@gmail.com");

		given(jobCenterRepository.findByUsername("adec")).willReturn(jobCenter0);

		mvc.perform(MockMvcRequestBuilders.get("/api/centers/adec").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("username", is(jobCenter0.getUsername())));

	}
	/**
	 * TEST 2
	 * 
	 * Get job center that doesn't exist
	 * 
	 */
	@Test
	public void test2_getNotExistingJobCenterByUsername() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/api/centers/adec").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
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
	 * Sign up with name null
	 * 
	 */
	@Test(expected = NestedServletException.class)
	public void test12_CreateJobCenter() throws Exception {
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
	 * TEST 13
	 * 
	 * Sign up with name empty
	 * 
	 */
	@Test(expected = NestedServletException.class)
	public void test13_CreateJobCenter() throws Exception {
		JobCenterEntity jobEntity = this.jobEntity;
		jobEntity.setName("");

		JSONObject jsonObject = new JSONObject().put("username", "loluca").put("name", "").put("email", "luca@luca.it")
				.put("id", 1);

		given(jobCenterRepository.save(jobEntity)).willThrow(ConstraintViolationException.class);

		mvc.perform(MockMvcRequestBuilders.post("/api/centers").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(jsonObject.toString())).andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
				.andExpect(MockMvcResultMatchers.forwardedUrl(null));
	}

	/**
	 * TEST 14
	 * 
	 * Sign up with email null
	 * 
	 */
	@Test(expected = NestedServletException.class)
	public void test14_CreateJobCenter() throws Exception {
		JobCenterEntity jobEntity = this.jobEntity;
		jobEntity.setEmail(null);

		JSONObject jsonObject = new JSONObject().put("username", "loluca").put("name", "luca").put("email", null)
				.put("id", 1);

		given(jobCenterRepository.save(jobEntity)).willThrow(ConstraintViolationException.class);

		mvc.perform(MockMvcRequestBuilders.post("/api/centers").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(jsonObject.toString())).andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
				.andExpect(MockMvcResultMatchers.forwardedUrl(null));
	}

	/**
	 * TEST 15
	 * 
	 * Sign up with email empty
	 * 
	 */
	@Test(expected = NestedServletException.class)
	public void test15_CreateJobCenter() throws Exception {
		JobCenterEntity jobEntity = this.jobEntity;
		jobEntity.setEmail("");

		JSONObject jsonObject = new JSONObject().put("username", "loluca").put("name", "luca").put("email", "")
				.put("id", 1);

		given(jobCenterRepository.save(jobEntity)).willThrow(ConstraintViolationException.class);

		mvc.perform(MockMvcRequestBuilders.post("/api/centers").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(jsonObject.toString())).andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
				.andExpect(MockMvcResultMatchers.forwardedUrl(null));
	}

	/**
	 * TEST 16
	 * 
	 * Sign up with invalid email
	 * 
	 */
	@Test(expected = NestedServletException.class)
	public void test16_CreateJobCenter() throws Exception {
		JobCenterEntity jobEntity = this.jobEntity;
		jobEntity.setEmail("invalid");

		JSONObject jsonObject = new JSONObject().put("username", "loluca").put("name", "luca").put("email", "invalid")
				.put("id", 1);

		given(jobCenterRepository.save(jobEntity)).willThrow(ConstraintViolationException.class);

		mvc.perform(MockMvcRequestBuilders.post("/api/centers").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(jsonObject.toString())).andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
				.andExpect(MockMvcResultMatchers.forwardedUrl(null));
	}

	/**
	 * TEST 17
	 * 
	 * Sign up with valid fields but existing username
	 * 
	 */
	@Test(expected = NestedServletException.class)
	public void test17_CreateJobCenter() throws Exception {
		JobCenterEntity jobEntity = this.jobEntity;

		JSONObject jsonObject = new JSONObject().put("username", "loluca").put("name", "luca")
				.put("email", "luca@luca.it").put("id", 1);

		given(jobCenterRepository.save(jobEntity)).willThrow(DuplicateKeyException.class);

		MvcResult result = mvc
				.perform(MockMvcRequestBuilders.post("/api/centers").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.content(jsonObject.toString()))
				.andExpect(MockMvcResultMatchers.status().is(201)).andDo(MockMvcResultHandlers.print()).andReturn();
	}

	/**
	 * TEST 18
	 * 
	 * Sign up with valid fields and non existing username
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
	}

	/**
	 * TEST 19
	 * 
	 * Delete existing job center with same username and loggeduser
	 * 
	 */
	@Test
	public void test19_deleteJobCenter() throws Exception {
		JobCenterEntity jobCenter2 = new JobCenterEntity(0, "Gigroup", "gg", "gg@gmail.com");
		given(jobCenterRepository.findByUsername("gg")).willReturn(jobCenter2);
		given(jobsProxy.deleteAllByUsername("gg", jobCenter2.getUsername())).willReturn(ResponseEntity.ok().build());

		mvc.perform(MockMvcRequestBuilders.delete("/api/centers/gg").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.header("X-User-Header", "gg")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	/**
	 * TEST 20
	 * 
	 *  Delete existing job center with different username and loggeduser
	 * 
	 */
	@Test
	public void test20_deleteJobCenter() throws Exception {
		JobCenterEntity jobCenter2 = new JobCenterEntity(0, "Gigroup", "gg", "gg@gmail.com");
		given(jobCenterRepository.findByUsername("gg")).willReturn(jobCenter2);
		given(jobsProxy.deleteAllByUsername("gg", jobCenter2.getUsername())).willReturn(ResponseEntity.ok().build());

		mvc.perform(MockMvcRequestBuilders.delete("/api/centers/gg").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.header("X-User-Header", "gbg")).andExpect(MockMvcResultMatchers.status().isUnauthorized());
	}

	/**
	 * TEST 21
	 * 
	 *  Try to delete non existing job center with same username and loggeduser
	 * 
	 */
	@Test
	public void test21_deleteJobCenter() throws Exception {
		given(jobCenterRepository.findByUsername("gg")).willReturn(null);

		mvc.perform(MockMvcRequestBuilders.delete("/api/centers/gg").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.header("X-User-Header", "gg")).andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	/**
	 * TEST 30
	 * 
	 *  Update job center with different path variable username and loggeduser
	 * 
	 */
	@Test
	public void test30_updateJobCenter() throws Exception {
		JSONObject jsonObject = new JSONObject().put("username", "loluca").put("name", "luca")
				.put("email", "luca@luca.it").put("id", 1);

		given(jobCenterRepository.findByUsername("loluca")).willReturn(jobEntity);

		mvc.perform(MockMvcRequestBuilders.put("/api/centers/luca").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.header("X-User-Header", "gg").content(jsonObject.toString()))
				.andExpect(MockMvcResultMatchers.status().isUnauthorized());
	}

	/**
	 * TEST 31
	 * 
	 *  Update job center with same path variable username and loggeduser and differente object field username
	 * 
	 */
	@Test
	public void test31_updateJobCenter() throws Exception {
		JSONObject jsonObject = new JSONObject().put("username", "different").put("name", "gigroup")
				.put("email", "gg@gmail.com").put("id", 0);

		JobCenterEntity jobCenter2 = new JobCenterEntity(0, "different", "gigroup", "gg@gmail.com");

		given(jobCenterRepository.findByUsername("gg")).willReturn(jobCenter2);

		mvc.perform(MockMvcRequestBuilders.put("/api/centers/gg").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.header("X-User-Header", "gg").content(jsonObject.toString()))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	/**
	 * TEST 32
	 * 
	 *  Update job center with same path variable username and loggeduser and null object field name
	 * 
	 */
	@Test
	public void test32_updateJobCenter() throws Exception {
		JSONObject jsonObject = new JSONObject().put("username", "gg").put("name", null).put("email", "gg@gmail.com")
				.put("id", 0);

		JobCenterEntity jobCenter2 = new JobCenterEntity(0, null, "gg", "gg@gmail.com");

		given(jobCenterRepository.findByUsername("gg")).willReturn(jobCenter2);

		mvc.perform(MockMvcRequestBuilders.put("/api/centers/gg").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.header("X-User-Header", "gg").content(jsonObject.toString()))
				.andExpect(MockMvcResultMatchers.status().isPartialContent());
	}

	/**
	 * TEST 33
	 * 
	 *  Update job center with same path variable username and loggeduser and empty object field name
	 * 
	 */
	@Test(expected = NestedServletException.class)
	public void test33_updateJobCenter() throws Exception {
		JSONObject jsonObject = new JSONObject().put("username", "gg").put("name", "").put("email", "gg@gmail.com")
				.put("id", 0);

		JobCenterEntity jobCenter2 = new JobCenterEntity(0, "", "gg", "gg@gmail.com");

		given(jobCenterRepository.findByUsername("gg")).willReturn(jobCenter2);
		given(jobCenterRepository.save(jobCenter2)).willThrow(ConstraintViolationException.class);

		mvc.perform(MockMvcRequestBuilders.put("/api/centers/gg").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.header("X-User-Header", "gg").content(jsonObject.toString()))
				.andExpect(MockMvcResultMatchers.status().isPartialContent());
	}

	/**
	 * TEST 34
	 * 
	 *  Update job center with same path variable username and loggeduser and null object field email
	 * 
	 */
	@Test
	public void test34_updateJobCenter() throws Exception {
		JSONObject jsonObject = new JSONObject().put("username", "gg").put("name", "gigroup").put("email", null)
				.put("id", 0);

		JobCenterEntity jobCenter2 = new JobCenterEntity(0, "gigroup", "gg", null);

		given(jobCenterRepository.findByUsername("gg")).willReturn(jobCenter2);

		mvc.perform(MockMvcRequestBuilders.put("/api/centers/gg").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.header("X-User-Header", "gg").content(jsonObject.toString()))
				.andExpect(MockMvcResultMatchers.status().isPartialContent());
	}

	/**
	 * TEST 35
	 * 
	 *  Update job center with same path variable username and loggeduser and empty object field email
	 * 
	 */
	@Test(expected = NestedServletException.class)
	public void test35_updateJobCenter() throws Exception {
		JSONObject jsonObject = new JSONObject().put("username", "gg").put("name", "Gigroup").put("email", "").put("id",
				0);

		JobCenterEntity jobCenter2 = new JobCenterEntity(0, "Gigroup", "gg", "");

		given(jobCenterRepository.findByUsername("gg")).willReturn(jobCenter2);
		given(jobCenterRepository.save(jobCenter2)).willThrow(ConstraintViolationException.class);

		mvc.perform(MockMvcRequestBuilders.put("/api/centers/gg").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.header("X-User-Header", "gg").content(jsonObject.toString()))
				.andExpect(MockMvcResultMatchers.status().isPartialContent());
	}

	/**
	 * TEST 36
	 * 
	 *  Update job center with same path variable username and loggeduser and not valid object field email
	 * 
	 */
	@Test(expected = NestedServletException.class)
	public void test36_updateJobCenter() throws Exception {
		JSONObject jsonObject = new JSONObject().put("username", "gg").put("name", "Gigroup").put("email", "aaa")
				.put("id", 0);

		JobCenterEntity jobCenter2 = new JobCenterEntity(0, "Gigroup", "gg", "aaa");

		given(jobCenterRepository.findByUsername("gg")).willReturn(jobCenter2);
		given(jobCenterRepository.save(jobCenter2)).willThrow(ConstraintViolationException.class);

		mvc.perform(MockMvcRequestBuilders.put("/api/centers/gg").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.header("X-User-Header", "gg").content(jsonObject.toString()))
				.andExpect(MockMvcResultMatchers.status().isPartialContent());
	}

	/**
	 * TEST 37
	 * 
	 * Try to Update a non existing job center with valid fields
	 * 
	 */
	@Test
	public void test37_updateJobCenter() throws Exception {
		JSONObject jsonObject = new JSONObject().put("username", "gg").put("name", "Gigroup")
				.put("email", "gg@gmail.com").put("id", 0);

		given(jobCenterRepository.findByUsername("gg")).willReturn(null);

		mvc.perform(MockMvcRequestBuilders.put("/api/centers/gg").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.header("X-User-Header", "gg").content(jsonObject.toString()))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	/**
	 * TEST 38
	 * 
	 * Update a non existing job center with valid fields
	 * 
	 */
	@Test
	public void test38_updateJobCenter() throws Exception {
		JSONObject jsonObject = new JSONObject().put("username", "gg").put("name", "Gigroup")
				.put("email", "gg@gmail.com").put("id", 0);

		JobCenterEntity jobCenter2 = new JobCenterEntity(0, "Gigroup", "gg", "gg@gmail.com");

		given(jobCenterRepository.findByUsername("gg")).willReturn(jobCenter2);
		given(jobCenterRepository.save(jobCenter2)).willReturn(jobCenter2);

		mvc.perform(MockMvcRequestBuilders.put("/api/centers/gg").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.header("X-User-Header", "gg").content(jsonObject.toString()))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	/**
	 * TEST 76
	 * 
	 * Get the list of job centers when the list is empty
	 * 
	 */
	@Test
	public void test76_getJobCenters() throws Exception {
				
		
		given(jobCenterRepository.findAll()).willReturn(new ArrayList());
		
		mvc.perform(MockMvcRequestBuilders.get("/api/centers"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(0)));
	}

	/**
	 * TEST 77
	 * 
	 * Get the list of job centers when the list contains one element
	 * 
	 */
	@Test
	public void test77_getJobCenters() throws Exception {
		JobCenterEntity jobCenter1 = new JobCenterEntity(0, "Gigroup", "gg", "gg@gmail.com");
		
		List<JobCenterEntity> centers = new ArrayList<JobCenterEntity>();
		centers.add(jobCenter1);
		
		given(jobCenterRepository.findAll()).willReturn(centers);
		
		mvc.perform(MockMvcRequestBuilders.get("/api/centers"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(1)));
	}
	
	/**
	 * TEST 78
	 * 
	 * Get the list of job centers when the list contains more than one element
	 * 
	 */
	@Test
	public void test78_getJobCenters() throws Exception {
		JobCenterEntity jobCenter1 = new JobCenterEntity(0, "Gigroup", "gg", "gg@gmail.com");
		JobCenterEntity jobCenter2 = new JobCenterEntity(1, "Adecco", "adecco", "adecco@adecco.it");
		
		List<JobCenterEntity> centers = new ArrayList<JobCenterEntity>();
		centers.add(jobCenter1);
		centers.add(jobCenter2);
		
		given(jobCenterRepository.findAll()).willReturn(centers);
		
		mvc.perform(MockMvcRequestBuilders.get("/api/centers"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(2)));
	}

}
