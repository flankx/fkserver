package com.github.fkserver.dto;

import java.io.Serial;
import java.io.Serializable;

import lombok.Data;

@Data
public class UserDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -5510020679385073728L;

    private Long id;

    private String login;

}
