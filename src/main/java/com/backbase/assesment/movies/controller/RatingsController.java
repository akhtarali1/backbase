package com.backbase.assesment.movies.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backbase.assesment.movies.model.Rating;
import com.backbase.assesment.movies.service.RatingService;

/**
 * Ratings controller to post user ratings
 *
 * @author Akhtar
 */
@RestController
@RequestMapping(value = "/backbase/movies/ratings", produces = APPLICATION_JSON_VALUE)
public class RatingsController {

    private final RatingService ratingService;

    /**
     * Constructor Ratings controller
     *
     * @param ratingService {@link RatingService}
     */
    public RatingsController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    /**
     * Post movie rating per user
     *
     * @param id     omdb movie id
     * @param userId logged-in user Id
     * @param rating user rating details
     * @return saved user rating details along with overall movie rating
     */
    @PostMapping("/{id}")
    public Rating postRatings(@PathVariable String id, @RequestHeader(required = false, value = "X-user-id") String userId, @RequestBody Rating rating) {
        return ratingService.saveUserRating(rating, id, userId);
    }
}
