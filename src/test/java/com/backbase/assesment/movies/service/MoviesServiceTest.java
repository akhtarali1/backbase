package com.backbase.assesment.movies.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.backbase.assesment.movies.client.OMDB;
import com.backbase.assesment.movies.client.OMDBRestClient;
import com.backbase.assesment.movies.domain.MovieNotFoundException;
import com.backbase.assesment.movies.domain.MovieRating;
import com.backbase.assesment.movies.model.Movie;
import com.backbase.assesment.movies.persistance.entity.AcademyAward;
import com.backbase.assesment.movies.persistance.repository.AcademyAwardRepository;
import com.backbase.assesment.movies.persistance.repository.UserRatingRepository;
import com.backbase.assesment.movies.service.mapper.MoviesRatingMapper;

@ExtendWith(MockitoExtension.class)
class MoviesServiceTest {

    private MoviesService moviesService;

    @Mock private AcademyAwardRepository awardRepository;
    @Mock private OMDBRestClient omdbRestClient;
    @Mock private UserRatingRepository ratingRepository;

    @BeforeEach
    void setUp() {
        MoviesRatingMapper mapper = new MoviesRatingMapper();
        moviesService = new MoviesService(awardRepository, omdbRestClient, ratingRepository, mapper);
    }

    @Test
    @DisplayName("Retrieve Movie details with Award details")
    void retrieveMoveDetails() {
        when(omdbRestClient.retrieveOmdbData(anyString(), anyString())).thenReturn(getOmdbResponse());
        when(awardRepository.findByCategoryAndNomineeEqualsIgnoreCase(anyString(), anyString())).thenReturn(awards(true));
        when(ratingRepository.getRatingCountAndAverage(anyString())).thenReturn(rating());

        Movie movie = moviesService.retrieveMovieDetails("Avatar");
        assertEquals("t123", movie.getId());
        assertEquals("2001", movie.getYear());
        assertEquals("$300,000,000", movie.getBoxOfficeValue());
        assertEquals("Avatar", movie.getName());
        assertEquals(new BigDecimal("9.50"), movie.getRating());
        assertEquals(15654L, movie.getVotes());
        assertEquals("Best Picture", movie.getAwards().get(0));
    }

    @Test
    @DisplayName("Retrieve Movie details with No Awards WON")
    void retrieveMoveDetailsWithNoAwards() {
        when(omdbRestClient.retrieveOmdbData(anyString(), anyString())).thenReturn(getOmdbResponse());
        when(awardRepository.findByCategoryAndNomineeEqualsIgnoreCase(anyString(), anyString())).thenReturn(awards(false));
        when(ratingRepository.getRatingCountAndAverage(anyString())).thenReturn(rating());

        Movie movie = moviesService.retrieveMovieDetails("Avatar");
        assertEquals("t123", movie.getId());
        assertEquals("2001", movie.getYear());
        assertEquals("$300,000,000", movie.getBoxOfficeValue());
        assertEquals("Avatar", movie.getName());
        assertEquals(new BigDecimal("9.50"), movie.getRating());
        assertEquals(15654L, movie.getVotes());
        assertTrue(movie.getAwards().isEmpty());
    }

    @Test
    @DisplayName("Retrieve Movie details with No Awards nominated fro Oscar")
    void retrieveMoveDetailsWithNoAwardsNominated() {
        when(omdbRestClient.retrieveOmdbData(anyString(), anyString())).thenReturn(getOmdbResponse());
        when(awardRepository.findByCategoryAndNomineeEqualsIgnoreCase(anyString(), anyString())).thenReturn(Optional.empty());
        when(ratingRepository.getRatingCountAndAverage(anyString())).thenReturn(rating());

        Movie movie = moviesService.retrieveMovieDetails("Avatar");
        assertEquals("t123", movie.getId());
        assertEquals("2001", movie.getYear());
        assertEquals("$300,000,000", movie.getBoxOfficeValue());
        assertEquals("Avatar", movie.getName());
        assertEquals(new BigDecimal("9.50"), movie.getRating());
        assertEquals(15654L, movie.getVotes());
        assertTrue(movie.getAwards().isEmpty());
    }

