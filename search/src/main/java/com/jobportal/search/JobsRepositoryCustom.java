package com.jobportal.search;

import java.util.List;

public interface JobsRepositoryCustom {
    public List<JobEntity> findJobByQuery(String q, String location);
}
