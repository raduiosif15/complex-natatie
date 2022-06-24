package com.example.complexnatatie.services;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class EmbeddedServletContainerConfig implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        // local
//        factory.setDocumentRoot(new File("/private/var/folders/k7/bjstfnxj7xg3709mh0pjv7br0000gn/T/tomcat-docbase.8080.6706630708292657697/"));
//        server
        factory.setDocumentRoot(new File("/tmp/tomcat-docbase.8080.6706630708292657697/"));

    }

}
