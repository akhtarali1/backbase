package com.backbase.assesment.movies.service;

import static java.util.Optional.ofNullable;

import org.springframework.stereotype.Service;

import com.backbase.assesment.movies.domain.MovieRating;
import com.backbase.assesment.movies.model.Rating;
import com.backbase.assesment.movies.persistance.entity.UserRating;
import com.backbase.assesment.movies.persistance.repository.UserRatingRepository;
import com.backbase.assesment.movies.service.mapper.MoviesRatingMapper;

/**
 * User Rating service
 *
 * @author Akhtar
 */
@Service
public class RatingService {

    private final UserRatingRepository userRatingRepository;
    private final MoviesRatingMapper mapper;


    /**
     * Constructor User rating service
     *
     * @param userRatingRepository {@link UserRatingRepository}
     * @param mapper               {@link MoviesRatingMapper}
     */
    public RatingService(UserRatingRepository userRatingRepository, MoviesRatingMapper mapper) {
        this.userRatingRepository = userRatingRepository;
        this.mapper = mapper;
    }

    /**
     * @param userRating user rating details to be persisted
     * @param movieId    OMDb movie Id
     * @param userId     logged-in user
     * @return Persisted rating details updated with average movie rating details
     */
    public Rating saveUserRating(Rating userRating, String movieId, String userId) {
        UserRating userRatingEntity = ofNullable(userId)
            .flatMap(user -> userRatingRepository.findByUserIdAndMovieId(userId, movieId))
            .map(existingRating -> mapper.mapUserEntity(existingRating.toBuilder(), userRating, movieId, userId))
            .orElseGet(() -> mapper.mapUserEntity(UserRating.builder(), userRating, movieId, userId));

        UserRating persistedEntity = userRatingRepository.save(userRatingEntity);

        MovieRating rating = userRatingRepository.getRatingCountAndAverage(movieId)
            .orElseGet(() -> new MovieRating(persistedEntity.getMovieId(), 0.0, 0L));

        return mapper.mapRatingFromEntity(persistedEntity, rating);
    }
}
