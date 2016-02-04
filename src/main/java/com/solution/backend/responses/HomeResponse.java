package com.solution.backend.responses;

/**
 *
 * @author dragomir
 */
public class HomeResponse {

    private final String message;

    public HomeResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
