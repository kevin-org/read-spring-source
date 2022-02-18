package org.springframework.kevin.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @author dinghaifeng
 * @date 2020-12-07 14:19:23
 * @desc
 */
@Aspect
@Component
@Slf4j
public class FaceAspectj {
    @Around("@annotation(org.springframework.kevin.aop.MethodLog)")
    public Object logMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("----beforeMethod {}----", joinPoint.getSignature().getName());
        Object proceed = joinPoint.proceed();
        log.info("----afterMethod {}----", joinPoint.getSignature().getName());
        return proceed;
    }

}
