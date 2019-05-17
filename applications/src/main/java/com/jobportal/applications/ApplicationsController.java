package com.jobportal.applications;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplicationsController {
	
	@Autowired
	private ApplicationsRepository applicationsRepository;
	
	@RequestMapping(value = "/api/seekers/{username}/applications",method=RequestMethod.GET)
	public ResponseEntity<List<ApplicationsEntity>> getApplicationsSeeker(@PathVariable String username){
		
		String usernameTemp = username;
		List<ApplicationsEntity> applications = applicationsRepository.findAllByUsername(usernameTemp);
		
		return ResponseEntity.ok().body(applications);
	}
	
	@RequestMapping(value = "/api/seekers/{username}/applications", method = RequestMethod.DELETE)
	@Transactional
	public ResponseEntity deleteAllApplicationsByUsername(@RequestHeader("X-User-Header") String loggedUser, @PathVariable String username) {
		if (!username.equals(loggedUser))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		
		applicationsRepository.deleteAllByUsername(username);
		return ResponseEntity.ok().build();
	}
}
