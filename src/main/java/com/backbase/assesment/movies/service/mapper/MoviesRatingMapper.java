package com.backbase.assesment.movies.service.mapper;

import static java.math.BigDecimal.valueOf;
import static java.math.RoundingMode.UP;

import java.util.List;

import org.springframework.stereotype.Service;

import com.backbase.assesment.movies.client.OMDB;
import com.backbase.assesment.movies.domain.MovieRating;
import com.backbase.assesment.movies.model.Movie;
import com.backbase.assesment.movies.model.Rating;
import com.backbase.assesment.movies.persistance.entity.UserRating;

/**
 * Movie & user rating mapper
 *
 * @author Akhtar
 */
@Service
public class MoviesRatingMapper {

    /**
     * Map movie model with all details
     *
     * @param omdbResponse movie details from OMDB
     * @param awards       list of awards won
     * @param rating       average movie rating by all users
     * @return movie model filled with all data
     */
    public Movie mapMovieDetails(OMDB omdbResponse, List<String> awards, MovieRating rating) {
        return Movie.builder()
            .name(omdbResponse.getTitle())
            .year(omdbResponse.getYear())
            .rating(valueOf(rating.getRating()).setScale(2, UP))
            .votes(rating.getVotes())
            .boxOfficeValue(omdbResponse.getBoxOffice())
            .id(rating.getId())
            .awards(awards)
            .build();
    }

    /**
     * Map user rating entity
     *
     * @param builder    user rating builder
     * @param userRating user rating details
     * @param movieId    OMDB movie Id
     * @param userId     logged-in user Id
     * @return userRating entity
     */
    public UserRating mapUserEntity(UserRating.UserRatingBuilder builder, Rating userRating, String movieId, String userId) {
        return builder
            .rating(userRating.getUserRating())
            .movieId(movieId)
            .userId(userId)
            .comment(userRating.getComments())
            .build();
    }

    /**
     * Map rating model with user rating details and average movie rating details
     *
     * @param persistedEntity saved user rating entity from DB
     * @param rating          average movie rating details
     * @return rating with user rating details and average movie rating details
     */
    public Rating mapRatingFromEntity(UserRating persistedEntity, MovieRating rating) {
        return Rating.builder()
            .userRating(persistedEntity.getRating())
            .comments(persistedEntity.getComment())
            .movieRating(valueOf(rating.getRating()).setScale(2, UP))
            .votes(rating.getVotes())
            .build();
    }
}
