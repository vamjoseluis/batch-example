package com.tuxpuckpepe.batchexample.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.tuxpuckpepe.batchexample.model.Dog;

/**
 * 
 * @author pepe
 *
 */
@Component
public class JobListener implements JobExecutionListener{

	private static final Logger LOG = LoggerFactory.getLogger(JobListener.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
	
	@Override
	public void beforeJob(JobExecution jobExecution) {
		LOG.info("Starting job");
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		LOG.info("Job is done, pls check the result stored in DB...");
		jdbcTemplate
			.query("SELECT name, breed, age FROM dog", 
					(rs, row) -> new Dog(rs.getString(1), rs.getString(2), rs.getInt(3)))
			.forEach(dog -> LOG.info("< " + dog + " >"));
	}

	
}
