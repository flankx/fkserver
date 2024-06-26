package com.github.fkserver.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode implements IErrorCode {
    // 错误码
    SUCCESS(200, "操作成功"),
    FAILURE(400, "业务异常"),
    INTERNAL_SERVER_ERROR(500, "服务器异常"),
    ;

    private final Integer code;
    private final String message;

}
