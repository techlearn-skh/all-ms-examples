package com.skh.services;

import com.skh.controllers.ProducerController;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

//@Slf4j
@Aspect
@Component
public class LoggingAspect {
    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);
    /**
     * Pointcut to match all Spring beans
     */
    @Pointcut(
            "within(com.skh.services..*) || " +
                    "within(com.skh.controllers..*)"
    )
    public void springBeanPointcut() {}

    /**
     * Logs method entry
     */
    @Before("springBeanPointcut()")
    public void logMethodEntry(JoinPoint joinPoint) {
        log.info("➡️ Entering method: {}.{}() with arguments = {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()));
    }

    /**
     * Logs method exit
     */
    @AfterReturning(pointcut = "springBeanPointcut()", returning = "result")
    public void logMethodExit(JoinPoint joinPoint, Object result) {
        log.info("⬅️ Exiting method: {}.{}() with result = {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                result);
    }

    /**
     * Logs exceptions
     */
    @AfterThrowing(pointcut = "springBeanPointcut()", throwing = "exception")
    public void logMethodException(JoinPoint joinPoint, Exception exception) {
        log.error("❌ Exception in method: {}.{}() with cause = {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                exception.getMessage(),
                exception);
    }

    /**
     * Logs execution time
     */
    @Around("springBeanPointcut()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long endTime = System.currentTimeMillis();

        log.info("⏱️ Execution time of {}.{}() = {} ms",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                (endTime - startTime));

        return result;
    }
}
