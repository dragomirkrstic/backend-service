package com.solution.backend.exceptions;

/**
 *
 * @author dragomir
 */
public class ForbiddenResourceException extends BaseApiException {

    public ForbiddenResourceException() {
        super(403);
    }

    public ForbiddenResourceException(String message) {
        super(403, message);
    }

    public ForbiddenResourceException(String message, Throwable cause) {
        super(403, message, cause);
    }

}
