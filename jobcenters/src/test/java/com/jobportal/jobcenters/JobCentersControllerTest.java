package com.jobportal.jobcenters;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testCreateJobCenter() throws Exception {
			
		JSONObject jsonObject = new JSONObject()
                .put("username", "luca")
                .put("name", "lorus")
                .put("email", "luca@luca.it");
		
		mvc.perform(MockMvcRequestBuilders.post("/api/centers")
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(jsonObject.toString()))
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
				.andExpect(jsonPath("$.username", is(jsonObject.get("username"))));	
	}
	
	@Test
	public void test3_getExistingJobCenterByUsername() throws Exception {
		JobCenterEntity jobCenter0 = new JobCenterEntity(0, "Adecco", "adec", "adecco.adec@gmail.com");
		
		given(jobCenterRepository.findByUsername("adec")).willReturn(jobCenter0);
		
		mvc.perform(MockMvcRequestBuilders.get("/api/centers/adec")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("username", is(jobCenter0.getUsername())));
				
	}
	
	@Test
	public void test4_getNotExistingJobCenterByUsername() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/api/centers/adec")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}
}
