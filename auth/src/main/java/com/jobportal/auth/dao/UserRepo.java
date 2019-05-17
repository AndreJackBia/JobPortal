package com.jobportal.auth.dao;

import com.jobportal.auth.model.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends CrudRepository<Account, Long> {

    Account findByUsername(String username);
    void deleteByUsername(String username);
    boolean existsByUsername(String username);
}