package com.solution.backend;

import com.solution.backend.exceptions.BadRequestException;
import com.solution.backend.exceptions.UnauthorizedException;
import com.solution.backend.impl.LoginServiceEndpoint;
import com.solution.backend.responses.BackendResponse;
import com.solution.backend.responses.HomeResponse;
import com.solution.backend.responses.LoginResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author dragomir
 */
public class Backend {

    private final Map<String, String> loggedUsers;

    private final LoginService loginEndpoint;

    public Backend() {
        loginEndpoint = new LoginServiceEndpoint();
        loggedUsers = new HashMap<>();
    }

    public Backend(LoginService loginService) {
        this.loginEndpoint = loginService;
        loggedUsers = new HashMap<>();
    }

    public BackendResponse<LoginResponse> login(String username, String password) {
        if (!areValidCredentials(username, password)) {
            throw new BadRequestException("You must provide username and password.");
        }
//        LoginResponse login = loginEndpoint.login(username, password);
        final String JOHN_DOE_JWT = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ";

        LoginResponse login = new LoginResponse(JOHN_DOE_JWT);
        System.out.println("### LOGIN GOOD");
        loggedUsers.put(username, (String) login.getToken());
        return new BackendResponse(200, null, login);
    }

    public BackendResponse<HomeResponse> home(String jwt) {
        if (isNullOrEmpty(jwt)) {
            throw new BadRequestException("You must provide the JWT.");
        }
        String key = Base64.getEncoder().encodeToString("secret".getBytes());
        Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(jwt).getBody();
        String subject = claims.getSubject();
        String userToken = loggedUsers.get(subject);
        if (!jwt.equals(userToken)) {
            throw new UnauthorizedException();
        }
        return makeResponse(claims);
    }

    private BackendResponse<HomeResponse> makeResponse(Claims claims) {
        HomeResponse homeResponse = new HomeResponse("Hello " + claims.get("name"));
        return new BackendResponse(200, null, homeResponse);
    }

    private boolean areValidCredentials(String username, String password) {
        return !isNullOrEmpty(username) && !isNullOrEmpty(password);
    }

    private boolean isNullOrEmpty(String username) {
        return username == null || username.isEmpty();
    }
}
