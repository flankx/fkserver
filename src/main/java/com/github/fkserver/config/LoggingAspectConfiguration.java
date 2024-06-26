package com.github.fkserver.config;

import com.github.fkserver.aop.LoggingAspect;
import com.github.fkserver.aop.RequestLogAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

@Configuration
@EnableAspectJAutoProxy
public class LoggingAspectConfiguration {

    @Bean
    @Profile("dev")
    public LoggingAspect loggingAspect(Environment env) {
        return new LoggingAspect(env);
    }

    @Bean
    @Profile("prod")
    public RequestLogAspect requestLogAspect(Environment env) {
        return new RequestLogAspect(env);
    }

}
