package com.jobportal.jobs;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
@WebMvcTest(JobsController.class)
public class JobsControllerTest {	

	@Autowired
	private MockMvc mvc;

	@MockBean
	private JobsRepository jobsRepository;

	@MockBean
	ApplicationsProxy applicationsProxy;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetJobsByUsername() throws Exception {
		JobEntity job0 = new JobEntity(0, "Albert", "test position", "test description", "Milano", new Date(2019, 5, 26, 0, 0, 0), "Stregatto company");
		JobEntity job1 = new JobEntity(1, "Albert", "test2 position", "test2 description", "Milano", new Date(2018, 5, 26, 0, 0, 0), "Stregatto company");

		List<JobEntity> allJobs = Arrays.asList(job0, job1);

		given(jobsRepository.findAllByUsername("Albert")).willReturn(allJobs);

		mvc.perform(MockMvcRequestBuilders.get("/api/centers/Albert/jobs/")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(2)))
				.andExpect(jsonPath("$[0].location", is(job0.getLocation())));
	}
}