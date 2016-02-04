package com.solution.backend;

import com.solution.backend.exceptions.BadRequestException;
import com.solution.backend.exceptions.UnauthorizedException;
import com.solution.backend.responses.BackendResponse;
import com.solution.backend.responses.LoginResponse;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.Ignore;
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

    private static final String JOHN_DOE_JWT = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ";
    private static final String JOHN_SMITH_JWT = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJvdGhlclVzZXIiLCJuYW1lIjoiSm9obiBTbWl0aCIsImFkbWluIjpmYWxzZX0.8mNbBMTEoM0bB1OX06xSx2AYzOznf7IeX2b9BwRpfR8";

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
    @Ignore
    public void testOkLogin() {
        String username = "GoodUser", pass = "arbitraryElephantConflict";
        Mockito.when(endpoint.login(username, pass)).thenReturn(new LoginResponse(JOHN_DOE_JWT));
        BackendResponse<LoginResponse> login = backend.login(username, pass);
        assertEquals(200, login.getStatus());
        assertEquals(JOHN_DOE_JWT, login.getData().getToken());
        Mockito.verify(endpoint, Mockito.times(1)).login(username, pass);
    }

    @Test(expected = BadRequestException.class)
    public void testNullOrEmptyPassword() {
        String username = "user", pass = null;
        backend.login(username, pass);
//        assertEquals(400, login.getStatus());
//        Mockito.verify(endpoint, Mockito.times(0)).login(username, pass);
    }

    @Test(expected = BadRequestException.class)
    public void testNullOrEmptyUsername() {
        String username = "", pass = "nic3Pass!Sword";
        backend.login(username, pass);
//        assertEquals(400, login.getStatus());
//        Mockito.verify(endpoint, Mockito.times(0)).login(username, pass);
    }

    @Test(expected = UnauthorizedException.class)
    @Ignore
    public void testBadLogin() {
        String username = "NonExisting", pass = "pass123";
        Mockito.when(endpoint.login(username, pass)).thenThrow(UnauthorizedException.class);
        assertEquals(401, backend.login(username, pass).getStatus());
        Mockito.verify(endpoint, Mockito.times(1)).login(username, pass);
    }

    @Test
    public void testOkHome() {
        Mockito.when(endpoint.login("1234567890", "pass")).thenReturn(new LoginResponse(JOHN_DOE_JWT));
        backend.login("1234567890", "pass");
        BackendResponse home = backend.home(JOHN_DOE_JWT);
        assertEquals(200, home.getStatus());
    }

    @Test(expected = UnauthorizedException.class)
    public void testBadHome() {
        Mockito.when(endpoint.login("otherUser", "pass")).thenReturn(new LoginResponse(JOHN_DOE_JWT));
        backend.login("otherUser", "pass");
        backend.home(JOHN_SMITH_JWT);
    }

    @Test(expected = BadRequestException.class)
    public void testHomeNoJwt() {
        BackendResponse home = backend.home(null);
        assertEquals(400, home.getStatus());
    }
}
