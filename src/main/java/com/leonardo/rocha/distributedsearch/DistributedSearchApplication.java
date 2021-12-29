package com.leonardo.rocha.distributedsearch;

import com.leonardo.rocha.distributedsearch.cluster.management.LeaderElection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DistributedSearchApplication implements CommandLineRunner {
	private static final Logger LOG = LoggerFactory
			.getLogger(DistributedSearchApplication.class);

	@Autowired
	private LeaderElection leaderElection;

	public static void main(String[] args) {
		LOG.info("STARTING THE APPLICATION");
		SpringApplication.run(DistributedSearchApplication.class, args);
		LOG.info("APPLICATION FINISHED");
	}

	@Override
	public void run(String... args) throws Exception {
		leaderElection.volunteerForLeadership();
		leaderElection.reelectLeader();
	}
}
