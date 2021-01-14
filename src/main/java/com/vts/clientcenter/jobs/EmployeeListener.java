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
                .query("SELECT * FROM tvs_employees",
                    (rs, row) -> new EmployeeEntity(rs.getLong("id"),
                        rs.getString("source_id"), rs.getString("first_name"),
                        rs.getString("middle_initial"), rs.getString("last_name"),
                        rs.getString("email_address"), rs.getString("phone_number"),
                        rs.getString("street"), rs.getString("city"),
                        rs.getString("state"),rs.getString("zip_code"),
                        rs.getDate("birth_date").toLocalDate(),
                        rs.getString("social_security_number")));

            log.info("Found <" + results.size() + "> in the database.");

        }
    }
}
