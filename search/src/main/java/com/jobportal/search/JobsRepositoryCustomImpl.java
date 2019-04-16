package com.jobportal.search;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class JobsRepositoryCustomImpl implements JobsRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    public List<JobEntity> findJobByQuery(String q, String location) {
        //TODO Create custom jobs query based on search parameters
    	return new ArrayList<JobEntity>();
    }
}
