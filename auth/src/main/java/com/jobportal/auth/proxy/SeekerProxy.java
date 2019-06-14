package com.jobportal.auth.proxy;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Repository
@FeignClient(name = "seekers")
@RibbonClient(name = "seekers")
public interface SeekerProxy {

	@RequestMapping(value = "/api/seekers", method = RequestMethod.POST)
	public ResponseEntity createSeeker(SeekerEntity seekerEntity);

	@RequestMapping(value = "/api/seekers/{username}", method = RequestMethod.DELETE)
	public ResponseEntity deleteSeeker(@RequestHeader("X-User-Header") String loggedUser,
			@PathVariable String username);

	@RequestMapping(value = "/api/seekers/{username}", method = RequestMethod.PUT)
	public ResponseEntity changeSeeker(@RequestHeader("X-User-Header") String loggedUser,
								@PathVariable String username,
								@RequestBody SeekerEntity seekerEntity);

}