    @Test
    @DisplayName("Retrieve Movie details When local rating Not available")
    void retrieveMoveDetailsWhenRatingNotAvailable() {
        when(omdbRestClient.retrieveOmdbData(anyString(), anyString())).thenReturn(getOmdbResponse());
        when(awardRepository.findByCategoryAndNomineeEqualsIgnoreCase(anyString(), anyString())).thenReturn(Optional.empty());
        when(ratingRepository.getRatingCountAndAverage(anyString())).thenReturn(Optional.empty());

        Movie movie = moviesService.retrieveMovieDetails("Avatar");
        assertEquals("t123", movie.getId());
        assertEquals("2001", movie.getYear());
        assertEquals("$300,000,000", movie.getBoxOfficeValue());
        assertEquals("Avatar", movie.getName());
        assertEquals(new BigDecimal("0.00"), movie.getRating());
        assertEquals(0, movie.getVotes());
        assertTrue(movie.getAwards().isEmpty());
    }

    @Test
    @DisplayName("Retrieve Movie details when movie not found in OMDB")
    void retrieveMoveDetailsWithNoMovieInOMDB() {
        when(omdbRestClient.retrieveOmdbData(anyString(), anyString())).thenReturn(OMDB.builder().build());

        MovieNotFoundException exception = assertThrows(MovieNotFoundException.class,
            () -> moviesService.retrieveMovieDetails("Avatar"));
        assertEquals("Avatar", exception.getMovieName());
    }

    @Test
    @DisplayName("Retrieve Movie details when OMDB returns error or rejects")
    void retrieveMoveDetailsWithErrorFromOMDB() {
        when(omdbRestClient.retrieveOmdbData(anyString(), anyString())).thenReturn(null);

        MovieNotFoundException exception = assertThrows(MovieNotFoundException.class,
            () -> moviesService.retrieveMovieDetails("Avatar"));
        assertEquals("Avatar", exception.getMovieName());
    }

    @Test
    @DisplayName("Get Top-10 movies")
    void getTopMovies() {
        when(ratingRepository.getTopMovieRatings(any())).thenReturn(List.of(rating().get()));
        when(omdbRestClient.retrieveOmdbData("i", "t123")).thenReturn(getOmdbResponse());
        List<Movie> movies = moviesService.getTopMovies(10);
        assertFalse(movies.isEmpty());
        Movie movie = movies.get(0);
        assertEquals("t123", movie.getId());
        assertEquals("2001", movie.getYear());
        assertEquals("$300,000,000", movie.getBoxOfficeValue());
        assertEquals(300000000, movie.getBoxOfficeValueAsNumber());
        assertEquals("Avatar", movie.getName());
        assertEquals(new BigDecimal("9.50"), movie.getRating());
        assertEquals(15654L, movie.getVotes());
        assertTrue(movie.getAwards().isEmpty());
    }

    @Test
    @DisplayName("Get Top movies when no response from OMDB")
    void getTopMoviesWithNoResponseFromOMDB() {
        when(ratingRepository.getTopMovieRatings(any())).thenReturn(List.of(rating().get()));
        when(omdbRestClient.retrieveOmdbData("i", "t123")).thenReturn(null);
        List<Movie> movies = moviesService.getTopMovies(10);
        assertTrue(movies.isEmpty());
    }

    private OMDB getOmdbResponse() {
        return OMDB.builder()
            .imdbID("t123")
            .title("Avatar")
            .year("2001")
            .boxOffice("$300,000,000")
            .build();
    }

    private Optional<AcademyAward> awards(boolean isWon) {
        AcademyAward awards = new AcademyAward();
        awards.setWon(isWon);
        awards.setCategory("Best Picture");
        return Optional.of(awards);
    }

    private Optional<MovieRating> rating() {
        return Optional.of(new MovieRating("t123", 9.5, 15654L));
    }
}