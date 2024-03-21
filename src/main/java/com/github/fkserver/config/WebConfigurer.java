package com.github.fkserver.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;

@Configuration
public class WebConfigurer implements ServletContextInitializer {

    private static final Logger log = LoggerFactory.getLogger(WebConfigurer.class);

    private final Environment env;

    public WebConfigurer(Environment env) {
        this.env = env;
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        if (env.getActiveProfiles().length != 0) {
            log.info("Web application configuration, using profiles: {}", (Object[])env.getActiveProfiles());
        }
        log.info("Web application fully configured");
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:8100,http://localhost:9000");
        // Enable CORS when running in GitHub CodeSpaces
        config.addAllowedOriginPattern("https://*.githubpreview.dev");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.addExposedHeader("Authorization,Link,X-Total-Count,X-FKS-alert,X-FKS-error,X-FKS-params");
        config.setAllowCredentials(true);
        config.setMaxAge(1800L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        log.info("Registering CORS filter");
        source.registerCorsConfiguration("/api/**", config);
        source.registerCorsConfiguration("/management/**", config);
        source.registerCorsConfiguration("/v3/api-docs", config);
        source.registerCorsConfiguration("/swagger-ui/**", config);
        source.registerCorsConfiguration("/doc.html", config);
        return new CorsFilter(source);
    }

}
