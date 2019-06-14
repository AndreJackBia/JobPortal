package com.jobportal.seekers;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class SeekerController {

	@Autowired
	private SeekerRepository seekerRepository;
	
	@Autowired
	private ApplicationsProxy applicationsProxy;
	
	@Value("${pass}")
	private String pass;

	/**
	 * This method is used to create a new seeker
	 *
	 * @param seekerEntity
	 * @return 201 if successful, 500 otherwise
	 * @throws URISyntaxException
	 */
	@RequestMapping(value = "/api/seekers", method = RequestMethod.POST)
	public ResponseEntity<SeekerEntity> createInstance(@RequestBody SeekerEntity seekerEntity)
			throws URISyntaxException {
		return ResponseEntity.created(new URI("/api/seekers" + seekerEntity.getId()))
				.body(seekerRepository.save(seekerEntity));
	}

	/**
	 * This method is used to get personal information of the registered seeker
	 *
	 * @param loggedUser
	 * @param username
	 * @return 401 if user is not logged, 404 if user doesn't exist, 200 and
	 *         seeker's data otherwise
	 */
	@RequestMapping(value = "/api/seekers/{username}", method = RequestMethod.GET)
	public ResponseEntity<SeekerEntity> getJobSeeker(@RequestHeader("X-User-Header") String loggedUser,
			@PathVariable String username) {
		
		SeekerEntity seekerEntity = seekerRepository.findByUsername(username);
		if (seekerEntity == null)
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

		return ResponseEntity.ok().body(seekerEntity);
	}

	/**
	 * This method is used to update personal information of the registered seeker.
	 *
	 * @param loggedUser
	 * @param username
	 * @return 401 if user is not logged, 404 if user doesn't exist, 200 otherwise
	 */
	@RequestMapping(value = "/api/seekers/{username}", method = RequestMethod.PUT)
	public ResponseEntity changeJobSeeker(@RequestHeader("X-User-Header") String loggedUser,
			@PathVariable String username, @RequestBody SeekerEntity seekerEntity) {
		if (!username.equals(loggedUser)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		if (!username.equals(seekerEntity.getUsername())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		if (!checkFieldUpdate(seekerEntity)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}

		SeekerEntity seekerEntityOld = seekerRepository.findByUsername(username);
		seekerEntityOld.setUsername(seekerEntity.getUsername());
		seekerEntityOld.setFirstName(seekerEntity.getFirstName());
		seekerEntityOld.setLastName(seekerEntity.getLastName());
		seekerEntityOld.setCity(seekerEntity.getCity());
		seekerEntityOld.setEmail(seekerEntity.getEmail());
		seekerEntityOld.setSkills(seekerEntity.getSkills());
		seekerRepository.save(seekerEntityOld);
		return ResponseEntity.ok(seekerEntityOld);
	}

	private boolean checkFieldUpdate(SeekerEntity newSeeker) {
		if (newSeeker.getUsername() == null) {
			return false;
		}
		if (newSeeker.getFirstName() == null) {
			return false;
		}
		if (newSeeker.getLastName() == null) {
			return false;
		}
		if (newSeeker.getEmail() == null) {
			return false;
		}
		if (newSeeker.getCity() == null) {
			return false;
		}
		if (newSeeker.getBirth() == null) {
			return false;
		}
		return true;
	}

	/**
	 * This method is used to delete personal information of the registered seeker.
	 *
	 * @param loggedUser
	 * @param username
	 * @return 401 if user is not logged, 404 if user doesn't exist, 200 otherwise
	 */
	@RequestMapping(value = "/api/seekers/{username}", method = RequestMethod.DELETE)
	public ResponseEntity deleteJobSeeker(@RequestHeader("X-User-Header") String loggedUser,
			@PathVariable String username) {
		if (!username.equals(loggedUser))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

		SeekerEntity seeker = seekerRepository.findByUsername(username);
		if (seeker == null)
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

		applicationsProxy.deleteAllByUsername(loggedUser, seeker.getUsername());
		CouchDBHelper.deleteDocument(username);
		seekerRepository.delete(seeker);
		return ResponseEntity.ok().build();
	}
	/**
	 * This method is used to upload the curriculum to the dedicated database.
	 * 
	 * @param username
	 * @param file
	 * @return 200 if the curriculum is uploaded successfully, 400 otherwise
	 */
	@RequestMapping(value = "/api/seekers/{username}/cv", method = RequestMethod.POST)
	public ResponseEntity saveCurriculum(@PathVariable String username, @RequestParam("file") MultipartFile file) {
		try {
			CouchDBHelper.saveDocument(username, file.getInputStream());
		} catch (IOException e) {
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok().build();
}

	/**
	 *
	 * This method is used to get the skills of the registered seeker.
	 *
	 * @param loggedUser
	 * @param username
	 * @return 401 if user is not logged, 404 if user doesn't exist, 200 and the
	 *         list of skills otherwise
	 *
	 * @throws URISyntaxException
	 */
	@RequestMapping(value = "/api/seekers/{username}/skills", method = RequestMethod.GET)
	public ResponseEntity<List<String>> getSkills(@RequestHeader("X-User-Header") String loggedUser,
			@PathVariable String username) throws URISyntaxException {

		if (!username.equals(loggedUser))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

		SeekerEntity seeker = seekerRepository.findByUsername(username);
		if (seeker == null)
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

		return ResponseEntity.ok(seeker.getSkills());
	}

	/**
	 *
	 * This method is used to add skills to the registered user.
	 *
	 * @param loggedUser
	 * @param username
	 * @param skills
	 * @return 401 if user is not logged, 404 if user doesn't exist, 200 and the
	 *         updated list of skills otherwise
	 *
	 * @throws URISyntaxException
	 */
	@RequestMapping(value = "/api/seekers/{username}/skills", method = RequestMethod.PUT)
	public ResponseEntity<List<String>> addSkills(@RequestHeader("X-User-Header") String loggedUser,
			@PathVariable String username, @RequestBody List<String> newSkills) throws URISyntaxException {
		if (!username.equals(loggedUser))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

		SeekerEntity seeker = seekerRepository.findByUsername(username);
		if (seeker == null)
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

		seeker.setSkills(newSkills);
		seeker = seekerRepository.save(seeker);
		return ResponseEntity.ok(seeker.getSkills());

	}

	/**
	 *
	 * This method is used to delete a skill of a registered user.
	 *
	 * @param loggedUser
	 * @param username
	 * @param skill
	 * @return 401 if user is not logged, 404 if user doesn't exist, 200 otherwise
	 * @throws URISyntaxException
	 */
	@RequestMapping(value = "/api/seekers/{username}/skills/{skill}", method = RequestMethod.DELETE)
	public ResponseEntity deleteSkill(@RequestHeader("X-User-Header") String loggedUser, @PathVariable String username,
			@PathVariable String skill) throws URISyntaxException {
		if (!username.equals(loggedUser))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

		SeekerEntity seeker = seekerRepository.findByUsername(username);
		if (seeker == null)
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

		seeker.removeSkill(skill);
		seekerRepository.save(seeker);
		return ResponseEntity.ok().build();
	}
	
}
