package com.mediasoft.warehouse.scheduling;

import com.mediasoft.warehouse.annotation.MeasureExecutionTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnExpression("'${app.scheduling.enabled}'.equals('true') and '${app.scheduling.optimization}'.equals('true')")
@Profile("!dev")
@Slf4j
public class OptimizedScheduler {
    private final JobLauncher jobLauncher;
    private final Job importUserJob;

    @Scheduled(fixedDelayString = "${app.scheduling.period}")
    @MeasureExecutionTime
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