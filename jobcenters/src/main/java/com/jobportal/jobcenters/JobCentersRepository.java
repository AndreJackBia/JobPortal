package com.jobportal.jobcenters;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JobCentersRepository extends JpaRepository<JobCenterEntity, Long>  {
	
	JobCenterEntity findByName(String name);
	boolean existsByName(String name);
	JobCenterEntity findByUsername(String username);
	void deleteByUsername(String username);
}
