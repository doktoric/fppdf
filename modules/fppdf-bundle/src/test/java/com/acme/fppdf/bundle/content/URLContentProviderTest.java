package com.acme.fppdf.bundle.content;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acme.fppdf.bundle.content.URLContentProvider;

public class URLContentProviderTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(URLContentProviderTest.class);
    
    private static final String PORT_PROPERTY_NAME = URLContentProviderTest.class.getName() + ".httpPort";
    private static final String TEST_CONTENT = "/TEST_CONTENT";
    private static final int DEFAULT_PORT = 9090;
    private static final int PORT = Integer.valueOf(System.getProperty(PORT_PROPERTY_NAME, String.valueOf(DEFAULT_PORT)));
    private static final String URL_FORMAT = "http://localhost:%d%s";

    private static Server server;

    private URLContentProvider target;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        LOGGER.info("Server port: " + PORT);
        LOGGER.info("In case of collision change it via property '" + PORT_PROPERTY_NAME + "'");
        server = new Server(PORT);
        server.setStopAtShutdown(true);
        server.setHandler(
                new AbstractHandler() {
                    @Override
                    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
                        throws IOException, ServletException {
                        if (request.getRequestURL().toString().endsWith(TEST_CONTENT)) {
                            response.setContentType("text/plain;charset=utf-8");
                            response.setStatus(HttpServletResponse.SC_OK);
                            baseRequest.setHandled(true);
                            response.getWriter().println(TEST_CONTENT);
                        } else {
                            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                            baseRequest.setHandled(true);
                            response.getWriter().println("NOT FOUND");
                        }
                    }
                });
        server.start();
        Thread.sleep(5000);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        server.stop();
    }

    @Before
    public void setUp() {
        target = new URLContentProvider();
    }

    @After
    public void tearDown() {
        target = null;
    }

    @Test
    public void testReadURLContentOK() throws Exception {
        final String expectedOk = TEST_CONTENT;

        assertEquals(
                expectedOk,
                new String(
                        target.readURLContent(
                                new URL(String.format(URL_FORMAT, PORT, TEST_CONTENT))),
                        "UTF-8")
                        .trim());
    }

    @Test(expected = IOException.class)
    public void testReadURLContentNotOK() throws Exception {
        target.readURLContent(new URL(String.format(URL_FORMAT, PORT, "/SomethingElse")));
    }
}
