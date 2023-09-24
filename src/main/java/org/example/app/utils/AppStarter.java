package org.example.app.utils;

import jakarta.ws.rs.core.UriBuilder;
import org.glassfish.jersey.netty.httpserver.NettyHttpContainerProvider;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.MalformedURLException;
import java.net.URI;

public final class AppStarter {
    public static void runApp() throws MalformedURLException {
        URI baseUri = UriBuilder.fromUri(Constants.URI_TEMPLATE).port(Constants.PORT).build();
        ResourceConfig resourceConfig = new ResourceConfig().packages(Constants.PACKS);
        NettyHttpContainerProvider.createServer(baseUri, resourceConfig, false);
        System.out.printf("Application running on %s\n", baseUri.toURL().toExternalForm());
    }
}
