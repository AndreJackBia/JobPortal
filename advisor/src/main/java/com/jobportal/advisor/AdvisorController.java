package com.jobportal.advisor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	
	/**
	 * This method is used to show all jobs on personal seeker's dashboard ordered by number of matches between user skills and jobs skills
	 *
	 * @param loggedUser
	 * @param username
	 * @return 401 if user is not logged, 404 if user doesn't exist, 200 and
	 *         seeker's best matches job
	 */
	@RequestMapping(value = "api/seekers/{username}/suggestions", method = RequestMethod.GET)
	public ResponseEntity<List<JobEntity>> getAllJobs(@RequestHeader("X-User-Header") String loggedUser,
			@PathVariable String username) {
		if (!username.equals(loggedUser))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		SeekerEntity user = seekerProxy.getSeeker(loggedUser, username);
		if (user == null)
			return ResponseEntity.notFound().build();

		List<JobEntity> jobs = jobsRepository.findAll();
		
		if (jobs == null)
			return ResponseEntity.ok().build();
		
		List<String> skills = user.getSkills();
		
		if (skills == null)
			return ResponseEntity.ok().build();
		
		Map<Long, List<JobEntity>> resultMatched = new HashMap();

		for (JobEntity jobEntity : jobs) {
			long matches = skills.stream().distinct().filter(jobEntity.getSkills()::contains).count();
			if (matches > 0) {
				if (resultMatched.get(Long.valueOf(matches)) == null)
					resultMatched.put(matches, new ArrayList());
				if (jobEntity.getLocation().equals(user.getCity()))
					resultMatched.get(Long.valueOf(matches)).add(0, jobEntity);
				else
					resultMatched.get(Long.valueOf(matches)).add(jobEntity);
			}
		}

		List<Long> keys = new ArrayList(resultMatched.keySet());
		Collections.sort(keys);
		List<JobEntity> result = new ArrayList();
		for (int i = keys.size() - 1; i >= 0; i--) {
			result.addAll(resultMatched.get(keys.get(i)));
		}
		
		return ResponseEntity.ok(result);
		// return ResponseEntity.ok(jobsRepository.findAllByLocation(user.getCity()));
	}
}