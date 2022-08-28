package com.backbase.assesment.movies.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.backbase.assesment.movies.model.Movie;
import com.backbase.assesment.movies.service.MoviesService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * Movies Controller
 *
 * @author Akhtar
 */
@RestController
@RequestMapping(value = "/backbase/movies", produces = APPLICATION_JSON_VALUE)
public class MoviesController {

    private final MoviesService moviesService;

    /**
     * Constructor Movies Controller
     *
     * @param moviesService {@link MoviesService}
     */
    public MoviesController(MoviesService moviesService) {
        this.moviesService = moviesService;
    }

    /**
     * Get movie based on title with Academy awards won or not
     * (for now only Best Picture Award category is checked)
     *
     * @param title to be searched
     * @return movie with award details
     */
    @Operation(summary = "Get movie based on title with Academy Award won or not")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Movie retrieved",
            content = {@Content(mediaType = APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = Movie.class))})
    })
    @GetMapping
    @ResponseStatus(OK)
    public Movie getMovieWithAwards(@RequestParam String title) {
        Movie movie = moviesService.retrieveMovieDetails(title);
        updateLinks(movie);
        return movie;
    }

    /**
     * Get Top movies based on requested count with default value as 10
     *
     * @param topCount number of top movies to be displayed
     * @return list of top movies
     */
    @Operation(summary = "Get top rated movies with sorting based on box office value")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Top rated Movies retrieved",
            content = {@Content(mediaType = APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = Movie.class))})
    })
    @GetMapping("/topMovies")
    @ResponseStatus(OK)
    public List<Movie> getTopMovies(@RequestParam(required = false, defaultValue = "10") Integer topCount) {
        List<Movie> movies = moviesService.getTopMovies(topCount);
        movies.forEach(this::updateLinks);
        return movies;
    }

    private void updateLinks(Movie movie) {
        movie.add(linkTo(methodOn(RatingsController.class)
            .postRatings(movie.getId(), null, null))
            .withRel("rating").withType(POST.name()));
    }
}
