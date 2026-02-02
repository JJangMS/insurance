package com.insurance.auto.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Around("execution(* com.insurance.auto..*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        log.info("JJ::Start:: {} . {}", className, methodName);

        Object proceed = joinPoint.proceed();

        stopWatch.stop();
        log.info("JJ::End:: {} . {} - time: {} ms", className, methodName, stopWatch.getTotalTimeMillis());

        return proceed;
    }
}
