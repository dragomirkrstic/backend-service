/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solution.backend;

import com.solution.backend.services.Backend;
import com.solution.backend.endpoints.impl.RestServiceImpl;
import com.solution.backend.exceptions.BadRequestException;
import com.solution.backend.exceptions.UnauthorizedException;
import com.solution.backend.params.LoginParameters;
import com.solution.backend.responses.BackendResponse;
import com.solution.backend.responses.HomeResponse;
import javax.ws.rs.core.Response;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 *
 * @author dragomir
 */
@RunWith(MockitoJUnitRunner.class)
public class RestServiceTest {

    private RestServiceImpl restService;

    @Mock
    private Backend backend;

    public RestServiceTest() {
    }

    @Before
    public void setUp() {
        restService = new RestServiceImpl(backend);
    }

    @Test
    public void testSuccessfulLogin() {
        Mockito.when(backend.login(Constants.GOOD_USER_NAME, Constants.GOOD_PASSWORD)).thenReturn(new BackendResponse(200, null, null));
        LoginParameters params = makeLoginParameters(Constants.GOOD_USER_NAME, Constants.GOOD_PASSWORD);
        Response login = restService.login(params);
        assertEquals(200, login.getStatus());
    }

    private LoginParameters makeLoginParameters(String username, String pass) {
        LoginParameters params = new LoginParameters();
        params.setUsername(username);
        params.setPassword(pass);
        return params;
    }

    @Test
    public void testNullUserOrPassword() {
        Mockito.when(backend.login(null, Constants.GOOD_PASSWORD)).thenThrow(new BadRequestException());
        Response login = restService.login(makeLoginParameters(null, Constants.GOOD_PASSWORD));
        assertEquals(400, login.getStatus());
    }

    @Test
    public void testSuccessfulHome() {
        Mockito.when(backend.home(null)).thenReturn(new BackendResponse(200, null, new HomeResponse(Constants.HELLO_JOHN_DOE_MESSAGE)));
        Response home = restService.home();
        assertEquals(200, home.getStatus());
    }

    @Test
    public void testBadUsernamePassword() {
        Mockito.when(backend.login(Constants.BAD_USER_NAME, Constants.BAD_USER_PASSWORD)).thenThrow(new UnauthorizedException());
        Response login = restService.login(makeLoginParameters(Constants.BAD_USER_NAME, Constants.BAD_USER_PASSWORD));
        assertEquals(401, login.getStatus());
        Mockito.verify(backend, Mockito.times(1)).login(Constants.BAD_USER_NAME, Constants.BAD_USER_PASSWORD);
    }

    @Test
    public void testLogout() {
        Mockito.doNothing().when(backend).logout(null);
        Response logout = restService.logout();
        assertEquals(204, logout.getStatus());
    }
}
