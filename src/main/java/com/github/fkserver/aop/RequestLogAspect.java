package com.github.fkserver.aop;

import java.util.Objects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.env.Environment;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.github.fkserver.utils.JsonUtils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
public class RequestLogAspect {

    private final Environment env;

    public RequestLogAspect(Environment env) {
        this.env = env;
    }

    @Pointcut("execution(!static org.springframework.http.ResponseEntity *(..))")
    private void resultPointcut() {}

    @Pointcut("@within(org.springframework.stereotype.Controller) || @within(org.springframework.web.bind.annotation.RestController)")
    private void controllerPointcut() {}

    @Around("resultPointcut() && controllerPointcut()")
    public Object logAround(ProceedingJoinPoint pjp) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request =
            (requestAttributes == null) ? null : ((ServletRequestAttributes)requestAttributes).getRequest();
        String requestUrl = Objects.requireNonNull(request).getRequestURI();
        String requestMethod = request.getMethod();
        Object[] args = pjp.getArgs();
        log.info("===Request=== {}: {}; params:{}", requestMethod, requestUrl, args);
        try {
            Object retVal = pjp.proceed(pjp.getArgs());
            if (!requestUrl.contains("page")) {
                log.info("===Result===  {}", JsonUtils.toJson(retVal));
            }
            return retVal;
        } finally {
            stopWatch.stop();
            log.info("===Response END=== {}: {} ({} ms)", requestMethod, requestUrl, stopWatch.getTotalTimeMillis());
        }

    }

}
