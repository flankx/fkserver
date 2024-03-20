package com.github.fkserver.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "登录令牌")
public class JwtTokenDTO {
    @JsonProperty("id_token")
    private String idToken;
}
