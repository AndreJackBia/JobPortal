package com.jobportal.applications;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Repository
@FeignClient(name = "auth")
@RibbonClient(name = "auth")
public interface UserEntityProxy {

	@RequestMapping(value = "/recover-email/{username}", method = RequestMethod.GET)
	String getEmail(@PathVariable String username);

	@RequestMapping(value = "/users/{username}", method = RequestMethod.GET)
    UserEntity getUser(@PathVariable String username);
}