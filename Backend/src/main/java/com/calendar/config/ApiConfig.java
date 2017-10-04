package com.calendar.config;


import com.calendar.endpoints.SampleEndpoint;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by valishah on 2017-08-31.
 */
public class ApiConfig extends Application{

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        // Configure endpoints
        classes.add(SampleEndpoint.class);
        return classes;
    }

    @Override
    public Set<Object> getSingletons() {
        Set<Object> singletons = new HashSet<>();
        // Configure singletons
        return singletons;
    }

}
