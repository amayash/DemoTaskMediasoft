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

/**
 * Запланированная задача для обновления цен товаров с использованием Spring Batching.
 */
@Component
@RequiredArgsConstructor
@ConditionalOnExpression("'${app.scheduling.enabled}'.equals('true') and '${app.scheduling.optimization}'.equals('true')" +
        " and '${app.scheduling.spring-batching}'.equals('true')")
@Profile("!dev")
@Slf4j
public class OptimizedSchedulerWithSpringBatching {
    private final JobLauncher jobLauncher;
    private final Job importUserJob;

    /**
     * Метод запускается периодически с фиксированной задержкой
     * для обновления цен товаров с использованием Spring Batching.
     */
    @Scheduled(fixedDelayString = "${app.scheduling.period}")
    @MeasureExecutionTime
    public void scheduleFixedDelayTask() {
        try {
            log.info("Start.");
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("JobID", String.valueOf(System.currentTimeMillis()))
                    .toJobParameters();
            jobLauncher.run(importUserJob, jobParameters);
            log.info("Batch job successfully triggered.");
        } catch (JobExecutionException e) {
            log.error("Error triggering batch job: {}", e.getMessage());
        }
    }
}
