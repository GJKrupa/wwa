package uk.me.krupa.wwa.service;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Created by krupagj on 25/03/2014.
 */
@Aspect
@Component
public class ServiceAspect {

    @Pointcut("execution (* uk.me.krupa.wwa.service..*(..))")
    public void serviceMethod() { }

    @Around("serviceMethod()")
    public Object doNothing(ProceedingJoinPoint joinPoint) throws Throwable {
        System.err.println(joinPoint.getSignature().toLongString());
        return joinPoint.proceed();
    }

}
