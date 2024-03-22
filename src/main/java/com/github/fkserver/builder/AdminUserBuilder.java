package com.github.fkserver.builder;

import java.util.stream.Collectors;

import com.github.fkserver.dto.AdminUserDTO;
import com.github.fkserver.entity.Authority;
import com.github.fkserver.entity.User;

public class AdminUserBuilder {

    public static AdminUserDTO build(User user) {
        AdminUserDTO adminUser = new AdminUserDTO();
        adminUser.setId(user.getId());
        adminUser.setLogin(user.getLogin());
        adminUser.setFirstName(user.getFirstName());
        adminUser.setLastName(user.getLastName());
        adminUser.setEmail(user.getEmail());
        adminUser.setActivated(user.isActivated());
        adminUser.setImageUrl(user.getImageUrl());
        adminUser.setLangKey(user.getLangKey());
        adminUser.setCreatedBy(user.getCreatedBy());
        adminUser.setCreatedDate(user.getCreatedDate());
        adminUser.setLastModifiedBy(user.getLastModifiedBy());
        adminUser.setLastModifiedDate(user.getLastModifiedDate());
        adminUser.setAuthorities(user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet()));
        return adminUser;
    }

}
