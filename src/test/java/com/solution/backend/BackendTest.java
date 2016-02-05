package com.solution.backend;

import com.solution.backend.endpoints.LoginService;
import com.solution.backend.services.Backend;
import com.solution.backend.exceptions.BadRequestException;
import com.solution.backend.exceptions.UnauthorizedException;
import com.solution.backend.responses.BackendResponse;
import com.solution.backend.responses.LoginResponse;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 *
 * @author dragomir
 */
@RunWith(MockitoJUnitRunner.class)
public class BackendTest {

    @Mock
    private LoginService endpoint;

    private Backend backend;

    public BackendTest() {
    }

    @Before
    public void setUp() {
        backend = new Backend(endpoint);
    }

    @Test
    public void testOkLogin() {
        Mockito.when(endpoint.login(Constants.GOOD_USER_NAME, Constants.GOOD_PASSWORD)).thenReturn(new LoginResponse(Constants.JOHN_DOE_JWT));
        BackendResponse<LoginResponse> login = backend.login(Constants.GOOD_USER_NAME, Constants.GOOD_PASSWORD);
        assertEquals(200, login.getStatus());
        assertEquals(Constants.JOHN_DOE_JWT, login.getData().getToken());
        Mockito.verify(endpoint, Mockito.times(1)).login(Constants.GOOD_USER_NAME, Constants.GOOD_PASSWORD);
    }

    @Test(expected = BadRequestException.class)
    public void testNullOrEmptyPassword() {
        backend.login(Constants.GOOD_USER_NAME, null);
    }

    @Test(expected = BadRequestException.class)
    public void testNullOrEmptyUsername() {
        backend.login("", Constants.GOOD_PASSWORD);
    }

    @Test(expected = UnauthorizedException.class)
    public void testBadLogin() {
        Mockito.when(endpoint.login(Constants.NOT_EXIST_USER_NAME, Constants.NOT_EXIST_USER_PASSWORD)).thenThrow(UnauthorizedException.class);
        assertEquals(401, backend.login(Constants.NOT_EXIST_USER_NAME, Constants.NOT_EXIST_USER_PASSWORD).getStatus());
        Mockito.verify(endpoint, Mockito.times(1)).login(Constants.NOT_EXIST_USER_NAME, Constants.NOT_EXIST_USER_PASSWORD);
    }

    @Test
    public void testOkHome() {
        Mockito.when(endpoint.login(Constants.GOOD_USER_NAME, Constants.GOOD_PASSWORD)).thenReturn(new LoginResponse(Constants.JOHN_DOE_JWT));
        backend.login(Constants.GOOD_USER_NAME, Constants.GOOD_PASSWORD);
        BackendResponse home = backend.home(Constants.JOHN_DOE_JWT);
        assertEquals(200, home.getStatus());
    }

    @Test(expected = UnauthorizedException.class)
    public void testBadHome() {
        Mockito.when(endpoint.login(Constants.GOOD_USER_NAME, Constants.GOOD_PASSWORD)).thenReturn(new LoginResponse(Constants.JOHN_DOE_JWT));
        backend.login(Constants.GOOD_USER_NAME, Constants.GOOD_PASSWORD);
        backend.home(Constants.JOHN_SMITH_JWT);
    }

    @Test(expected = BadRequestException.class)
    public void testHomeNoJwt() {
        BackendResponse home = backend.home(null);
        assertEquals(400, home.getStatus());
    }

    @Test(expected = UnauthorizedException.class)
    public void testLogout() {
        Mockito.when(endpoint.login(Constants.GOOD_USER_NAME, Constants.GOOD_PASSWORD)).thenReturn(new LoginResponse(Constants.JOHN_DOE_JWT));
        backend.login(Constants.GOOD_USER_NAME, Constants.GOOD_PASSWORD);
        backend.logout(Constants.JOHN_DOE_JWT);
        backend.home(Constants.JOHN_DOE_JWT);
    }
}
