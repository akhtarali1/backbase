package com.backbase.assesment.movies.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.backbase.assesment.movies.domain.MovieRating;
import com.backbase.assesment.movies.model.Rating;
import com.backbase.assesment.movies.persistance.entity.UserRating;
import com.backbase.assesment.movies.persistance.repository.UserRatingRepository;
import com.backbase.assesment.movies.service.mapper.MoviesRatingMapper;

@ExtendWith(MockitoExtension.class)
class RatingServiceTest {

    @Mock private UserRatingRepository ratingRepository;
    private RatingService ratingService;

    @BeforeEach
    void setUp() {
        MoviesRatingMapper mapper = new MoviesRatingMapper();
        ratingService = new RatingService(ratingRepository, mapper);
    }

    @Test
    @DisplayName("Save user posted rating with logged-in user")
    void saveUserRating() {
        when(ratingRepository.findByUserIdAndMovieId(anyString(), anyString()))
            .thenReturn(Optional.of(UserRating.builder().build()));
        when(ratingRepository.save(any())).thenReturn(userRating());
        when(ratingRepository.getRatingCountAndAverage(anyString())).thenReturn(movieRating());

        Rating rating = ratingService.saveUserRating(rating(), "t123", "u1");
        assertEquals(new BigDecimal("9.50"), rating.getMovieRating());
        assertEquals(123, rating.getVotes());
        assertEquals("Super Movie", rating.getComments());
        assertEquals(9, rating.getUserRating());
    }

    @Test
    @DisplayName("Save user posted rating as anonymous")
    void saveUserRatingWithAnonymousUser() {
        when(ratingRepository.save(any())).thenReturn(userRating());
        when(ratingRepository.getRatingCountAndAverage(anyString())).thenReturn(movieRating());

        Rating rating = ratingService.saveUserRating(rating(), "t123", null);
        assertEquals(new BigDecimal("9.50"), rating.getMovieRating());
        assertEquals(123, rating.getVotes());
        assertEquals("Super Movie", rating.getComments());
        assertEquals(9, rating.getUserRating());
    }

    private Rating rating() {
        return Rating.builder()
            .comments("Super Movie")
            .userRating(10L)
            .build();
    }

    private Optional<MovieRating> movieRating() {
        return Optional.of(new MovieRating("t123", 9.5, 123L));
    }
    private UserRating userRating() {
        return UserRating.builder()
            .rating(9L)
            .id(1L)
            .movieId("t123")
            .comment("Super Movie")
            .build();
    }
}