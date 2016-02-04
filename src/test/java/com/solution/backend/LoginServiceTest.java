/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solution.backend;

import com.solution.backend.exceptions.BadRequestException;
import com.solution.backend.exceptions.ForbiddenResourceException;
import com.solution.backend.exceptions.UnauthorizedException;
import com.solution.backend.impl.LoginServiceEndpoint;
import com.solution.backend.responses.LoginResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 *
 * @author dragomir
 */
@RunWith(MockitoJUnitRunner.class)
public class LoginServiceTest {

    @Mock
    private HttpClient httpClient;

    private LoginServiceEndpoint endpoint;

    public LoginServiceTest() {
    }

    @Before
    public void setUp() {
        endpoint = new LoginServiceEndpoint(httpClient);
    }

    @Test
    public void testOkRequest() throws Exception {
        HttpResponse response = makeHttpResponse(200, Constants.JOHN_DOE_JWT);
        Mockito.when(httpClient.execute(Mockito.any(HttpPost.class))).thenReturn(response);
        LoginResponse login = endpoint.login(Constants.GOOD_USER_NAME, Constants.GOOD_PASSWORD);
        assertEquals(Constants.JOHN_DOE_JWT, login.getToken());
        Mockito.verify(httpClient, Mockito.times(1)).execute(Mockito.any(HttpPost.class));
    }

    @Test(expected = BadRequestException.class)
    public void testBadRequest() throws IOException {
        HttpResponse response = makeHttpResponse(400, "");
        Mockito.when(httpClient.execute(Mockito.any(HttpPost.class))).thenReturn(response);
        endpoint.login(Constants.BAD_USER_NAME, Constants.BAD_USER_PASSWORD);
    }

    @Test(expected = UnauthorizedException.class)
    public void testUnauthorizedRequest() throws IOException {
        HttpResponse response = makeHttpResponse(401, "");
        Mockito.when(httpClient.execute(Mockito.any(HttpPost.class))).thenReturn(response);
        endpoint.login(Constants.BAD_USER_NAME, Constants.BAD_USER_PASSWORD);
    }

    @Test(expected = ForbiddenResourceException.class)
    public void testForbiddenRequest() throws IOException {
        HttpResponse response = makeHttpResponse(403, "");
        Mockito.when(httpClient.execute(Mockito.any(HttpPost.class))).thenReturn(response);
        endpoint.login(Constants.BAD_USER_NAME, Constants.BAD_USER_PASSWORD);
    }

    private HttpResponse makeHttpResponse(int status, String body) {
        HttpResponse response = new BasicHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, status, null));
        BasicHttpEntity e = new BasicHttpEntity();
        e.setContent(new ByteArrayInputStream(body.getBytes()));
        response.setEntity(e);
        return response;
    }

}
