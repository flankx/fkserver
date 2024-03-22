package com.github.fkserver.web.user;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.fkserver.dto.UserDTO;
import com.github.fkserver.error.BizException;
import com.github.fkserver.error.R;
import com.github.fkserver.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api")
@Tag(name = "用户用信息")
public class PublicUserResource {

    private static final List<String> ALLOWED_ORDERED_PROPERTIES =
        List.of("id", "login", "firstName", "lastName", "email", "activated", "langKey");

    private final Logger log = LoggerFactory.getLogger(PublicUserResource.class);

    private final UserService userService;

    public PublicUserResource(UserService userService) {
        this.userService = userService;
    }

    /**
     * {@code GET /users} : get all users with only public information - calling this method is allowed for anyone.
     */
    @GetMapping("/users")
    public R<Page<UserDTO>> getAllPublicUsers(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get all public User names");
        if (!pageable.getSort().stream().map(Sort.Order::getProperty).allMatch(ALLOWED_ORDERED_PROPERTIES::contains)) {
            throw new BizException("params not contains sort field");
        }
        return R.data(userService.getAllPublicUsers(pageable));
    }

    /**
     * Gets a list of all roles.
     */
    @GetMapping("/authorities")
    public R<List<String>> getAuthorities() {
        return R.data(userService.getAuthorities());
    }

}
