package com.jobportal.applications;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import feign.FeignException;

@RestController
public class ApplicationsController {

	@Autowired
	private JobsProxy jobEntityProxy;

	@Autowired
	private ApplicationsRepository applicationsRepository;

	@Autowired
	private JobCenterProxy jobCenterProxy;

	@Autowired
	private SeekerProxy seekerProxy;

	@Autowired
	private NotificationProxy notificationProxy;
	
	@Value("${pass}")
	private String pass;

	/**
	 * This method is used to get all applications of a specific center
	 *
	 * @param loggedUser
	 * @param username
	 * @return 401 if user is not logged, 401 if user is not a Center, 200 and center's applications
	 *         
	 */
	@RequestMapping(value = "/api/centers/{username}/applications/", method = RequestMethod.GET)
	public ResponseEntity<List<ApplicationEntity>> getCentersApplications(
			@RequestHeader("X-User-Header") String loggedUser, @PathVariable String username) {
		if (!username.equals(loggedUser)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		// MUST BE CENTER
		ResponseEntity<JobCenterEntity> response = jobCenterProxy.getJobCenter(pass, username);
		if (!(response.getStatusCodeValue() == 200)) {
			ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		return ResponseEntity.ok().body(applicationsRepository.findAllByCenterUsername(username));
	}
	
	/**
	 * This method is used to get all applications of a specific center and a specific job
	 *
	 * @param loggedUser
	 * @param username
	 * @param jobId
	 * @return 401 if user is not logged, 401 if user is not a Center, 200 and center's applications of a specific job
	 *         
	 */
	@RequestMapping(value = "/api/centers/{username}/jobs/{jobId}/applications", method = RequestMethod.GET)
	public ResponseEntity<List<ApplicationEntity>> getCentersApplications(
			@RequestHeader("X-User-Header") String loggedUser, @PathVariable String username,
			@PathVariable long jobId) {
		if (!username.equals(loggedUser)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		// MUST BE CENTER
		ResponseEntity<JobCenterEntity> response = jobCenterProxy.getJobCenter(pass, username);
		if (!(response.getStatusCodeValue() == 200)) {
			ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		return ResponseEntity.ok().body(applicationsRepository.findAllByJobId(jobId));
	}

	/**
	 * This method is used to get all applications of a specific seeker
	 *
	 * @param loggedUser
	 * @param username
	 * @return 401 if user is not logged, 401 if user is not a Seeker, 200 and Seeker's applications
	 *         
	 */
	@RequestMapping(value = "/api/seekers/{username}/applications/", method = RequestMethod.GET)
	public ResponseEntity<List<ApplicationEntity>> getApplications(@RequestHeader("X-User-Header") String loggedUser,
			@PathVariable String username) {
		if (!username.equals(loggedUser)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		
		//MUST BE SEEKER
		ResponseEntity<SeekerEntity> response = seekerProxy.getJobSeeker(pass, username);
		if (!(response.getStatusCodeValue() == 200)) {
			ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		return ResponseEntity.ok().body(applicationsRepository.findAllByUsername(username));
	}
	
	/**
	 * This method is used to create an application
	 *
	 * @param loggedUser
	 * @param role
	 * @param username
	 * @param application
	 * @return 401 if user is not logged, 401 if user is not a Seeker, 404 if job doesn't exist, 200 and Seeker's applications
	 *         
	 */
	@RequestMapping(value = "/api/seekers/{username}/applications/", method = RequestMethod.POST)
	public ResponseEntity<ApplicationEntity> createApplication(@RequestHeader("X-User-Header") String loggedUser,
			@RequestHeader("X-User-Role-Header") String role, @PathVariable String username,
			@RequestBody ApplicationEntity application) throws URISyntaxException {
		if (!username.equals(loggedUser)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		JobEntity relatedJob;
		if (!"SEEKER".equals(role)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		try {
			relatedJob = jobEntityProxy.getJob(loggedUser, application.getUsername(), application.getJobId());
		} catch (FeignException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		if((applicationsRepository.findByUsernameAndJobId(application.getUsername(), application.getJobId())) == null) {
			application.setDateCreation(new Date());
			application.setUsername(username);
			application.setJobId(application.getJobId());
			application.setCenterUsername(relatedJob.getUsername());
			ApplicationEntity a = applicationsRepository.save(application);
			ResponseEntity<JobCenterEntity> response = jobCenterProxy.getJobCenter(pass, relatedJob.getUsername());
			String receiverEmail = "";
			if (response.getStatusCodeValue() == 200)
				receiverEmail = response.getBody().getEmail();
			notificationProxy.sendNotification(new NotificationEntity(receiverEmail, "Seeker applied to your job ",
					"Seeker " + username + " Applied to your job " + relatedJob.getJobDescription(), username));
			return ResponseEntity.created(new URI("/api/seekers/" + username + "/applications/" + a.getId())).body(a);
		}
		else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

	
	/**
	 * This method is used to get a specific application
	 * 
	 * @param loggedUser
	 * @param username
	 * @param applicationId
	 * @return 401 if user is not logged, 404 if application or user don't exist, 200 and application
	 *         
	 */
	@RequestMapping(value = "/api/seekers/{username}/applications/{applicationId}", method = RequestMethod.GET)
	public ResponseEntity<ApplicationEntity> getApplication(@RequestHeader("X-User-Header") String loggedUser,
			@PathVariable String username, @PathVariable long applicationId) {
		if (!username.equals(loggedUser)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		Optional<ApplicationEntity> applicationOpt = applicationsRepository.findById(applicationId);
		if (!applicationOpt.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		ApplicationEntity application = applicationOpt.get();
		if (username.equals(application.getUsername())) {
			return ResponseEntity.ok(application);
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	/**
	 * This method is used to delete all applications by username
	 * 
	 * @param loggedUser
	 * @param username
	 * @return 401 if user is not logged, 404 if application or user don't exist, 200
	 *         
	 */
	@RequestMapping(value = "/api/seekers/{username}/applications", method = RequestMethod.DELETE)
	@Transactional
	public ResponseEntity deleteAllApplicationsByUsername(@RequestHeader("X-User-Header") String loggedUser,
			@PathVariable String username) {
		if (!username.equals(loggedUser))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

		applicationsRepository.deleteAllByUsername(username);
		return ResponseEntity.ok().build();
	}

	/**
	 * This method is used to delete a specific application by id
	 * 
	 * @param loggedUser
	 * @param username
	 * @param jobId
	 * @return 401 if user is not logged, 404 if application doesn't exist and 200 if everything is OK
	 *         
	 */
	@RequestMapping(value = "/api/centers/{username}/jobs/{jobId}/applications", method = RequestMethod.DELETE)
	@Transactional
	public ResponseEntity deleteAllApplicationsByJobId(@RequestHeader("X-User-Header") String loggedUser,
			@PathVariable String username, @PathVariable long jobId) {
		if (!username.equals(loggedUser))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

		applicationsRepository.deleteAllByJobId(jobId);
		return ResponseEntity.ok().build();
	}
	
	/**
	 * This method is used to delete a specific application by id
	 * 
	 * @param loggedUser
	 * @param username
	 * @param applicationId
	 * @return 401 if user is not logged or user isn't subscribed, 404 if application doesn't exist and 200 if everything is OK
	 *         
	 */
	@RequestMapping(value = "/api/seekers/{username}/applications/{applicationId}", method = RequestMethod.DELETE)
	public ResponseEntity<ApplicationEntity> deleteApplication(@RequestHeader("X-User-Header") String loggedUser,
			@PathVariable String username, @PathVariable long applicationId) {
		if (!username.equals(loggedUser)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		Optional<ApplicationEntity> applicationOpt = applicationsRepository.findById(applicationId);
		if (!applicationOpt.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		ApplicationEntity application = applicationOpt.get();
		if (!username.equals(application.getUsername())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		applicationsRepository.deleteById(applicationId);
		return ResponseEntity.ok().build();
	}

	/**
	 * This method is used to update an application by id
	 * 
	 * @param loggedUser
	 * @param role
	 * @param username
	 * @param applicationId
	 * @param application
	 * @return 401 if user is not logged or user isn't a seeker, 404 if application doesn't exist and 200 if everything is OK
	 *         
	 */
	@RequestMapping(value = "/api/seekers/{username}/applications/{applicationId}", method = RequestMethod.PUT)
	public ResponseEntity<ApplicationEntity> updateApplication(@RequestHeader("X-User-Header") String loggedUser,
			@RequestHeader("X-User-Role-Header") String role, @PathVariable String username,
			@PathVariable long applicationId, @RequestBody ApplicationEntity application) {
		if (!username.equals(loggedUser)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		if (!"SEEKER".equals(role)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		if (application.getId() != applicationId || !username.equals(application.getUsername())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}

		JobEntity relatedJob = jobEntityProxy.getJob(loggedUser, application.getUsername(), application.getJobId());
		Date dateCreation = applicationsRepository.findById(applicationId).get().getDateCreation();
		application.setDateCreation(dateCreation);
		applicationsRepository.save(application);
		return ResponseEntity.ok(application);
	}
}