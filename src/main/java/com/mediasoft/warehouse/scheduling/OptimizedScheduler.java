package com.mediasoft.warehouse.scheduling;

import com.mediasoft.warehouse.annotation.MeasureExecutionTime;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnExpression("'${app.scheduling.enabled}'.equals('true') and '${app.scheduling.optimization}'.equals('true')")
@Profile("prod")
public class OptimizedScheduler {
    private static final Logger log = LoggerFactory.getLogger(OptimizedScheduler.class);

    private final JobLauncher jobLauncher;
    private final Job importUserJob;

    @Scheduled(fixedDelayString = "${app.scheduling.period}")
    @MeasureExecutionTime
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public void scheduleFixedDelayTask() {
        try {
            log.info("OptimizedScheduler: Start.");
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("JobID", String.valueOf(System.currentTimeMillis()))
                    .toJobParameters();
            jobLauncher.run(importUserJob, jobParameters);
            log.info("OptimizedScheduler: Batch job successfully triggered.");
        } catch (JobExecutionException e) {
            log.error("OptimizedScheduler: Error triggering batch job: {}", e.getMessage());
        }
    }
}