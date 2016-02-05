package com.solution.backend.endpoints.impl;

import com.solution.backend.endpoints.LoginService;
import com.solution.backend.exceptions.BadRequestException;
import com.solution.backend.exceptions.ForbiddenResourceException;
import com.solution.backend.exceptions.InternalServerErrorException;
import com.solution.backend.exceptions.UnauthorizedException;
import com.solution.backend.responses.LoginResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.ServiceUnavailableException;
import org.apache.http.HttpResponse;
import static org.apache.http.HttpStatus.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class LoginServiceEndpoint implements LoginService {

    // this should go to properties file
    private static final String LOGIN_URI = "http://localhost:8080/login-service/rest/private/login";

    private final HttpClient httpClient;

    public LoginServiceEndpoint() {
        httpClient = HttpClientBuilder.create().build();
    }

    public LoginServiceEndpoint(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public LoginResponse login(String username, String password) {
        try {
            String rawString = username + ':' + password;
            String base = Base64.getEncoder().encodeToString(rawString.getBytes());
            HttpResponse response = httpClient.execute(makePostLogin(base));
            String entity = EntityUtils.toString(response.getEntity());

            return makeResponse(response, entity);
        } catch (IOException ex) {
            Logger.getLogger(LoginServiceEndpoint.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServiceUnavailableException(ex.getMessage());
        }
    }

    private LoginResponse makeResponse(HttpResponse response, String entity) {
        switch (response.getStatusLine().getStatusCode()) {
            case SC_OK:
                return new LoginResponse(entity);
            case SC_BAD_REQUEST:
                throw new BadRequestException();
            case SC_UNAUTHORIZED:
                throw new UnauthorizedException();
            case SC_FORBIDDEN:
                throw new ForbiddenResourceException();
            case SC_SERVICE_UNAVAILABLE:
                throw new ServiceUnavailableException();
            default:
                throw new InternalServerErrorException();
        }
    }

    private HttpPost makePostLogin(String base) throws UnsupportedEncodingException {
        HttpPost post = new HttpPost(LOGIN_URI);
        post.setEntity(new StringEntity(""));
        post.addHeader("Base", base);
        return post;
    }

}
