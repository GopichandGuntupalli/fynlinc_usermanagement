package com.nss.usermanagement.role.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
    @Before("execution(* com.nss.usermanagement.role.controller.UserController.*(..))")
    public void logBeforeMethod(JoinPoint joinPoint){
        log.info("Entering method: {} with arguments: {}", joinPoint.getSignature(), joinPoint.getArgs());
    }
    @AfterReturning(value = "execution(* com.nss.usermanagement.role.controller.UserController.*(..))",
            returning = "result")
    public void logAfterMethod(JoinPoint joinPoint,Object result){
        log.info("Exiting method: {} with result: {}", joinPoint.getSignature(),result);
    }
    @AfterThrowing(value = "execution(* com.nss.usermanagement.role.controller.UserController.*(..))",
            throwing = "exception")
    public void logException(JoinPoint joinPoint,Throwable exception){
        log.error("Exception in method: {} with cause: {}", joinPoint.getSignature(),exception);
    }
    @Around("execution(* com.nss.usermanagement.role.controller.UserController.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint)throws  Throwable{
      long startTime=System.currentTimeMillis();
      Object proceed=joinPoint.proceed();
      long executionTime=System.currentTimeMillis()-startTime;
      log.info("Method {} executed in {} ms",joinPoint.getSignature(),executionTime);
      return proceed;
    }
}
