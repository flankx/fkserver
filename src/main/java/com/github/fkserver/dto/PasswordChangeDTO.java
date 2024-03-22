package com.github.fkserver.dto;

import java.io.Serial;
import java.io.Serializable;

import lombok.Data;

@Data
public class PasswordChangeDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -3176081218281539308L;

    private String currentPassword;

    private String newPassword;

}
