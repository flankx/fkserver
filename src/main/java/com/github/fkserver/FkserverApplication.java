package com.github.fkserver;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MarkerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;

import com.github.fkserver.config.ApplicationProperties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@EnableConfigurationProperties(value = {ApplicationProperties.class})
public class FkserverApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(FkserverApplication.class);
        Map<String, Object> defaultEnvironment = new HashMap<>();
        // 默认配置 dev
        defaultEnvironment.put("spring.profiles.default", "dev");
        app.setDefaultProperties(defaultEnvironment);
        Environment env = app.run(args).getEnvironment();
        logApplicationStartup(env);
    }

    public static void logApplicationStartup(Environment env) {
        String applicationName = env.getProperty("spring.application.name");
        String serverPort = env.getProperty("server.port");
        String contextPath = Optional.ofNullable(env.getProperty("server.servlet.context-path"))
            .filter(StringUtils::isNotBlank).orElse("/");
        String hostAddress = "localhost";
        String protocol = "http";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("The host name could not be determined, using `localhost` as fallback");
        }
        log.info(MarkerFactory.getMarker("CRLF_SAFE"), """

            ----------------------------------------------------------
            \tApplication '{}' is running! Access URLs:
            \tLocal: \t\t{}://localhost:{}{}
            \tExternal: \t{}://{}:{}{}
            \tProfile(s): \t{}
            ----------------------------------------------------------""", applicationName, protocol, serverPort,
            contextPath, protocol, hostAddress, serverPort, contextPath,
            env.getActiveProfiles().length == 0 ? env.getDefaultProfiles() : env.getActiveProfiles());
    }

}
