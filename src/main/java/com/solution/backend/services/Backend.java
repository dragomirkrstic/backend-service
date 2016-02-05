package com.solution.backend.services;

import com.solution.backend.endpoints.LoginService;
import com.solution.backend.exceptions.BadRequestException;
import com.solution.backend.exceptions.UnauthorizedException;
import com.solution.backend.endpoints.impl.LoginServiceEndpoint;
import com.solution.backend.responses.BackendResponse;
import com.solution.backend.responses.HomeResponse;
import com.solution.backend.responses.LoginResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author dragomir
 */
public class Backend {

    private final static Map<String, String> loggedUsers = new HashMap<>();

    private final LoginService loginEndpoint;

    public Backend() {
        loginEndpoint = new LoginServiceEndpoint();
    }

    public Backend(LoginService loginService) {
        this.loginEndpoint = loginService;
    }

    /**
     * Method to call to log in the user.
     *
     * @param username String
     * @param password String
     * @return BackendResponse object containing LoginResponse entity
     * @throws BadRequestException when username and/or password is null or
     * empty
     * @throws UnauthorizedException
     * @throws ForbiddenResourceException
     * @throws ServiceUnavailableException
     * @see BackendResponse
     * @see LoginResponse
     */
    public BackendResponse<LoginResponse> login(String username, String password) {
        if (!areValidCredentials(username, password)) {
            throw new BadRequestException("You must provide username and password.");
        }
        LoginResponse login = loginEndpoint.login(username, password);
        addUserToMap(username, (String) login.getToken());
        return new BackendResponse(200, null, login);
    }

    /**
     * Method to call for user's home
     *
     * @param jwt String - token representing the user
     * @return BackendResponse object containing HomeResponse entity
     * @throws BadRequestException if the JWT is missing
     * @throws UnauthorizedException if the user is not authorized for the
     * resource
     * @see BackendResponse
     * @see HomeResponse
     */
    public BackendResponse<HomeResponse> home(String jwt) {
        validateJWTString(jwt);
        Claims claims = parseClaimsFromJwt(jwt);
        String subject = claims.getSubject();
        String userToken = loggedUsers.get(subject);
        isTokenValidForUser(jwt, userToken);
        return makeResponse(claims);
    }

    private void isTokenValidForUser(String jwt, String userToken) throws UnauthorizedException {
        if (!jwt.equals(userToken)) {
            throw new UnauthorizedException();
        }
    }

    private void validateJWTString(String jwt) throws BadRequestException {
        if (isNullOrEmpty(jwt)) {
            throw new BadRequestException("You must provide the JWT.");
        }
    }

    /**
     * Method for logging out the user
     *
     * @param jwt String - token representing the user
     * @throws BadRequestException if the JWT is missing
     */
    public void logout(String jwt) {
        validateJWTString(jwt);
        Claims claims = parseClaimsFromJwt(jwt);
        String subject = claims.getSubject();
        String userToken = loggedUsers.get(subject);
        isTokenValidForUser(jwt, userToken);
        removeUserFromMap(subject);
    }

    private Claims parseClaimsFromJwt(String jwt) throws IllegalArgumentException, ExpiredJwtException, MalformedJwtException, SignatureException, UnsupportedJwtException {
        String key = Base64.getEncoder().encodeToString("secret".getBytes());
        Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(jwt).getBody();
        return claims;
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

    private void addUserToMap(String username, String JOHN_DOE_JWT) {
        synchronized (loggedUsers) {
            loggedUsers.put(username, JOHN_DOE_JWT);
        }
    }

    private void removeUserFromMap(String subject) {
        synchronized (loggedUsers) {
            loggedUsers.remove(subject);
        }
    }
}
