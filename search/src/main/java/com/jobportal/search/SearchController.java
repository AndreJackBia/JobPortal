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

    /**
     * This method is used to browse jobs by keyword of location
     * 
     * @param query
     * @param location
     * @return 200 and the list of jobs
     * 
     */
    @RequestMapping(value = "/api/jobs/search", method = RequestMethod.GET)
    public ResponseEntity<List<JobEntity>> getJobs(@RequestParam("q") Optional<String> query,
                                                   @RequestParam("location") Optional<String> location) {
        String q = "";
        String locationOpt = "";

        if (query.isPresent()) {
            q = query.get();
        }
        if (location.isPresent()) {
            locationOpt = location.get();
        }

        List<JobEntity> result = jobsRepository.findJobByQuery(q, locationOpt);
        return ResponseEntity.ok(result);
    }

}
