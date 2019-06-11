package com.jobportal.search;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
	
	@Test
	public void test3_getJobs_QueryNull_LocationEmpty() throws Exception {
		JobEntity job0 = new JobEntity(0, "Albert1", "test position", "test description", "Milano", new Date(2019, 5, 26, 0, 0, 0), "Stregatto company", new ArrayList<String>());
		JobEntity job1 = new JobEntity(1, "Albert", "test2 position", "test2 description", "Milano", new Date(2018, 5, 26, 0, 0, 0), "Stregatto company", new ArrayList<String>());

		List<JobEntity> correctJob = Arrays.asList(job0, job1);
		
		given(jobsRepository.findJobByQuery("", "")).willReturn(correctJob);
		
		mvc.perform(MockMvcRequestBuilders.get("/api/jobs/search")
				.contentType(MediaType.APPLICATION_JSON)
				.param("location", ""))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$[0].username", is(job0.getUsername())))
				.andExpect(jsonPath("$[1].username", is(job1.getUsername())));
	}
	
	@Test
	public void test4_getJobs_QueryEmpty_LocationNotEmpty() throws Exception {
		JobEntity job0 = new JobEntity(0, "Albert1", "test position", "test description", "Milano", new Date(2019, 5, 26, 0, 0, 0), "Stregatto company", new ArrayList<String>());
		JobEntity job1 = new JobEntity(0, "Albert2", "test position", "test description", "Milano", new Date(2019, 5, 26, 0, 0, 0), "Stregatto company", new ArrayList<String>());
		JobEntity job2 = new JobEntity(1, "Albert", "test2 position", "test2 description", "Pavia", new Date(2018, 5, 26, 0, 0, 0), "Stregatto company", new ArrayList<String>());

		List<JobEntity> correctJob = Arrays.asList(job0, job1);
		
		given(jobsRepository.findJobByQuery("", "Milano")).willReturn(correctJob);
		
		
		mvc.perform(MockMvcRequestBuilders.get("/api/jobs/search")
				.contentType(MediaType.APPLICATION_JSON)
				.param("q", "")
				.param("location", "Milano"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$[0].location", is(job0.getLocation())))
				.andExpect(jsonPath("$[1].location", is(job1.getLocation())));
	}
	
	@Test
	public void test5_getJobs_QueryNotEmpty_LocationEmpty() throws Exception {
		JobEntity job0 = new JobEntity(0, "Albert1", "test position", "test description", "Milano", new Date(2019, 5, 26, 0, 0, 0), "Stregatto company", new ArrayList<String>());
		JobEntity job1 = new JobEntity(0, "Luca2", "test position", "test description", "Milano", new Date(2019, 5, 26, 0, 0, 0), "Stregatto company", new ArrayList<String>());
		JobEntity job2 = new JobEntity(1, "Albert", "test2 position", "test2 description", "Pavia", new Date(2018, 5, 26, 0, 0, 0), "Stregatto company", new ArrayList<String>());

		List<JobEntity> correctJob = Arrays.asList(job0, job2);
		
		given(jobsRepository.findJobByQuery("Albert", "")).willReturn(correctJob);
		
		
		mvc.perform(MockMvcRequestBuilders.get("/api/jobs/search")
				.contentType(MediaType.APPLICATION_JSON)
				.param("q", "Albert")
				.param("location", ""))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$[0].username", is(job0.getUsername())))
				.andExpect(jsonPath("$[1].username", is(job2.getUsername())));
	}
	
	@Test
	public void test6_getJobs_QueryNotEmpty_LocationNull() throws Exception {
		JobEntity job0 = new JobEntity(0, "Albert1", "test position", "test description", "Milano", new Date(2019, 5, 26, 0, 0, 0), "Stregatto company", new ArrayList<String>());
		JobEntity job1 = new JobEntity(0, "Luca2", "test position", "test description", "Milano", new Date(2019, 5, 26, 0, 0, 0), "Stregatto company", new ArrayList<String>());
		JobEntity job2 = new JobEntity(1, "Albert", "test2 position", "test2 description", "Pavia", new Date(2018, 5, 26, 0, 0, 0), "Stregatto company", new ArrayList<String>());

		List<JobEntity> correctJob = Arrays.asList(job1);
		
		given(jobsRepository.findJobByQuery("Luca2", "")).willReturn(correctJob);
		
		
		mvc.perform(MockMvcRequestBuilders.get("/api/jobs/search")
				.contentType(MediaType.APPLICATION_JSON)
				.param("q", "Luca2"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$[0].username", is(job1.getUsername())));
	}
	
	@Test
	public void test7_getJobs_QueryNotEmpty_LocationNull() throws Exception {
		JobEntity job0 = new JobEntity(0, "Albert1", "test position", "test description", "Milano", new Date(2019, 5, 26, 0, 0, 0), "Stregatto company", new ArrayList<String>());
		JobEntity job1 = new JobEntity(0, "Luca2", "test position", "test description", "Pavia", new Date(2019, 5, 26, 0, 0, 0), "Stregatto company", new ArrayList<String>());
		JobEntity job2 = new JobEntity(1, "Albert", "test2 position", "test2 description", "Milano", new Date(2018, 5, 26, 0, 0, 0), "Stregatto company", new ArrayList<String>());

		List<JobEntity> correctJob = Arrays.asList(job0, job2);
		
		given(jobsRepository.findJobByQuery("Albert", "Milano")).willReturn(correctJob);
		
		
		mvc.perform(MockMvcRequestBuilders.get("/api/jobs/search")
				.contentType(MediaType.APPLICATION_JSON)
				.param("q", "Albert")
				.param("location", "Milano"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$[0].username", is(job0.getUsername())))
				.andExpect(jsonPath("$[1].username", is(job2.getUsername())));
	}
	
	
	
}
