package com.mediasoft.warehouse.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Aspect
@Component
@Slf4j
public class TransactionAspect {
    private final ThreadLocal<String> callingMethodName = new ThreadLocal<>();

    @Around("@annotation(org.springframework.transaction.annotation.Transactional)")
    public Object logTransactionalMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        callingMethodName.set(joinPoint.getSignature().toShortString());
        return joinPoint.proceed();
    }

    @Before("@annotation(org.springframework.transaction.annotation.Transactional)")
    public void registerTransactionSynchronization() {
        String methodName = callingMethodName.get();
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            private long startTime;

            @Override
            public void beforeCommit(boolean readOnly) {
                startTime = System.nanoTime();
            }

            @Override
            public void afterCommit() {
                long endTime = System.nanoTime();
                long executionTimeNano = endTime - startTime;
                double executionTimeSec = executionTimeNano / 1e6;
                log.info("Transactional method: {} | {} ms", methodName, executionTimeSec);
            }
        });
    }
}