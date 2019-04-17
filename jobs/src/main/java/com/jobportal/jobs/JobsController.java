package com.jobportal.jobs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class JobsController {

    @Autowired
    JobsRepository jobRepository;

    @RequestMapping(value = "/api/centers/{username}/jobs/", method = RequestMethod.GET)
    public ResponseEntity<List<JobEntity>> getJobs(@PathVariable String username) {
    	List<JobEntity> jobs = jobRepository.findAllByUsername(username);
    	if (jobs == null)
    		return ResponseEntity.notFound().build();
        return ResponseEntity.ok(jobs);
    }

    @RequestMapping(value = "/api/centers/{username}/jobs", method = RequestMethod.POST)
    public ResponseEntity<JobEntity> createJob(@PathVariable String username,
    											@RequestBody JobEntity job) throws URISyntaxException {
    	//TODO Auth needed!
        job.setDateCreation(new Date());
        job.setUsername(username);
        JobEntity j = jobRepository.save(job);
        return ResponseEntity.created(new URI("/api/centers/" + username + "/jobs/" + j.getId())).body(j);
    }

    @RequestMapping(value = "/api/centers/{username}/jobs/{jobId}", method = RequestMethod.GET)
    public ResponseEntity<JobEntity> getJob(@PathVariable long jobId) {

        Optional<JobEntity> jobOpt = jobRepository.findById(jobId);
        if (!jobOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        JobEntity job = jobOpt.get();
        if(job != null) {
        	return ResponseEntity.ok(job);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
