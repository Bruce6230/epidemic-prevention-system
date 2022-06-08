package com.makiyo.exception;

import lombok.Data;

@Data
public class EpsException extends RuntimeException
{
    private String msg;

    private int code = 500;

    public EpsException(String msg) {
        this.msg = msg;
    }

    public EpsException(Throwable cause, String msg) {
        super(msg, cause);
        this.msg = msg;
    }

    public EpsException(String msg, int code) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public EpsException(Throwable cause, String msg, int code) {
        super(msg,cause);
        this.msg = msg;
        this.code = code;
    }
}
