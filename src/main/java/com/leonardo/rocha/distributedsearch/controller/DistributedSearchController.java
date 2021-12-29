package com.leonardo.rocha.distributedsearch.controller;

import com.leonardo.rocha.distributedsearch.cluster.management.LeaderElection;
import com.leonardo.rocha.distributedsearch.model.Result;
import com.leonardo.rocha.distributedsearch.model.SerializationUtils;
import com.leonardo.rocha.distributedsearch.model.Task;
import com.leonardo.rocha.distributedsearch.search.SearchCoordinator;
import com.leonardo.rocha.distributedsearch.search.SearchWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/")
public class DistributedSearchController {
    @Autowired
    SearchCoordinator searchCoordinator;

    @Autowired
    LeaderElection leaderElection;

    @RequestMapping(value = "task", method = RequestMethod.POST)
    private byte[] searchTask(@RequestBody byte[] requestPayload){
        if(leaderElection.isLeader()){
            return null;
        }
        return SearchWorker.handleRequest(requestPayload);
    }

    @RequestMapping(value = "search", method = RequestMethod.GET)
    private byte[] coordinateSearch(@RequestBody byte[] requestPayload){
        if(!leaderElection.isLeader()){
            return null;
        }
        return searchCoordinator.handleRequest(requestPayload);
    }
}
