package com.jobportal.applications;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jobportal.applications.JobCenterEntity;

@Repository
@FeignClient(name="jobcenters")
@RibbonClient(name="jobcenters")
public interface JobCenterProxy {

	@RequestMapping(value = "/api/centers/{username}", method = RequestMethod.GET)
	public ResponseEntity<JobCenterEntity> getJobCenter(@RequestHeader("X-User-Header") String loggedUser, @PathVariable String username);

}
