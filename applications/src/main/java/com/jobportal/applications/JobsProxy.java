package com.jobportal.applications;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@Repository
@FeignClient(name = "jobs")
@RibbonClient(name = "jobs")
public interface JobsProxy {
	
	@RequestMapping(value = "/api/centers/{username}/jobs/{jobId}", method = RequestMethod.GET)
	JobEntity getJob(@RequestHeader("X-User-Header") String loggedUser, @PathVariable String username, @PathVariable long jobId);

}