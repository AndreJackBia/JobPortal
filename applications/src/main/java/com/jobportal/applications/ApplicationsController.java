package com.jobportal.applications;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jobportal.applications.UserEntity.Role;

import feign.FeignException;

@RestController
public class ApplicationsController {

	@Autowired
	private JobEntityProxy jobEntityProxy;

	@Autowired
	private ApplicationsRepository applicationsRepository;

	@Autowired
	private UserEntityProxy userEntityProxy;

	@Autowired
	private NotificationEntityProxy notificationEntityProxy;

	@RequestMapping(value = "/api/centers/{username}/applications/", method = RequestMethod.GET)
	public ResponseEntity<List<ApplicationsEntity>> getCentersApplications(
			@RequestHeader("X-User-Header") String loggedUser, @PathVariable String username) {
		if (!username.equals(loggedUser)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		UserEntity user = userEntityProxy.getUser(username);
		if (!Role.JOB_CENTER.equals(user.getRole())) {
			ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		return ResponseEntity.ok().body(applicationsRepository.findAllByCenterUsername(username));
	}

	@RequestMapping(value = "/api/centers/{username}/jobs/{jobId}/applications", method = RequestMethod.GET)
	public ResponseEntity<List<ApplicationsEntity>> getCentersApplications(@RequestHeader("X-User-Header") String loggedUser,
	                                                                       @PathVariable String username,
	                                                                       @PathVariable long jobId) {
	    if (!username.equals(loggedUser)) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	    }

	    UserEntity user = userEntityProxy.getUser(username);
	    if (!Role.JOB_CENTER.equals(user.getRole())) {
	        ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	    }

	    return ResponseEntity.ok().body(applicationsRepository.findAllByJobId(jobId));
	}

	@RequestMapping(value = "/api/seekers/{username}/applications/", method = RequestMethod.GET)
	public ResponseEntity<List<ApplicationsEntity>> getApplications(@RequestHeader("X-User-Header") String loggedUser,
			@PathVariable String username) {
		if (!username.equals(loggedUser)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		UserEntity user = userEntityProxy.getUser(username);
		if (!Role.SEEKER.equals(user.getRole())) {
			ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		return ResponseEntity.ok().body(applicationsRepository.findAllByUsername(username));
	}

	@RequestMapping(value = "/api/seekers/{username}/applications/", method = RequestMethod.POST)
	public ResponseEntity<ApplicationsEntity> createApplication(@RequestHeader("X-User-Header") String loggedUser,
			@RequestHeader("X-User-Role-Header") String role, @PathVariable String username,
			@RequestBody ApplicationsEntity application) throws URISyntaxException {
		if (!username.equals(loggedUser)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		JobEntityBean relatedJob;
		if (!"SEEKER".equals(role)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		try {
			relatedJob = jobEntityProxy.getJob(loggedUser, application.getUsername(), application.getJobId());
		} catch (FeignException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		application.setDateCreation(new Date());
		application.setUsername(username);
		application.setJobId(application.getJobId());
		application.setCenterUsername(relatedJob.getUsername());
		ApplicationsEntity a = applicationsRepository.save(application);
		String receiveremail = userEntityProxy.getEmail(relatedJob.getUsername());
		notificationEntityProxy.sendNotification(new NotificationEntity(receiveremail, "Seeker applied to your job ",
				"Seeker " + username + " Applied to your job " + relatedJob.getJobDescription(), username));
		return ResponseEntity.created(new URI("/api/seekers/" + username + "/applications/" + a.getId())).body(a);
	}

	@RequestMapping(value = "/api/seekers/{username}/applications/{applicationId}", method = RequestMethod.GET)
	public ResponseEntity<ApplicationsEntity> getApplication(@RequestHeader("X-User-Header") String loggedUser,
			@PathVariable String username, @PathVariable long applicationId) {
		if (!username.equals(loggedUser)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		Optional<ApplicationsEntity> applicationOpt = applicationsRepository.findById(applicationId);
		if (!applicationOpt.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		ApplicationsEntity application = applicationOpt.get();
		if (username.equals(application.getUsername())) {
			return ResponseEntity.ok(application);
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@RequestMapping(value = "/api/seekers/{username}/applications", method = RequestMethod.DELETE)
	@Transactional
	public ResponseEntity deleteAllApplicationsByUsername(@RequestHeader("X-User-Header") String loggedUser, @PathVariable String username) {
		if (!username.equals(loggedUser))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		
		applicationsRepository.deleteAllByUsername(username);
		return ResponseEntity.ok().build();
	}
	
	@RequestMapping(value = "/api/centers/{username}/jobs/{jobId}/applications", method = RequestMethod.DELETE)
	@Transactional
	public ResponseEntity deleteAllApplicationsByJobId(@RequestHeader("X-User-Header") String loggedUser, @PathVariable String username, @PathVariable long jobId) {
		if (!username.equals(loggedUser))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		
		applicationsRepository.deleteAllByJobId(jobId);
		return ResponseEntity.ok().build();
	}
	
	@RequestMapping(value = "/api/seekers/{username}/applications/{applicationId}", method = RequestMethod.DELETE)
	public ResponseEntity<ApplicationsEntity> deleteApplication(@RequestHeader("X-User-Header") String loggedUser,
			@PathVariable String username, @PathVariable long applicationId) {
		if (!username.equals(loggedUser)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		Optional<ApplicationsEntity> applicationOpt = applicationsRepository.findById(applicationId);
		if (!applicationOpt.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		ApplicationsEntity application = applicationOpt.get();
		if (!username.equals(application.getUsername())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		applicationsRepository.deleteById(applicationId);
		return ResponseEntity.ok().build();
	}

	@RequestMapping(value = "/api/seekers/{username}/applications/{applicationId}", method = RequestMethod.PUT)
	public ResponseEntity<ApplicationsEntity> updateApplication(@RequestHeader("X-User-Header") String loggedUser,
			@RequestHeader("X-User-Role-Header") String role, @PathVariable String username,
			@PathVariable long applicationId, @RequestBody ApplicationsEntity application) {
		if (!username.equals(loggedUser)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		if (!"SEEKER".equals(role)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		if (application.getId() != applicationId || !username.equals(application.getUsername())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}

		JobEntityBean relatedJob = jobEntityProxy.getJob(loggedUser, application.getUsername(), application.getJobId());
		Date dateCreation = applicationsRepository.findById(applicationId).get().getDateCreation();
		application.setDateCreation(dateCreation);
		applicationsRepository.save(application);
		return ResponseEntity.ok(application);
	}
}