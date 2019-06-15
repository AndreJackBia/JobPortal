package com.jobportal.search;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@WebMvcTest(SearchController.class)
public class SearchControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private JobsRepository jobsRepository;
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}
	
	
	/**
	 * TEST 3
	 * 
	 * Get job with query null and location empty
	 * 
	 */
	@Test(expected = IllegalArgumentException.class)
	public void test3_getJobs_QueryNull_LocationNonEmpty() throws Exception {
		JobEntity job0 = new JobEntity(0, "Mater", "test position", "test description", "Pavia", new Date(2019, 5, 26, 0, 0, 0), "Stregatto company", new ArrayList<String>());
		JobEntity job1 = new JobEntity(1, "Albert", "test2 position", "test2 description", "Milano", new Date(2018, 5, 26, 0, 0, 0), "Stregatto company", new ArrayList<String>());

		List<JobEntity> correctJob = Arrays.asList(job1);
		
		given(jobsRepository.findJobByQuery(null, "Milano")).willThrow(IllegalArgumentException.class);
		
		mvc.perform(MockMvcRequestBuilders.get("/api/jobs/search")
				.contentType(MediaType.APPLICATION_JSON)
				.param("q", null)
				.param("location", "Milano"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.forwardedUrl(null));
	}
	
	/**
	 * TEST 4
	 * 
	 * Get job with query empty and location not empty
	 * 
	 */
	@Test
	public void test4_getJobs_QueryEmpty_LocationNotEmpty() throws Exception {
		JobEntity job0 = new JobEntity(0, "Albert1", "test position", "test description", "Milano", new Date(2019, 5, 26, 0, 0, 0), "Stregatto company", new ArrayList<String>());
		JobEntity job1 = new JobEntity(0, "Albert2", "test position", "test description", "Pavia", new Date(2019, 5, 26, 0, 0, 0), "Stregatto company", new ArrayList<String>());

		List<JobEntity> correctJob = Arrays.asList(job0);
		
		given(jobsRepository.findJobByQuery("", "Milano")).willReturn(correctJob);
		
		
		mvc.perform(MockMvcRequestBuilders.get("/api/jobs/search")
				.contentType(MediaType.APPLICATION_JSON)
				.param("q", "")
				.param("location", "Milano"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$[0].location", is(job0.getLocation())))
				.andExpect(jsonPath("$", Matchers.hasSize(1)));

	}
	
	/**
	 * TEST 5
	 * 
	 * Get job with query not empty and location empty
	 * 
	 */
	@Test
	public void test5_getJobs_QueryNotEmpty_LocationEmpty() throws Exception {
		JobEntity job0 = new JobEntity(0, "Giovanni", "test position", "test description", "Milano", new Date(2019, 5, 26, 0, 0, 0), "Stregatto company", new ArrayList<String>());
		JobEntity job1 = new JobEntity(1, "Luca", "test position", "test description", "Milano", new Date(2019, 5, 26, 0, 0, 0), "Stregatto company", new ArrayList<String>());
		JobEntity job2 = new JobEntity(2, "Alberto", "test2 position", "test2 description", "Pavia", new Date(2018, 5, 26, 0, 0, 0), "Stregatto company", new ArrayList<String>());


		List<JobEntity> correctJob = Arrays.asList(job2);
		
		given(jobsRepository.findJobByQuery("Alberto", "")).willReturn(correctJob);
		
		
		mvc.perform(MockMvcRequestBuilders.get("/api/jobs/search")
				.contentType(MediaType.APPLICATION_JSON)
				.param("q", "Alberto")
				.param("location", ""))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$[0].username", is(job2.getUsername())))
				.andExpect(jsonPath("$", Matchers.hasSize(1)));
	}
	
	/**
	 * TEST 6
	 * 
	 * Get job with query not empty and location null
	 * 
	 */
	@Test(expected = IllegalArgumentException.class)
	public void test6_getJobs_QueryNotEmpty_LocationNull() throws Exception {
		JobEntity job0 = new JobEntity(0, "Mater", "test position", "test description", "Pavia", new Date(2019, 5, 26, 0, 0, 0), "Stregatto company", new ArrayList<String>());
		JobEntity job1 = new JobEntity(1, "Albert", "test2 position", "test2 description", "Milano", new Date(2018, 5, 26, 0, 0, 0), "Stregatto company", new ArrayList<String>());

		List<JobEntity> correctJob = Arrays.asList(job1);
		
		given(jobsRepository.findJobByQuery("query", null)).willThrow(IllegalArgumentException.class);
		
		mvc.perform(MockMvcRequestBuilders.get("/api/jobs/search")
				.contentType(MediaType.APPLICATION_JSON)
				.param("q", "query")
				.param("location", null))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.forwardedUrl(null));
	}
	
	/**
	 * TEST 7
	 * 
	 * No valid match for jobs with query not empty and location not empty
	 * 
	 */
	@Test
	public void test7_getJobs_QueryNotEmpty_LocationNotEmpty_NoResults() throws Exception {
		JobEntity job0 = new JobEntity(0, "Mater", "test position", "test description", "Pavia", new Date(2019, 5, 26, 0, 0, 0), "Stregatto company", new ArrayList<String>());
		JobEntity job1 = new JobEntity(1, "Albert", "test2 position", "test2 description", "Milano", new Date(2018, 5, 26, 0, 0, 0), "Stregatto company", new ArrayList<String>());
		
		given(jobsRepository.findJobByQuery("query", "Palermo")).willReturn(new ArrayList());
		
		mvc.perform(MockMvcRequestBuilders.get("/api/jobs/search")
				.contentType(MediaType.APPLICATION_JSON)
				.param("q", "query")
				.param("location", "Palermo"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(0)));
	}
	
	/**
	 * TEST 8
	 * 
	 * Get job with query not empty and location not empty
	 * 
	 */
	@Test
	public void test8_getJobs_QueryNotEmpty_LocationNotEmpty_OneResult() throws Exception {
		JobEntity job0 = new JobEntity(0, "Mater", "test position", "test description", "Pavia", new Date(2019, 5, 26, 0, 0, 0), "Stregatto company", new ArrayList<String>());
		JobEntity job1 = new JobEntity(1, "Albert", "test2 position", "test2 description", "Milano", new Date(2018, 5, 26, 0, 0, 0), "Stregatto company", new ArrayList<String>());

		List<JobEntity> correctJob = Arrays.asList(job1);
		
		given(jobsRepository.findJobByQuery("", "Milano")).willReturn(correctJob);
		
		mvc.perform(MockMvcRequestBuilders.get("/api/jobs/search")
				.contentType(MediaType.APPLICATION_JSON)
				.param("q", "")
				.param("location", "Milano"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(1)))
				.andExpect(jsonPath("$[0].location", is(job1.getLocation())));
	}
	
	/**
	 * TEST 9
	 * 
	 * Get more than one jobs with query not empty and location not empty
	 * 
	 */
	@Test
	public void test9_getJobs_QueryNotEmpty_LocationNotEmpty_MoreThanOne() throws Exception {
		JobEntity job0 = new JobEntity(0, "Lorenzo", "test position", "test description", "Milano", new Date(2019, 5, 26, 0, 0, 0), "Stregatto company", new ArrayList<String>());
		JobEntity job1 = new JobEntity(1, "Luca2", "test2 position", "test description", "Pavia", new Date(2019, 5, 26, 0, 0, 0), "Stregatto company", new ArrayList<String>());
		JobEntity job2 = new JobEntity(2, "Andrea", "test position", "test2 description", "Milano", new Date(2018, 5, 26, 0, 0, 0), "Stregatto company", new ArrayList<String>());

		List<JobEntity> correctJob = Arrays.asList(job0, job2);
		
		given(jobsRepository.findJobByQuery("test position", "Milano")).willReturn(correctJob);
		
		
		mvc.perform(MockMvcRequestBuilders.get("/api/jobs/search")
				.contentType(MediaType.APPLICATION_JSON)
				.param("q", "test position")
				.param("location", "Milano"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$[0].username", is(job0.getUsername())))
				.andExpect(jsonPath("$[1].username", is(job2.getUsername())))
				.andExpect(jsonPath("$", Matchers.hasSize(2)));
	}
	
	
	
	
}
