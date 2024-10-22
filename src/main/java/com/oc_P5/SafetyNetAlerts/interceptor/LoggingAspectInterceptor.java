package com.oc_P5.SafetyNetAlerts.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspectInterceptor {

    private Integer currentLevel = 0;


    @Pointcut("@within(org.springframework.stereotype.Service)" +
            " || @within(org.springframework.stereotype.Repository)" +
            " || @within(org.springframework.web.bind.annotation.RestController)")
    private void applicationPackagePointcut(){}


    @Around("applicationPackagePointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        this.logMethodEntry("Enter in method: " + joinPoint.getSignature().getDeclaringType().getSimpleName() + "." + joinPoint.getSignature().getName() +
                " with arguments: " + Arrays.toString(joinPoint.getArgs()));

        try {
            Object result = joinPoint.proceed();
            this.logMethodExit("Exit from method : " + joinPoint.getSignature().getDeclaringType().getSimpleName() + "." + joinPoint.getSignature().getName() +
                    " with result: " + result);
            return result;
        } catch (Exception e) {
            this.logMethodExit("Exit from method : " + joinPoint.getSignature().getDeclaringType().getSimpleName() + "." + joinPoint.getSignature().getName() +
                    " with exception: " + e.getMessage());
            throw e;
        }
    }

    private void logMethodEntry(String message) {
        if (log.isDebugEnabled()) {
            log.debug("{}{}", indent(), message);
        }
        this.currentLevel++;
    }

    private void logMethodExit(String message) {
        if (this.currentLevel > 0) {
            this.currentLevel--;
        }
        if (log.isDebugEnabled()) {
            log.debug("{}{}", indent(), message);
        }
    }

    private String indent() {
        return ">".repeat(this.currentLevel) ;
    }
}