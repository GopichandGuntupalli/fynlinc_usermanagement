package com.nss.usermanagement.role.exception;

import java.io.Serial;

public class GeneralRunTimeException extends RuntimeException{
@Serial
    private static final long serialVersionUID = 1L;

    public GeneralRunTimeException(String message) {
        super(message);
    }

    public GeneralRunTimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
