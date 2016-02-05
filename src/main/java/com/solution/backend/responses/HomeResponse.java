package com.solution.backend.responses;

import java.io.Serializable;

/**
 *
 * @author dragomir
 */
public class HomeResponse implements Serializable {

    private final String message;

    public HomeResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
