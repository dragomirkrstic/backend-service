package com.solution.backend.endpoints;

import com.solution.backend.responses.LoginResponse;

/**
 * Interface for external login service
 * @author dragomir
 */
public interface LoginService {

    /**
     * Call external service to login the user
     * @param username String
     * @param password String
     * @return LoginResponse containing the JWT
     * @throws BadRequestException
     * @throws UnauthorizedException
     * @throws ServiceUnavailableException
     * @throws ForbiddenResourceException
     * @see LoginResponse
     */
    public LoginResponse login(String username, String password);

}
