package com.mediasoft.warehouse.aspect;

import com.mediasoft.warehouse.annotation.MeasureExecutionTime;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * Аспект, который измеряет время выполнения методов,
 * помеченных аннотацией {@link MeasureExecutionTime}.
 */
@Component
@Aspect
@Slf4j
public class ExecutionTimeAspect {
    /**
     * Метод используется для измерения времени выполнения методов.
     *
     * @param joinPoint            точка присоединения, представляющая вызов целевого метода
     * @param measureExecutionTime аннотация, примененная к методу
     * @return результат выполнения метода
     * @throws Throwable, если возникает исключение при выполнении метода
     */
    @Around("@annotation(measureExecutionTime)")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint,
                                       MeasureExecutionTime measureExecutionTime) throws Throwable {
        long startTime = System.nanoTime();
        try {
            return joinPoint.proceed();
        } finally {
            long endTime = System.nanoTime();
            long executionTimeInNanos = endTime - startTime;
            double executionTimeInMilliseconds = executionTimeInNanos / 1e6;
            log.info("{} execution time: {} ms", joinPoint.getSignature().toShortString(), executionTimeInMilliseconds);
        }
    }
}