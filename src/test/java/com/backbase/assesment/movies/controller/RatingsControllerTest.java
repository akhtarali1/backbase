package com.backbase.assesment.movies.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.sql.SQLException;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.web.servlet.MockMvc;

import com.backbase.assesment.movies.model.Rating;
import com.backbase.assesment.movies.service.RatingService;

@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureWebClient
@WebMvcTest(value = RatingsController.class)
class RatingsControllerTest {

    private static final String URL = "/backbase/movies";

    @Autowired private MockMvc mockMvc;
    @MockBean private RatingService ratingService;

    @Test
    @DisplayName("Post user ratings to DB")
    void postRatings() throws Exception {
        given(ratingService.saveUserRating(any(), anyString(), isNull())).willReturn(rating());
        mockMvc.perform(post(URL + "/t123/rating")
                .contentType(APPLICATION_JSON)
                .content("{\"userRating\":9,\"comments\":\"super movie\"}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.userRating").value(9))
            .andExpect(jsonPath("$.comments").value("super movie"))
            .andExpect(jsonPath("$.movieRating").value(9.52))
            .andExpect(jsonPath("$.votes").value(1234));
    }

    @Test
    @DisplayName("Post user rating to DB when incorrect data sent")
    void postRatingsWithDBError() throws Exception {
        given(ratingService.saveUserRating(any(), anyString(), isNull())).willThrow(formDataIntegrityViolationException());
        mockMvc.perform(post(URL + "/t123/rating")
                .contentType(APPLICATION_JSON)
                .content("{\"userRating\":9,\"comments\":\"super movie\"}"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("1000"))
            .andExpect(jsonPath("$.message").value("Unique Constraint Violation"));
    }

    private Rating rating() {
        return Rating.builder()
            .userRating(9L)
            .comments("super movie")
            .movieRating(BigDecimal.valueOf(9.52))
            .votes(1234L)
            .build();
    }

    private DataIntegrityViolationException formDataIntegrityViolationException() {
        return new DataIntegrityViolationException("Violation",
            new ConstraintViolationException("Violation",
                new SQLException("Exception: SQL: Unique Constraint Violation"), "Violation"));
    }
}