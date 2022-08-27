package com.backbase.assesment.movies.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * OAuth configuration
 *
 * @author Akhtar
 */
@Configuration
@EnableResourceServer
public class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {

    /**
     * Override configure method to validate endpoints security check
     *
     * @param http to check endpoints security
     * @throws Exception exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers("/backbase/movies/**").authenticated()
            .antMatchers("/").permitAll();
    }
}
