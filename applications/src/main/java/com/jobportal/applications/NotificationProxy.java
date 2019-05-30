package com.jobportal.applications;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Repository
@FeignClient(name="notification")
@RibbonClient(name="notification")
public interface NotificationProxy {

	@RequestMapping(value = "/api/send-notification", method = RequestMethod.POST)
	public void sendNotification(NotificationEntity notificationEntity);
}