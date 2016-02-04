package com.solution.backend.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author dragomir
 */
public class LoginResponse {

    private String token;

    public LoginResponse(String token) {
        this.token = token;
    }

    @JsonProperty("auth_token")
    public String getToken() {
        return token;
    }
}
