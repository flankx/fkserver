package com.github.fkserver.error;

public class BizException extends RuntimeException {

    private static final long serialVersionUID = -4966587587799868847L;

    private final IErrorCode resultCode;

    public BizException(String message) {
        super(message);
        this.resultCode = ErrorCode.FAILURE;
    }

    public BizException(IErrorCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }

    public BizException(IErrorCode resultCode, Throwable cause) {
        super(cause);
        this.resultCode = resultCode;
    }

    public IErrorCode getResultCode() {
        return resultCode;
    }

}
