package com.solution.backend;

import com.solution.backend.responses.LoginResponse;

/**
 *
 * @author dragomir
 */
public interface LoginService {

    public LoginResponse login(String username, String password);

}
