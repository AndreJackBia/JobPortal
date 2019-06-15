package com.jobportal.applications;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationsRepository  extends JpaRepository<ApplicationEntity, Long> {
	
	public List<ApplicationEntity> findAllByUsername(String username);
	public ApplicationEntity findByUsernameAndJobId(String username, long jobId);
	public List<ApplicationEntity> findAllByCenterUsername(String username);
	public List<ApplicationEntity> findAllByJobId(long jobId);
	public void deleteAllByJobId(long jobId);
	public void deleteAllByUsername(String username);


}
