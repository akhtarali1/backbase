package com.backbase.assesment.movies;

import java.time.Duration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.web.client.RestTemplate;

/**
 * Backbase movie application main class
 *
 * @author Akhtar
 */
@SpringBootApplication
@EnableAuthorizationServer
public class BackbaseMoviesApplication {

    /**
     * Main method for application startup
     *
     * @param args application runtime arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(BackbaseMoviesApplication.class, args);
    }

    /**
     * Rest template bean creation
     *
     * @param restBuilder Rest template builder
     * @return rest template
     */
    @Bean
    public RestTemplate getRestTemplate(RestTemplateBuilder restBuilder) {
        return restBuilder
            .setReadTimeout(Duration.ofSeconds(5))
            .setConnectTimeout(Duration.ofSeconds(3))
            .build();
    }

}
