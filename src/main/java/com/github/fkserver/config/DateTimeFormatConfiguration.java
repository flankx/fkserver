package com.github.fkserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class DateTimeFormatConfiguration implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        DateTimeFormatterRegistrar formatterRegistrar = new DateTimeFormatterRegistrar();
        formatterRegistrar.setUseIsoFormat(true);
        formatterRegistrar.registerFormatters(registry);
    }

}
