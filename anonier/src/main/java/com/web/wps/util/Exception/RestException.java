package com.web.wps.util.Exception;

public class RestException extends RuntimeException {
    private static final long serialVersionUID = -4090267321805131103L;
    private Integer code;
    private Object[] args;

    public RestException(String message, Object... args) {
        super(message);
        this.args = args;
    }

    public RestException(String message, Throwable cause) {
        super(message, cause);
    }

    public RestException(int code, String message) {
        super(message);
        this.code = code;
    }

    public RestException(int code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public Integer getCode() {
        return this.code;
    }

    public Object[] getArgs() {
        return this.args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }
}