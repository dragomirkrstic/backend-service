package com.solution.backend.exceptions;

/**
 *
 * @author dragomir
 */
public abstract class BaseApiException extends RuntimeException {

    private final int status;

    public BaseApiException(int status) {
        this.status = status;
    }

    public BaseApiException(int status, String message) {
        super(message);
        this.status = status;
    }

    public BaseApiException(int status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

}
