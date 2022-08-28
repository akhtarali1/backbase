package com.backbase.assesment.movies.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.backbase.assesment.movies.domain.MovieNotFoundException;
import com.backbase.assesment.movies.model.Movie;
import com.backbase.assesment.movies.service.MoviesService;

@WebMvcTest(value = MoviesController.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureWebClient
class MoviesControllerTest {

    private static final String URL = "/backbase/movies";

    @Autowired private MockMvc mockMvc;
    @MockBean private MoviesService moviesService;

    @Test
    @DisplayName("Get Movie details with any Academy Awards won")
    void getMovieWithAwards() throws Exception {
        given(moviesService.retrieveMovieDetails(anyString())).willReturn(getMovie());
        mockMvc.perform(get(URL)
                .contentType(APPLICATION_JSON)
                .param("title", "King's speech"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value("t123"))
            .andExpect(jsonPath("$.year").value("2001"))
            .andExpect(jsonPath("$.rating").value(9))
            .andExpect(jsonPath("$.votes").value(100))
            .andExpect(jsonPath("$.boxOfficeValue").value("$3000"))
            .andExpect(jsonPath("$.awards").isArray())
            .andExpect(jsonPath("$._links.rating.type").value("POST"))
            .andExpect(jsonPath("$._links.rating.href").hasJsonPath())
            .andExpect(jsonPath("$.boxOfficeValueAsNumber").doesNotHaveJsonPath());
    }

    @Test
    @DisplayName("Get movie details when movie not found in OMDB API response")
    void getMovieWithAwardsWhenNoMovieFound() throws Exception {
        given(moviesService.retrieveMovieDetails(anyString())).willThrow(new MovieNotFoundException("king"));
        mockMvc.perform(get(URL)
                .contentType(APPLICATION_JSON)
                .param("title", "King's speech"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("1000"))
            .andExpect(jsonPath("$.message").value("Requested Movie: king is not found. Please Check"));
    }

    @Test
    @DisplayName("Get Top rated movies sorted based on box office value")
    void getTopMovies() throws Exception {
        given(moviesService.getTopMovies(10)).willReturn(List.of(getMovie()));
        mockMvc.perform(get(URL + "/topMovies")
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[0].id").value("t123"))
            .andExpect(jsonPath("$.[0].year").value("2001"))
            .andExpect(jsonPath("$.[0].rating").value(9))
            .andExpect(jsonPath("$.[0].votes").value(100))
            .andExpect(jsonPath("$.[0].boxOfficeValue").value("$3000"))
            .andExpect(jsonPath("$.[0].awards").isArray())
            .andExpect(jsonPath("$.[0].links.[0].rel").value("rating"))
            .andExpect(jsonPath("$.[0].links.[0].type").value("POST"))
            .andExpect(jsonPath("$.[0].links.[0].href").hasJsonPath())
            .andExpect(jsonPath("$.[0].boxOfficeValueAsNumber").doesNotHaveJsonPath());
    }

    private Movie getMovie() {
        return Movie.builder()
            .id("t123")
            .year("2001")
            .boxOfficeValue("$3000")
            .rating(BigDecimal.valueOf(9))
            .votes(100L)
            .awards(List.of("Best Picture"))
            .build();
    }
}