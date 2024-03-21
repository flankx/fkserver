package com.github.fkserver.config;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;

import com.github.fkserver.security.SecurityUtils;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.util.Base64;

@Configuration
public class SecurityJwtConfiguration {

    private final ApplicationProperties applicationProperties;

    public SecurityJwtConfiguration(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withSecretKey(getSecretKey()).macAlgorithm(SecurityUtils.JWT_ALGORITHM).build();
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(getSecretKey()));
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("");
        grantedAuthoritiesConverter.setAuthoritiesClaimName(SecurityUtils.AUTHORITIES_KEY);

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    public BearerTokenResolver bearerTokenResolver() {
        var bearerTokenResolver = new DefaultBearerTokenResolver();
        bearerTokenResolver.setAllowUriQueryParameter(true);
        return bearerTokenResolver;
    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = Base64.from(applicationProperties.getJwt().getBase64Secret()).decode();
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, SecurityUtils.JWT_ALGORITHM.getName());
    }

}
