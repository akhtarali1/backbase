package com.backbase.assesment.movies;

import java.time.Duration;

import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import io.swagger.v3.oas.models.parameters.Parameter;

/**
 * Backbase movie application main class
 *
 * @author Akhtar
 */
@SpringBootApplication
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

    /**
     * Bean to add default header of API key for Swagger-UI
     *
     * @return customizable operation
     */
    @Bean
    public OperationCustomizer customize() {
        return (operation, handlerMethod) -> operation.addParametersItem(
            new Parameter()
                .in("header")
                .required(true)
                .description("API key")
                .name("X-API-KEY"));
    }

}
