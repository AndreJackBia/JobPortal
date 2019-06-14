package com.jobportal.search;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;

import java.util.LinkedList;
import java.util.List;

public class JobsRepositoryCustomImpl implements JobsRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * This methods implements a custom query for the research of jobs following Spring rules.
     * @param: query
     * @param: location
     * @return: list of JobEntities that satisfy the research criterion
     */
    public List<JobEntity> findJobByQuery(String q, String location) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<JobEntity> query = cb.createQuery(JobEntity.class);
        Root<JobEntity> jobEntity = query.from(JobEntity.class);
        List<Predicate> queryPredicates = new LinkedList<>();


        List<Predicate> termPredicates = new LinkedList<>();
        if (q != null && !q.isEmpty()) {
            Path<String> description = jobEntity.get("jobDescription");
            Path<String> position = jobEntity.get("position");
            Path<String> companyName = jobEntity.get("companyName");
            for (String term : q.split(" ")) {
                termPredicates.add(cb.like(cb.upper(description), "%" + term.toUpperCase() + "%"));
                termPredicates.add(cb.like(cb.upper(position), "%" + term.toUpperCase() + "%"));
                termPredicates.add(cb.like(cb.upper(companyName), "%" + term.toUpperCase() + "%"));
            }
            queryPredicates.add(cb.or(termPredicates.toArray(new Predicate[termPredicates.size()])));
        }


        if (location != null && !location.isEmpty()) {
            Path<String> loc = jobEntity.get("location");
            queryPredicates.add(cb.like(cb.upper(loc), "%" + location.toUpperCase() + "%"));
        }

        if (queryPredicates.size() > 0) {
            query.select(jobEntity)
                    .where(cb.and(queryPredicates.toArray(new Predicate[queryPredicates.size()])))
                    .orderBy(cb.desc(jobEntity.get("dateCreation")));
        } else {
            query.select(jobEntity)
                    .orderBy(cb.desc(jobEntity.get("dateCreation")));
        }

        return entityManager.createQuery(query).getResultList();
    }
}
