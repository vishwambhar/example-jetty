package com.example.sample.rest;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.servlets.MetricsServlet;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Starts Jetty server on PORT 4080.
 */
public class Server {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);
    private static final JmxReporter jmxReporter = JmxReporter.forRegistry(MetricsReporter.getInstance()).build();
    private static final ConsoleReporter consoleReporter =
        ConsoleReporter.forRegistry(MetricsReporter.getInstance()).build();

    private static org.eclipse.jetty.server.Server server = null;

    private static final int PORT = 4080;

    public static void startServer() throws Exception {
        HttpServletDispatcher dispatcher = new HttpServletDispatcher();
        ServletHolder holder = new ServletHolder(dispatcher);
        holder.setInitParameter("javax.ws.rs.Application", RestApplication.class.getName());

        ServletContextHandler servletContextHandler = new ServletContextHandler();
        servletContextHandler.setInitParameter("resteasy.servlet.mapping.prefix", "/");
        servletContextHandler.addServlet(holder, "/*");

        // Add Dropwizard HTTP MetricsServlet
        servletContextHandler.addServlet(MetricsServlet.class, "/metrics");

        servletContextHandler.addEventListener(new MetricsServlet.ContextListener() {
            @Override
            protected MetricRegistry getMetricRegistry() {
                return MetricsReporter.getInstance();
            }

            @Override
            protected TimeUnit getRateUnit() {
                return TimeUnit.SECONDS;
            }

            @Override
            protected TimeUnit getDurationUnit() {
                return TimeUnit.MILLISECONDS;
            }
        });

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{servletContextHandler});

        server = new org.eclipse.jetty.server.Server(PORT);
        server.setHandler(handlers);

        server.start();
        logger.info("Listening on PORT " + PORT);

        // Dropwizard JMX Reporter
        jmxReporter.start();
        logger.info("Started reporter " + jmxReporter.getClass().getName());

        // Dropwizard Console Reporter
        consoleReporter.start(1, TimeUnit.MINUTES); // should expose values every minute
        logger.info("Started reporter " + consoleReporter.getClass().getName());
    }

    public static void stopServer() throws Exception {
        if (server != null) {
            server.stop();
        }

        server = null;
    }

    public static boolean isServerRunning() {
        return server != null;
    }

    public static void main(String... args) throws Exception {
        startServer();
    }
}
