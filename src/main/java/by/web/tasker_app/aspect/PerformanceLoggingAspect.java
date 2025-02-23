package by.web.tasker_app.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PerformanceLoggingAspect {
    
    private static final Logger log = LoggerFactory.getLogger(PerformanceLoggingAspect.class);

    @Around("@annotation(LogExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        log.debug("Starting method: {}", methodName);
        
        long start = System.currentTimeMillis();
        try {
            Object proceed = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - start;
            log.debug("Method {} completed in {} ms", methodName, executionTime);
            return proceed;
        } catch (Throwable e) {
            log.error("Method {} failed after {} ms", methodName, System.currentTimeMillis() - start, e);
            throw e;
        }
    }
} 