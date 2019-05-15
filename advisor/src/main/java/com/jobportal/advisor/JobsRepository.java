package com.jobportal.advisor;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobsRepository extends JpaRepository<JobEntity, Long> {
    public List<JobEntity> findAllByLocation(String location);
}