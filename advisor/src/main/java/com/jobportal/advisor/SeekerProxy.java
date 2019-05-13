package com.jobportal.advisor;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Repository
@FeignClient(name="seekers")
@RibbonClient(name="seekers")
public interface SeekerProxy {
	
	@RequestMapping(value = "/api/seekers/{username}", method = RequestMethod.GET)
	SeekerEntity getSeeker(@RequestHeader("X-User-Header") String loggedUser, @PathVariable String username);

}