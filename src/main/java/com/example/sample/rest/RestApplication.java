package com.example.sample.rest;


import com.example.sample.rest.services.SampleResource;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

public class RestApplication extends Application {

    private final Set<Object> singletons;

    public RestApplication() throws IOException {
        Set<Object> singletons = new HashSet<>();
        singletons.add(new SampleResource());
        this.singletons = Collections.unmodifiableSet(singletons);
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }
}
