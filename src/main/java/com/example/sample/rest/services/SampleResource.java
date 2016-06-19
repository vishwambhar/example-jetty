package com.example.sample.rest.services;

import com.codahale.metrics.MetricRegistry;
import com.example.sample.rest.MetricsReporter;

import java.util.concurrent.TimeUnit;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Defines Sample Rest API.
 *
 * curl http://localhost:4080/ping
 */

@Path("/")
public class SampleResource {

    private static final String CLICHED_MESSAGE = "Pong!";

    private com.codahale.metrics.Meter pingGet;

    private com.codahale.metrics.Timer pingResponseTimer;

    public SampleResource() {
        this.pingGet = MetricsReporter.getInstance().meter(MetricRegistry.name(SampleResource.class, "ping_get"));
        this.pingResponseTimer =
            MetricsReporter.getInstance().timer(MetricRegistry.name(SampleResource.class, "pig_get_time"));
    }

    @GET
    @Path("/ping")
    @Produces("text/plain")
    public String getPing() {
        long st = System.currentTimeMillis();
        pingGet.mark();
        long et = System.currentTimeMillis();
        pingResponseTimer.update(et - st, TimeUnit.MILLISECONDS);
        return CLICHED_MESSAGE;
    }
}
