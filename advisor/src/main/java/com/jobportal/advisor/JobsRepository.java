package com.jobportal.advisor;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JobsRepository  extends JpaRepository<JobEntity, Long> {

}
