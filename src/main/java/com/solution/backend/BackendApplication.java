package com.solution.backend;

import com.solution.backend.endpoints.impl.RestServiceImpl;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 *
 * @author dragomir
 */
@ApplicationPath("/rest")
public class BackendApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        resources.add(RestServiceImpl.class);
        return resources;
    }
}
