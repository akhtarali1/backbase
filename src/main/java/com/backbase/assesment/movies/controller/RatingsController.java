package com.backbase.assesment.movies.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.backbase.assesment.movies.model.Rating;
import com.backbase.assesment.movies.service.RatingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * Ratings controller to post user ratings
 *
 * @author Akhtar
 */
@RestController
@RequestMapping(value = "/backbase/movies", produces = APPLICATION_JSON_VALUE)
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
     * Post movie rating of user
     *
     * @param id     omdb movie id
     * @param userId logged-in user Id
     * @param rating user rating details
     * @return saved user rating details along with overall movie rating
     */
    @Operation(summary = "Save movie rating given by user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User rating saved",
            content = { @Content(mediaType = APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = Rating.class)) })
    })
    @PostMapping("/{id}/rating")
    @ResponseStatus(CREATED)
    public Rating postRatings(@PathVariable String id, @RequestBody @Valid Rating rating,
                              @RequestHeader(required = false, value = "x-user-id") String userId) {
        return ratingService.saveUserRating(rating, id, userId);
    }
}
