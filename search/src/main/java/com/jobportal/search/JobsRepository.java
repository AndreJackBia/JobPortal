package com.jobportal.search;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JobsRepository extends JpaRepository<JobEntity, Long>, JobsRepositoryCustom {
}
