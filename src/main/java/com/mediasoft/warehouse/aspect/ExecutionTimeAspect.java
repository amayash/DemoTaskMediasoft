package com.mediasoft.warehouse.aspect;

import com.mediasoft.warehouse.annotation.MeasureExecutionTime;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@Aspect
public class ExecutionTimeAspect {
    @Around("@annotation(measureExecutionTime)")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint, MeasureExecutionTime measureExecutionTime) throws Throwable {
        long startTime = System.nanoTime();

        Object result = joinPoint.proceed();

        long endTime = System.nanoTime();
        long executionTimeInNanos = endTime - startTime;
        Signature signature = joinPoint.getSignature();
        double executionTimeInMilliseconds = executionTimeInNanos / 1e6;
        log.info("{} execution time: {} ms", signature.toShortString(), executionTimeInMilliseconds);
        return result;
    }
}