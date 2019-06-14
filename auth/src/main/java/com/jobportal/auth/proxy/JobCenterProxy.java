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
@FeignClient(name="jobcenters")
@RibbonClient(name="jobcenters")
public interface JobCenterProxy {

	@RequestMapping(value = "/api/centers", method = RequestMethod.POST)
	public ResponseEntity createCenter(@RequestBody JobCenterEntity jobCenterEntity);

	@RequestMapping(value="/api/centers/{centerName}", method = RequestMethod.GET)
	public boolean existsCenter(@PathVariable("centerName") String centerName);

	@RequestMapping(value = "/api/centers/{username}", method = RequestMethod.DELETE)
	public ResponseEntity deleteCenter(@RequestHeader("X-User-Header") String loggedUser, @PathVariable String username);

	@RequestMapping(value = "/api/centers/{username}", method = RequestMethod.PUT)
	public ResponseEntity<JobCenterEntity> changeCenter(@RequestHeader("X-User-Header") String loggedUser,
														   @PathVariable String username,
														   @RequestBody JobCenterEntity jobCenter);
}