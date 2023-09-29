package com.binanceproject.binance.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class InsertTimeLoggerAspect {

    private static final Logger logger = LoggerFactory.getLogger(InsertTimeLoggerAspect.class);

    @Around("execution(public * com.binanceproject.binance.repository.BinanceRepository.insert(..))")
    public Object logInsertTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        logger.info("Single insert time: {} ms", elapsedTime);
        return result;
    }

    @Around("execution(public * com.binanceproject.binance.service.LoadService.load(..))")
    public Object logLoadTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        logger.info("Total load time: {} ms", elapsedTime);
        return result;
    }

    @Around("execution(public * com.binanceproject.binance.repository.BinanceRepository.batchInsert(..))")
    public Object logBatchInsertTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        logger.info("Batch insert time: {} ms", elapsedTime);
        return result;
    }
}
