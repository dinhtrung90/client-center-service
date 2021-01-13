package com.vts.clientcenter.jobs;

import com.vts.clientcenter.domain.EmployeeEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmployeeListener implements JobExecutionListener {

    private final Logger log = LoggerFactory.getLogger(EmployeeListener.class);

    private final JdbcTemplate jdbcTemplate;

    public EmployeeListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("!!! JOB START! Time to process employee");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {

        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("!!! JOB FINISHED! Time to verify the results");

            List<EmployeeEntity> results = jdbcTemplate
                .query("SELECT * FROM employees",
                    (rs, row) -> new EmployeeEntity(rs.getString(0), rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getDate(10).toLocalDate(), rs.getString(11)));

            log.info("Found <" + results.size() + "> in the database.");

        }
    }
}
