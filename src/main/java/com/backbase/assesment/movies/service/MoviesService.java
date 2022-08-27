package com.backbase.assesment.movies.service;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.backbase.assesment.movies.client.OMDB;
import com.backbase.assesment.movies.client.OMDBRestClient;
import com.backbase.assesment.movies.domain.MovieNotFoundException;
import com.backbase.assesment.movies.domain.MovieRating;
import com.backbase.assesment.movies.model.Movie;
import com.backbase.assesment.movies.persistance.repository.AcademyAwardRepository;
import com.backbase.assesment.movies.persistance.repository.UserRatingRepository;
import com.backbase.assesment.movies.service.mapper.MoviesRatingMapper;


/**
 * Movie service - Retrieves movie and rating details
 *
 * @author Akhtar
 */
@Service
public class MoviesService {

    private final AcademyAwardRepository academyAwardRepository;
    private final OMDBRestClient omdbRestClient;
    private final UserRatingRepository ratingRepository;
    private final MoviesRatingMapper mapper;

    /**
     * Constructor movie service
     *
     * @param academyAwardRepository {@link AcademyAwardRepository}
     * @param omdbRestClient         {@link OMDBRestClient}
     * @param ratingRepository       {@link UserRatingRepository}
     * @param mapper                 {@link MoviesRatingMapper}
     */
    public MoviesService(AcademyAwardRepository academyAwardRepository, OMDBRestClient omdbRestClient, UserRatingRepository ratingRepository, MoviesRatingMapper mapper) {
        this.academyAwardRepository = academyAwardRepository;
        this.omdbRestClient = omdbRestClient;
        this.ratingRepository = ratingRepository;
        this.mapper = mapper;
    }

    /**
     * Get movie detail based oon title
     *
     * @param title movie title to be searched
     * @return Movie details
     */
    public Movie retrieveMoveDetails(String title) {
        OMDB omdbResponse = omdbRestClient.retrieveOmdbData("t", title);
        String movieFullName = ofNullable(omdbResponse)
            .flatMap(omdb -> ofNullable(omdb.getTitle()))
            .orElseThrow(() -> new MovieNotFoundException(title));

        List<String> awards = academyAwardRepository.findByCategoryAndNomineeEqualsIgnoreCase("Best Picture", movieFullName)
            .map(academyAward -> academyAward.isWon()
                ? List.of(academyAward.getCategory())
                : Collections.<String>emptyList())
            .orElseGet(Collections::emptyList);

        MovieRating rating = ratingRepository.getRatingCountAndAverage(movieFullName)
            .orElseGet(() -> new MovieRating(omdbResponse.getImdbID(), 0.0, 0L));
        return mapper.mapMovieDetails(omdbResponse, awards, rating);
    }

    /**
     * Retrieve top movies based on count
     *
     * @param topCount top movies count
     * @return list of movies sorted by box office value
     */
    public List<Movie> getTopMovies(int topCount) {
        List<MovieRating> movieRatings = ratingRepository.getTopRatedMovies(PageRequest.of(0, topCount));
        return movieRatings.stream()
            .map(this::getMovie)
            .filter(Objects::nonNull)
            .sorted((m1, m2) -> m2.getBoxOfficeValueAsNumber().compareTo(m1.getBoxOfficeValueAsNumber()))
            .collect(Collectors.toList());
    }

    private Movie getMovie(MovieRating movie) {
        OMDB omdb = omdbRestClient.retrieveOmdbData("i", movie.getId());
        return ofNullable(omdb)
            .map(db -> mapper.mapMovieDetails(db, emptyList(), movie))
            .orElse(null);
    }
}
