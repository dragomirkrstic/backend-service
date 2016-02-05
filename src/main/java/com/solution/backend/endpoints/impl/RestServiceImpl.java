package com.solution.backend.endpoints.impl;

import com.solution.backend.services.Backend;
import com.solution.backend.endpoints.BackendEndpoint;
import com.solution.backend.responses.BackendResponse;
import com.solution.backend.exceptions.BadRequestException;
import com.solution.backend.exceptions.ForbiddenResourceException;
import com.solution.backend.exceptions.InternalServerErrorException;
import com.solution.backend.exceptions.UnauthorizedException;
import com.solution.backend.params.LoginParameters;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

/**
 *
 * @author dragomir
 */
public class RestServiceImpl implements BackendEndpoint {

    private final Backend backend;

    @Context
    private HttpHeaders headers;

    public RestServiceImpl() {
        backend = new Backend();
    }

    public RestServiceImpl(Backend backend) {
        this.backend = backend;
    }

    @Override
    public Response login(LoginParameters parameters) {
        try {
            BackendResponse login = backend.login(parameters.getUsername(), parameters.getPassword());
            return Response.ok(login).build();
        } catch (BadRequestException | UnauthorizedException | ForbiddenResourceException | InternalServerErrorException ex) {
            return Response.status(ex.getStatus()).entity(
                    new BackendResponse(ex.getStatus(), ex.getMessage(), null))
                    .build();
        }
    }

    @Override
    public Response home() {
        try {
            String auth = null;
            if (headers != null) {
                auth = headers.getHeaderString("auth_token");
            }
            BackendResponse home = backend.home(auth);
            return Response.ok(home.getStatus()).entity(home).build();
        } catch (BadRequestException | UnauthorizedException | ForbiddenResourceException ex) {
            return Response.status(ex.getStatus()).entity(
                    new BackendResponse(ex.getStatus(), ex.getMessage(), null))
                    .build();
        }
    }

    @Override
    public Response logout() {
        try {
            String auth = null;
            if (headers != null) {
                auth = headers.getHeaderString("auth_token");
            }
            backend.logout(auth);
            return Response.noContent().build();
        } catch (BadRequestException | UnauthorizedException | ForbiddenResourceException ex) {
            return Response.status(ex.getStatus()).entity(
                    new BackendResponse(ex.getStatus(), ex.getMessage(), null))
                    .build();
        }
    }
}
