package com.jobportal.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
public class SearchController {

    @Autowired
    JobsRepository jobsRepository;

    /** Get all the jobs that correspond to the query and location parameters. */
    @RequestMapping(value = "/api/jobs/search", method = RequestMethod.GET)
    public ResponseEntity<List<JobEntity>> newJobs(@RequestParam("q") Optional<String> q,
                                                   @RequestParam("location") Optional<String> locationOpt) {
        String query = "";
        String location = "";

        if (q.isPresent()) {
            query = q.get();
        }
        if (locationOpt.isPresent()) {
            location = locationOpt.get();
        }

        List<JobEntity> result = jobsRepository.findJobByQuery(query, location);
        return ResponseEntity.ok(result);
    }

}
