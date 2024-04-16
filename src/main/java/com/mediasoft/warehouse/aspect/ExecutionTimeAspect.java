package com.mediasoft.warehouse.aspect;

import com.mediasoft.warehouse.annotation.MeasureExecutionTime;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;


@Component
@Aspect
@Slf4j
public class ExecutionTimeAspect {
    @Around("@annotation(measureExecutionTime)")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint,
                                       MeasureExecutionTime measureExecutionTime) throws Throwable {
        long startTime = System.nanoTime();
        long endTime = System.nanoTime();
        long executionTimeInNanos = endTime - startTime;
        double executionTimeInMilliseconds = executionTimeInNanos / 1e6;
        log.info("{} execution time: {} ms", joinPoint.getSignature().toShortString(), executionTimeInMilliseconds);
        return joinPoint.proceed();
    }
}