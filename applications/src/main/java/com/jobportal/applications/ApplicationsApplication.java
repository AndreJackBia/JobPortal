package com.jobportal.applications;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ApplicationsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApplicationsApplication.class, args);
	}

}
