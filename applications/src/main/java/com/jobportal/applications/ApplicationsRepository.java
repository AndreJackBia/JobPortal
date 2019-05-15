package com.jobportal.applications;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationsRepository  extends JpaRepository<ApplicationsEntity, Long> {
	public List<ApplicationsEntity> findAllByUsername(String username);
	public void deleteAllByUsername(String username);


}
