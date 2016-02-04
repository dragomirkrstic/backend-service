package com.solution.backend.exceptions;

/**
 *
 * @author dragomir
 */
public class UnauthorizedException extends BaseApiException {

    public UnauthorizedException() {
        super(401);
    }

    public UnauthorizedException(String message) {
        super(401, message);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(401, message, cause);
    }

}
