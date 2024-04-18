package com.mediasoft.warehouse.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Аспект, который отслеживает время транзакций методов,
 * помеченных аннотацией @Transactional.
 */
@Aspect
@Component
@Slf4j
public class TransactionAspect {
    private final ThreadLocal<String> callingMethodName = new ThreadLocal<>();
    private final ThreadLocal<Long> startTime = new ThreadLocal<>();

    /**
     * Метод используется для запоминания имени методов,
     * помеченных аннотацией @Transactional.
     *
     * @param joinPoint точка присоединения, представляющая вызов целевого метода
     * @return результат выполнения метода
     * @throws Throwable, если возникает исключение при выполнении метода
     */
    @Around("@annotation(org.springframework.transaction.annotation.Transactional)")
    public Object logTransactionalMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        callingMethodName.set(joinPoint.getSignature().toShortString());
        startTime.set(System.nanoTime());
        return joinPoint.proceed();
    }

    /**
     * Регистрирует синхронизацию транзакций для методов,
     * помеченных аннотацией @Transactional, чтобы измерить время выполнения после завершения транзакции.
     */
    @Before("@annotation(org.springframework.transaction.annotation.Transactional)")
    public void registerTransactionSynchronization() {
        String methodName = callingMethodName.get();
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                long endTime = System.nanoTime();
                long executionTimeNano = endTime - startTime.get();
                double executionTimeSec = executionTimeNano / 1e6;
                log.info("Transactional method: {} | {} ms", methodName, executionTimeSec);
            }
        });
    }
}