package com.solution.backend.exceptions;

/**
 *
 * @author dragomir
 */
public class BadRequestException extends BaseApiException {

    public BadRequestException() {
        super(400);
    }

    public BadRequestException(String message) {
        super(400, message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(400, message, cause);
    }

}
