package com.jobportal.jobcenters;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobCentersController {

	@Autowired
	private JobCentersRepository jobCenterRepository;
	
	@Autowired
	private JobsProxy jobsProxy;
	
	@Value("${pass}")
	private String pass;

	/**
	 *
	 * This method is used to get the list of all the job centers registered to the portal
	 *
	 * @return The list of all the job centers
	 */
	@RequestMapping(value = "/api/centers", method = RequestMethod.GET)
	public List<JobCenterEntity> getJobCenters() {
		return jobCenterRepository.findAll();
	}

	/**
	 * 
	 * This method is used to register a new job center to the portal
	 * 
	 * @param jobCenterEntity
	 * @return 201 if successful, 500 otherwise
	 * @throws URISyntaxException
	 */
	@RequestMapping(value = "/api/centers", method = RequestMethod.POST)
	public ResponseEntity<JobCenterEntity> createJobCenter(@RequestBody JobCenterEntity jobCenterEntity)
			throws URISyntaxException {
		jobCenterRepository.save(jobCenterEntity);
		return ResponseEntity.created(new URI("/api/centers" + jobCenterEntity.getId())).body(jobCenterEntity);
	}

	/**
	 * This method is used to get the details of a job center given its username
	 * @param username
	 * @return 404 if the username is not valid, 200 and the data otherwise
	 * 
	 */
	@RequestMapping(value = "/api/centers/{username}", method = RequestMethod.GET)
	public ResponseEntity<JobCenterEntity> getJobCenter(@PathVariable String username) {
		
		JobCenterEntity jobCenterEntity = jobCenterRepository.findByUsername(username);
		if (jobCenterEntity != null) {
			return ResponseEntity.ok().body(jobCenterEntity);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	/**
	 * 
	 * This method is used to delete a job center. This method propagates the deletion to
	 * the jobs and the applications linked to this account
	 * 
	 * @param loggedUser
	 * @param username
	 * @return 401 if not authorized, 404 if username doesn't exist, 200 otherwise
	 */
	@RequestMapping(value = "/api/centers/{username}", method = RequestMethod.DELETE)
	public ResponseEntity<JobCenterEntity> deleteJobCenter(@RequestHeader("X-User-Header") String loggedUser,
														   @PathVariable String username) {
		if (!username.equals(loggedUser)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		JobCenterEntity jobCenter = jobCenterRepository.findByUsername(username);
		if (jobCenter == null) {
			return ResponseEntity.notFound().build();
		}
		jobsProxy.deleteAllByUsername(loggedUser, username);
		jobCenterRepository.delete(jobCenter);
		return ResponseEntity.ok().build();
	}

	/**
	 * This method is used to edit the details of a job center given its username
	 * 
	 * @param loggedUser
	 * @param username
	 * @param jobCenter
	 * @return 4xx if not authorized, 206 if some data are missing, 200 and the new data otherwise
	 */
	@RequestMapping(value = "/api/centers/{username}", method = RequestMethod.PUT)
	public ResponseEntity<JobCenterEntity> updateJobCenter(@RequestHeader("X-User-Header") String loggedUser,
														   @PathVariable String username,
														   @RequestBody JobCenterEntity jobCenter) {
		if (!username.equals(loggedUser)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		JobCenterEntity jobCenterOld = jobCenterRepository.findByUsername(username);
		if (jobCenterOld == null) {
			return ResponseEntity.notFound().build();
		}
		
		if (!username.equals(jobCenter.getUsername())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		if(!checkField(jobCenter)) {
			return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).build();
		}
	
		jobCenterOld.setName(jobCenter.getName());
		jobCenterOld.setEmail(jobCenter.getEmail());
		jobCenterRepository.save(jobCenterOld);
		return ResponseEntity.ok(jobCenterOld);
	}
	
	
	private boolean checkField(JobCenterEntity jobCenterEntity) {
		if(jobCenterEntity.getUsername() == null) {
			return false;
		}
		if(jobCenterEntity.getEmail() == null) {
			return false;
		}
		if(jobCenterEntity.getName() == null) {
			return false;
		}
		return true;
	}
	
}
