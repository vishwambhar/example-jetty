package com.example.sample.rest;

import com.codahale.metrics.MetricRegistry;

/**
 * Dropwizard Metrics Reporter.
 */
public class MetricsReporter extends MetricRegistry {

    private static final MetricsReporter metricsReporter = new MetricsReporter();

    private MetricsReporter() {
    }

    public static MetricsReporter getInstance() {
        return metricsReporter;
    }
}
