package com.github.fkserver.security;

import java.util.List;
import java.util.Locale;

import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.github.fkserver.entity.Authority;
import com.github.fkserver.entity.User;
import com.github.fkserver.repository.UserRepository;

@Service("sserDetailsService")
public class DomainUserDetailsService implements UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(DomainUserDetailsService.class);

    private final UserRepository userRepository;

    public DomainUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        logger.info("Authenticating {}", login);
        if (new EmailValidator().isValid(login, null)) {
            return userRepository.findOneByEmailIgnoreCase(login).map(user -> createSpringSecurityUser(login, user))
                .orElseThrow(() -> new RuntimeException("User with email " + login + " was not found in the database"));
        }
        String lowercaseLogin = login.toLowerCase(Locale.ENGLISH);

        return userRepository.findOneWithAuthoritiesByLogin(lowercaseLogin)
            .map(user -> createSpringSecurityUser(lowercaseLogin, user))
            .orElseThrow(() -> new RuntimeException("User " + lowercaseLogin + " was not found in the database"));
    }

    private org.springframework.security.core.userdetails.User createSpringSecurityUser(String lowercaseLogin,
        User user) {
        if (!user.isActivated()) {
            throw new RuntimeException("User " + lowercaseLogin + " was not activated");
        }
        List<SimpleGrantedAuthority> grantedAuthorities =
            user.getAuthorities().stream().map(Authority::getName).map(SimpleGrantedAuthority::new).toList();
        return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(),
            grantedAuthorities);
    }

}
