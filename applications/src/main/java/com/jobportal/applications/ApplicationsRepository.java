package com.jobportal.applications;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationsRepository  extends JpaRepository<ApplicationsEntity, Long> {

}
