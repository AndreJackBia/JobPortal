package com.jobportal.seekers;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Repository
@FeignClient(name = "applications")
@RibbonClient(name = "applications")
public interface ApplicationsProxy {

	@RequestMapping(value = "/api/seekers/{username}/applications", method = RequestMethod.DELETE)
	ResponseEntity deleteAllByUsername(@RequestHeader("X-User-Header") String loggedUser, @PathVariable String username);
	
}
