package com.jobportal.seekers;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SeekerRepository extends JpaRepository<SeekerEntity, Long> {

	SeekerEntity findByUsername(String username);

}
