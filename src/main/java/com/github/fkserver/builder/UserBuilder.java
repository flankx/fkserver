package com.github.fkserver.builder;

import com.github.fkserver.dto.UserDTO;
import com.github.fkserver.entity.User;

public class UserBuilder {

    public static UserDTO build(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setLogin(user.getLogin());
        return dto;
    }

}
