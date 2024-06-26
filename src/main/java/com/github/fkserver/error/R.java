package com.github.fkserver.error;

import lombok.Data;

@Data
public class R<T> {

    private int code;
    private String message;
    private T data;

    public R(IErrorCode code) {
        this(code.getCode(), code.getMessage(), null);
    }

    public R(IErrorCode code, String message) {
        this(code.getCode(), message, null);
    }

    public R(IErrorCode code, T data) {
        this(code.getCode(), code.getMessage(), data);
    }

    public R(IErrorCode code, T data, String message) {
        this(code.getCode(), message, data);
    }

    public R(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> R<T> data(T data) {
        return data(data, "操作成功");
    }

    public static <T> R<T> data(T data, String msg) {
        return data(200, data, msg);
    }

    public static <T> R<T> data(int code, T data, String msg) {
        return new R(code, data == null ? "暂无承载数据" : msg, data);
    }

    public static <T> R<T> success(IErrorCode code) {
        return new R<>(code);
    }

    public static <T> R<T> success(String msg) {
        return new R<>(ErrorCode.SUCCESS, msg);
    }

    public static <T> R<T> success(IErrorCode code, String msg) {
        return new R<>(code, msg);
    }

    public static <T> R<T> fail(String msg) {
        return new R<>(ErrorCode.FAILURE, msg);
    }

    public static <T> R<T> fail(int code, String msg) {
        return new R<>(code, msg, null);
    }

    public static <T> R<T> fail(IErrorCode code) {
        return new R<>(code);
    }

    public static <T> R<T> fail(IErrorCode code, String msg) {
        return new R<>(code.getCode(), msg, null);
    }

    public static <T> R<T> status(boolean flag) {
        return flag ? success("操作成功") : fail("操作失败");
    }

}
