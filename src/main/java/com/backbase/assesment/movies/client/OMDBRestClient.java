package com.backbase.assesment.movies.client;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * OMDB Rest client to retrieve movie details
 *
 * @author Akhtar
 */
@Service
public class OMDBRestClient {

    @Value("${omdb.api.url}")
    private String omdbURL;
    @Value("${omdb.api.key}")
    private String omdbKey;

    private final RestTemplate restTemplate;

    /**
     * Constructor OMDB rest client
     *
     * @param restTemplate to call API
     */
    public OMDBRestClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Retrieve OMDB movie details based on parameter type
     *
     * @param parameterType  to query with either title or id
     * @param parameterValue value for parameter type
     * @return OMDB movie details
     */
    public OMDB retrieveOmdbData(String parameterType, String parameterValue) {
        URI uri = UriComponentsBuilder.fromHttpUrl(omdbURL)
            .queryParam(parameterType, parameterValue)
            .queryParam("type", "movie")
            .queryParam("apikey", omdbKey)
            .build().toUri();
        return restTemplate.getForObject(uri, OMDB.class);
    }
}
