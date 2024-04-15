package com.github.fkserver.web.user;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.github.fkserver.builder.AdminUserBuilder;
import com.github.fkserver.constants.Constants;
import com.github.fkserver.dto.AdminUserDTO;
import com.github.fkserver.entity.User;
import com.github.fkserver.error.BizException;
import com.github.fkserver.error.R;
import com.github.fkserver.repository.UserRepository;
import com.github.fkserver.security.AuthoritiesConstants;
import com.github.fkserver.service.MailService;
import com.github.fkserver.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "用户信息")
public class UserResource {

    private static final List<String> ALLOWED_ORDERED_PROPERTIES = List.of("id", "login", "firstName", "lastName",
        "email", "activated", "langKey", "createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate");

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    @Value("${spring.application.name}")
    private String applicationName;

    private final UserService userService;

    private final UserRepository userRepository;

    private final MailService mailService;

    public UserResource(UserService userService, UserRepository userRepository, MailService mailService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.mailService = mailService;
    }

    /**
     * {@code POST  /admin/users} : Creates a new user.
     */
    @PostMapping("/users")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<User> createUser(@Valid @RequestBody AdminUserDTO userDTO) throws URISyntaxException {
        log.info("REST request to save User : {}", userDTO);

        if (userDTO.getId() != null) {
            throw new BizException("A new user cannot already have an ID");
            // Lowercase the user login before comparing with database
        } else if (userRepository.findOneByLogin(userDTO.getLogin().toLowerCase()).isPresent()) {
            throw new BizException("user already exists");
        } else if (userRepository.findOneByEmailIgnoreCase(userDTO.getEmail()).isPresent()) {
            throw new BizException("email already exists");
        } else {
            User newUser = userService.createUser(userDTO);
            mailService.sendCreationEmail(newUser);
            return ResponseEntity.created(new URI("/api/admin/users/" + newUser.getLogin()))
                .headers(createAlert(applicationName, newUser.getLogin())).body(newUser);
        }
    }

    private HttpHeaders createAlert(String applicationName, String param) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-" + applicationName + "-alert", "userManagement.created");
        try {
            headers.add("X-" + applicationName + "-params",
                URLEncoder.encode(param, StandardCharsets.UTF_8.toString()));
        } catch (UnsupportedEncodingException e) {
            // StandardCharsets are supported by every Java implementation so this exception will never happen
        }
        return headers;
    }

    /**
     * {@code PUT /admin/users} : Updates an existing User.
     */
    @PutMapping("/users")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public R<Optional<AdminUserDTO>> updateUser(@Valid @RequestBody AdminUserDTO userDTO) {
        log.info("REST request to update User : {}", userDTO);
        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.orElseThrow().getId().equals(userDTO.getId()))) {
            throw new BizException("email already exists");
        }
        existingUser = userRepository.findOneByLogin(userDTO.getLogin().toLowerCase());
        if (existingUser.isPresent() && (!existingUser.orElseThrow().getId().equals(userDTO.getId()))) {
            throw new BizException("user already exists");
        }
        return R.data(userService.updateUser(userDTO));
    }

    /**
     * {@code GET /admin/users} : get all users with all the details - calling this are only allowed for the
     */
    @GetMapping("/users")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public R<Page<AdminUserDTO>> getAllUsers(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.info("REST request to get all User for an admin");
        if (!pageable.getSort().stream().map(Sort.Order::getProperty).allMatch(ALLOWED_ORDERED_PROPERTIES::contains)) {
            throw new BizException("params not contains sort field");
        }
        return R.data(userService.getAllManagedUsers(pageable));
    }

    /**
     * {@code GET /admin/users/:login} : get the "login" user.
     */
    @GetMapping("/users/{login}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public R<Optional<AdminUserDTO>>
        getUser(@PathVariable("login") @Pattern(regexp = Constants.LOGIN_REGEX) String login) {
        log.info("REST request to get User : {}", login);
        return R.data(userService.getUserWithAuthoritiesByLogin(login).map(AdminUserBuilder::build));
    }

    /**
     * {@code DELETE /admin/users/:login} : delete the "login" User.
     */
    @DeleteMapping("/users/{login}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public R deleteUser(@PathVariable("login") @Pattern(regexp = Constants.LOGIN_REGEX) String login) {
        log.debug("REST request to delete User: {}", login);
        userService.deleteUser(login);
        return R.status(true);
    }
}
