package com.codejam;

import com.bedatadriven.jackson.datatype.jts.JtsModule;
import com.codejam.controller.CustomerResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * The spring boot application class, that starts the app.
 */
@SpringBootApplication
public class SpatialJpaApplication extends ResourceConfig {

    @Bean
    public JtsModule jtsModule() {
        return new JtsModule();
    }

    public SpatialJpaApplication() {
        register(CustomerResource.class);
    }

    /**
     * Main method that starts the Spring Boot application.
     *
     * @param args Arguments passed to the app.
     */
    public static void main(String[] args) {
        SpringApplication.run(SpatialJpaApplication.class, args);
    }

}
