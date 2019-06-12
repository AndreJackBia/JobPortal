package com.jobportal.jobcenters;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import javax.validation.ConstraintViolationException;

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
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.util.NestedServletException;

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

	@Test
	public void test1_getExistingJobCenterByUsername() throws Exception {
		JobCenterEntity jobCenter0 = new JobCenterEntity(0, "Adecco", "adec", "adecco.adec@gmail.com");

		given(jobCenterRepository.findByUsername("adec")).willReturn(jobCenter0);

		mvc.perform(MockMvcRequestBuilders.get("/api/centers/adec").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("username", is(jobCenter0.getUsername())));

	}

	@Test
	public void test2_getNotExistingJobCenterByUsername() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/api/centers/adec").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	/**
	 * TEST 8
	 * 
	 * Sign up with username null
	 * 
	 */
	@Test(expected = NestedServletException.class)
	public void test8_CreateJobCenter() throws Exception {

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
	 * TEST 9
	 * 
	 * Sign up with username empty
	 * 
	 */
	@Test(expected = NestedServletException.class)
	public void test9_CreateJobCenter() throws Exception {
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
	 * TEST 10
	 * 
	 * Sign up with valid fields
	 * 
	 */

	@Test
	public void test10_CreateJobCenter() throws Exception {
		JobCenterEntity jobEntity = this.jobEntity;

		JSONObject jsonObject = new JSONObject().put("username", "Gigroup").put("name", "gg")
				.put("email", "gg@gmail.com").put("id", 1);

		given(jobCenterRepository.save(jobEntity)).willReturn(jobEntity);

		MvcResult result = mvc
				.perform(MockMvcRequestBuilders.post("/api/centers").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.content(jsonObject.toString()))
				.andExpect(MockMvcResultMatchers.status().is(201)).andDo(MockMvcResultHandlers.print()).andReturn();
		System.out.println(result.getResponse().getContentAsString());
	}

	/**
	 * TEST 11
	 * 
	 * Sign up with name null
	 * 
	 */
	@Test(expected = NestedServletException.class)
	public void test11_CreateJobCenter() throws Exception {
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
	 * TEST 12
	 * 
	 * Sign up with name empty
	 * 
	 */
	@Test(expected = NestedServletException.class)
	public void test12_CreateJobCenter() throws Exception {
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
	 * TEST 13
	 * 
	 * Sign up with email null
	 * 
	 */
	@Test(expected = NestedServletException.class)
	public void test13_CreateJobCenter() throws Exception {
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
	 * TEST 14
	 * 
	 * Sign up with email empty
	 * 
	 */
	@Test(expected = NestedServletException.class)
	public void test14_CreateJobCenter() throws Exception {
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
	 * TEST 15
	 * 
	 * Sign up with invalid email
	 * 
	 */
	@Test(expected = NestedServletException.class)
	public void test15_CreateJobCenter() throws Exception {
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
	 * TEST 16
	 * 
	 * Sign up with valid fields
	 * 
	 */

	@Test
	public void test16_CreateJobCenter() throws Exception {
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

	/**
	 * TEST 17
	 * 
	 * unexisting header
	 * 
	 */

	@Test(expected = IllegalArgumentException.class)
	public void test17_deleteJobCenter() throws Exception {
		mvc.perform(MockMvcRequestBuilders.delete("/api/centers/gg").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.header("X-User-Header", null)).andExpect(MockMvcResultMatchers.status().isUnauthorized());
	}

	/**
	 * TEST 18
	 * 
	 * center not existing
	 * 
	 */
	@Test
	public void test18_deleteJobCenter() throws Exception {
		mvc.perform(MockMvcRequestBuilders.delete("/api/centers/gg").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.header("X-User-Header", "")).andExpect(MockMvcResultMatchers.status().isUnauthorized());
	}

	/**
	 * TEST 19
	 * 
	 * center not existing
	 * 
	 */
	@Test
	public void test19_deleteJobCenter() throws Exception {
		mvc.perform(MockMvcRequestBuilders.delete("/api/centers/gg").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.header("X-User-Header", "")).andExpect(MockMvcResultMatchers.status().isUnauthorized());
	}

	/**
	 * TEST 20
	 * 
	 * existing
	 * 
	 */

	@Test
	public void test20_deleteJobCenter() throws Exception {
		JobCenterEntity jobCenter2 = new JobCenterEntity(0, "Gigroup", "gg", "gg@gmail.com");
		given(jobCenterRepository.findByUsername("gg")).willReturn(jobCenter2);
		given(jobsProxy.deleteAllByUsername("gg", jobCenter2.getUsername())).willReturn(ResponseEntity.ok().build());

		mvc.perform(MockMvcRequestBuilders.delete("/api/centers/gg").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.header("X-User-Header", "gg")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	/**
	 * TEST 21
	 * 
	 * existing
	 * 
	 */

	@Test
	public void test21_deleteJobCenter() throws Exception {
		given(jobCenterRepository.findByUsername("gg")).willReturn(null);

		mvc.perform(MockMvcRequestBuilders.delete("/api/centers/gg").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.header("X-User-Header", "gg")).andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	public void test30_updateJobCenter() throws Exception {
		JSONObject jsonObject = new JSONObject().put("username", "gg").put("name", "Gigroup")
				.put("email", "gg@gmail.com").put("id", 0);

		given(jobCenterRepository.findByUsername("gg")).willReturn(null);

		mvc.perform(MockMvcRequestBuilders.put("/api/centers/gg").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.header("X-User-Header", "gg").content(jsonObject.toString()))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test(expected = IllegalArgumentException.class)
	public void test31_updateJobCenter() throws Exception {
		JSONObject jsonObject = new JSONObject().put("username", "gg").put("name", "Gigroup")
				.put("email", "gg@gmail.com").put("id", 0);

		mvc.perform(MockMvcRequestBuilders.put("/api/centers/gg").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.header("X-User-Header", null).content(jsonObject.toString()))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	public void test32_updateJobCenter() throws Exception {
		JSONObject jsonObject = new JSONObject().put("username", "gg").put("name", "Gigroup")
				.put("email", "gg@gmail.com").put("id", 0);

		mvc.perform(MockMvcRequestBuilders.put("/api/centers/gg").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.header("X-User-Header", "").content(jsonObject.toString()))
				.andExpect(MockMvcResultMatchers.status().isUnauthorized());
	}

	@Test
	public void test33_updateJobCenter() throws Exception {
		JSONObject jsonObject = new JSONObject().put("username", "gg").put("name", "Gigroup")
				.put("email", "gg@gmail.com").put("id", 0);

		mvc.perform(MockMvcRequestBuilders.put("/api/centers/gg").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.header("X-User-Header", "different").content(jsonObject.toString()))
				.andExpect(MockMvcResultMatchers.status().isUnauthorized());
	}

	@Test
	public void test38_updateJobCenter() throws Exception {
		JSONObject jsonObject = new JSONObject().put("username", "gg").put("name", null).put("email", "gg@gmail.com")
				.put("id", 0);

		JobCenterEntity jobCenter2 = new JobCenterEntity(0, null, "gg", "gg@gmail.com");

		given(jobCenterRepository.findByUsername("gg")).willReturn(jobCenter2);

		mvc.perform(MockMvcRequestBuilders.put("/api/centers/gg").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.header("X-User-Header", "gg").content(jsonObject.toString()))
				.andExpect(MockMvcResultMatchers.status().isPartialContent());
	}

	@Test
	public void test40_updateJobCenter() throws Exception {
		JSONObject jsonObject = new JSONObject().put("username", "gg").put("name", "gigroup").put("email", null)
				.put("id", 0);

		JobCenterEntity jobCenter2 = new JobCenterEntity(0, "gigroup", "gg", null);

		given(jobCenterRepository.findByUsername("gg")).willReturn(jobCenter2);

		mvc.perform(MockMvcRequestBuilders.put("/api/centers/gg").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.header("X-User-Header", "gg").content(jsonObject.toString()))
				.andExpect(MockMvcResultMatchers.status().isPartialContent());
	}

	@Test
	public void test34_updateJobCenter() throws Exception {
		JSONObject jsonObject = new JSONObject().put("username", null).put("name", "gigroup")
				.put("email", "gg@gmail.com").put("id", 0);

		JobCenterEntity jobCenter2 = new JobCenterEntity(0, null, "gg", "gg@gmail.com");

		given(jobCenterRepository.findByUsername("gg")).willReturn(jobCenter2);

		mvc.perform(MockMvcRequestBuilders.put("/api/centers/gg").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.header("X-User-Header", "gg").content(jsonObject.toString()))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	public void test35_updateJobCenter() throws Exception {
		JSONObject jsonObject = new JSONObject().put("username", "").put("name", "gigroup").put("email", "gg@gmail.com")
				.put("id", 0);

		JobCenterEntity jobCenter2 = new JobCenterEntity(0, "", "gg", "gg@gmail.com");

		given(jobCenterRepository.findByUsername("gg")).willReturn(jobCenter2);

		mvc.perform(MockMvcRequestBuilders.put("/api/centers/gg").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.header("X-User-Header", "gg").content(jsonObject.toString()))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	public void test36_updateJobCenter() throws Exception {
		JSONObject jsonObject = new JSONObject().put("username", "different").put("name", "gigroup")
				.put("email", "gg@gmail.com").put("id", 0);

		JobCenterEntity jobCenter2 = new JobCenterEntity(0, "different", "gg", "gg@gmail.com");

		given(jobCenterRepository.findByUsername("gg")).willReturn(jobCenter2);

		mvc.perform(MockMvcRequestBuilders.put("/api/centers/gg").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.header("X-User-Header", "gg").content(jsonObject.toString()))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	public void test37_updateJobCenter() throws Exception {
		JSONObject jsonObject = new JSONObject().put("username", "different2").put("name", "gigroup")
				.put("email", "gg@gmail.com").put("id", 0);

		JobCenterEntity jobCenter2 = new JobCenterEntity(0, "different2", "gg", "gg@gmail.com");

		given(jobCenterRepository.findByUsername("gg")).willReturn(jobCenter2);

		mvc.perform(MockMvcRequestBuilders.put("/api/centers/gg").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.header("X-User-Header", "gg").content(jsonObject.toString()))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test(expected = NestedServletException.class)
	public void test39_updateJobCenter() throws Exception {
		JSONObject jsonObject = new JSONObject().put("username", "gg").put("name", "").put("email", "gg@gmail.com")
				.put("id", 0);

		JobCenterEntity jobCenter2 = new JobCenterEntity(0, "", "gg", "gg@gmail.com");

		given(jobCenterRepository.findByUsername("gg")).willReturn(jobCenter2);
		given(jobCenterRepository.save(jobCenter2)).willThrow(ConstraintViolationException.class);

		mvc.perform(MockMvcRequestBuilders.put("/api/centers/gg").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.header("X-User-Header", "gg").content(jsonObject.toString()))
				.andExpect(MockMvcResultMatchers.status().isPartialContent());
	}

	@Test(expected = NestedServletException.class)
	public void test41_updateJobCenter() throws Exception {
		JSONObject jsonObject = new JSONObject().put("username", "gg").put("name", "Gigroup").put("email", "").put("id",
				0);

		JobCenterEntity jobCenter2 = new JobCenterEntity(0, "Gigroup", "gg", "");

		given(jobCenterRepository.findByUsername("gg")).willReturn(jobCenter2);
		given(jobCenterRepository.save(jobCenter2)).willThrow(ConstraintViolationException.class);

		mvc.perform(MockMvcRequestBuilders.put("/api/centers/gg").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.header("X-User-Header", "gg").content(jsonObject.toString()))
				.andExpect(MockMvcResultMatchers.status().isPartialContent());
	}

	@Test(expected = NestedServletException.class)
	public void test42_updateJobCenter() throws Exception {
		JSONObject jsonObject = new JSONObject().put("username", "gg").put("name", "Gigroup").put("email", "aaa")
				.put("id", 0);

		JobCenterEntity jobCenter2 = new JobCenterEntity(0, "Gigroup", "gg", "aaa");

		given(jobCenterRepository.findByUsername("gg")).willReturn(jobCenter2);
		given(jobCenterRepository.save(jobCenter2)).willThrow(ConstraintViolationException.class);

		mvc.perform(MockMvcRequestBuilders.put("/api/centers/gg").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.header("X-User-Header", "gg").content(jsonObject.toString()))
				.andExpect(MockMvcResultMatchers.status().isPartialContent());
	}

	@Test
	public void test43_updateJobCenter() throws Exception {
		JSONObject jsonObject = new JSONObject().put("username", "gg").put("name", "Gigroup")
				.put("email", "gg@gmail.com").put("id", 0);

		JobCenterEntity jobCenter2 = new JobCenterEntity(0, "Gigroup", "gg", "aaa");

		given(jobCenterRepository.findByUsername("gg")).willReturn(jobCenter2);
		given(jobCenterRepository.save(jobCenter2)).willReturn(jobCenter2);

		mvc.perform(MockMvcRequestBuilders.put("/api/centers/gg").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.header("X-User-Header", "gg").content(jsonObject.toString()))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

}
