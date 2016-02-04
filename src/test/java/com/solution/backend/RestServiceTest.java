/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solution.backend;

import com.solution.backend.exceptions.BadRequestException;
import com.solution.backend.exceptions.UnauthorizedException;
import com.solution.backend.params.LoginParameters;
import com.solution.backend.responses.BackendResponse;
import com.solution.backend.responses.HomeResponse;
import com.solution.backend.responses.LoginResponse;
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

    private static final String HELLO_JOHN_DOE = "Hello John Doe";
    private static final String JOHN_DOE_JWT = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ";
    private static final String JOHN_SMITH_JWT = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJvdGhlclVzZXIiLCJuYW1lIjoiSm9obiBTbWl0aCIsImFkbWluIjpmYWxzZX0.8mNbBMTEoM0bB1OX06xSx2AYzOznf7IeX2b9BwRpfR8";

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
        String username = "user", pass = "pass";
        Mockito.when(backend.login(username, pass)).thenReturn(new BackendResponse(200, null, null));
        LoginParameters params = makeLoginParameters(username, pass);
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
        Mockito.when(backend.login(null, "asd")).thenThrow(new BadRequestException());
        Response login = restService.login(makeLoginParameters(null, "asd"));
        assertEquals(400, login.getStatus());
    }

    @Test
    public void testSuccessfulHome() {
        Mockito.when(backend.home(JOHN_DOE_JWT)).thenReturn(new BackendResponse(200, null, new HomeResponse(HELLO_JOHN_DOE)));
        BackendResponse<HomeResponse> home = backend.home(JOHN_DOE_JWT);
        assertEquals(HELLO_JOHN_DOE, home.getData().getMessage());
    }

    @Test
    public void testBadUsernamePassword() {
        String username = "BadUser", pass = "pass123";
        Mockito.when(backend.login(username, pass)).thenThrow(new UnauthorizedException());
        Response login = restService.login(makeLoginParameters(username, pass));
        assertEquals(401, login.getStatus());
        Mockito.verify(backend, Mockito.times(1)).login(username, pass);
    }

}
