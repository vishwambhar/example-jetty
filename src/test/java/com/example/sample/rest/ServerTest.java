package com.example.sample.rest;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;

public class ServerTest {

    @Before
    public void setup() throws Exception {
        start();
    }

    @After
    public void tearDown() throws Exception {
        Server.stopServer();
    }

    @Test
    public void testGetPing() throws Exception {
        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target("http://localhost:4080/ping");
        Response response = target.request().get();
        String result = response.readEntity(String.class);
        Assert.assertEquals("Pong!", result);
        response.close();
    }

    @Test
    public void testServerStop() throws Exception {
        Server.stopServer();
        Assert.assertFalse(Server.isServerRunning());
    }

    private void start() throws Exception {
        Server.main();
    }
}