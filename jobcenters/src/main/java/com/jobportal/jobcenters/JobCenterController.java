package com.jobportal.jobcenters;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobCenterController {

	@Autowired
	private JobCenterRepository jobCenterRepository;
	
	@RequestMapping(value = "/api/centers", method = RequestMethod.GET)
	public List<JobCenterEntity> getJobCenters() {
		return jobCenterRepository.findAll();
	}
	
	@RequestMapping(value = "/api/centers", method = RequestMethod.POST)
	public ResponseEntity<JobCenterEntity> createJobCenter(@RequestBody JobCenterEntity jobCenterEntity)
			throws URISyntaxException {
		jobCenterRepository.save(jobCenterEntity);
		return ResponseEntity.created(new URI("/api/centers" + jobCenterEntity.getId())).body(jobCenterEntity);
	}
	
}
