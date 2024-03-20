package com.github.fkserver.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "登录信息")
public class LoginDTO {

    @NotNull
    @Size(min = 1, max = 50)
    @Schema(description = "用户名称")
    private String username;

    @NotNull
    @Size(min = 4, max = 100)
    @Schema(description = "用户密码")
    private String password;

    @Schema(description = "记住我")
    private boolean rememberMe;

}
