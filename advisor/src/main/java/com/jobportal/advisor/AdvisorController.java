package com.jobportal.advisor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdvisorController {

	@Autowired
	private JobsRepository jobsRepository;

	@Autowired
	private SeekerProxy seekerProxy;
	
	@RequestMapping(value = "api/seekers/{username}/suggestions", method = RequestMethod.GET)
	public ResponseEntity<List<JobEntity>> getAllJobs(@RequestHeader("X-User-Header") String loggedUser, 
														@PathVariable String username) {
		if (!username.equals(loggedUser))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		SeekerEntity user = seekerProxy.getSeeker(loggedUser, username);
		if (user == null)
			return ResponseEntity.notFound().build();
		
		return ResponseEntity.ok(jobsRepository.findAllByLocation(user.getCity()));
	}
}