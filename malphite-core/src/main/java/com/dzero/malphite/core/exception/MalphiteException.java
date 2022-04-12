package com.dzero.malphite.core.exception;

import lombok.Data;

/**
 * Malphite异常类
 */
@Data
public class MalphiteException extends RuntimeException {

    private String errorCode;

    public MalphiteException() {
        super();
    }

    public MalphiteException(String message) {
        super(message);
    }

    public MalphiteException(String message, Throwable cause) {
        super(message, cause);
    }

    public MalphiteException(Throwable cause) {
        super(cause);
    }
}
