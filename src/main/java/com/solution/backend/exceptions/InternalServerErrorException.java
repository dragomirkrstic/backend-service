package com.solution.backend.exceptions;

/**
 *
 * @author dragomir
 */
public class InternalServerErrorException extends BaseApiException {

    public InternalServerErrorException() {
        super(500);
    }

    public InternalServerErrorException(String message) {
        super(500, message);
    }

    public InternalServerErrorException(String message, Throwable cause) {
        super(500, message, cause);
    }
}
