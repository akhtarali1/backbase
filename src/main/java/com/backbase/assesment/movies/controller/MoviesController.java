package com.backbase.assesment.movies.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backbase.assesment.movies.model.Movie;
import com.backbase.assesment.movies.service.MoviesService;

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
     * Get movie based on title with Best Picture Award won or not
     *
     * @param title to be searched
     * @return movie with award details
     */
    @GetMapping("/awards")
    public Movie getMovies(@RequestParam String title) {
        Movie movie = moviesService.retrieveMoveDetails(title);
        updateLinks(movie);
        return movie;
    }

    /**
     * Get Top movies based on requested count with default value as 10
     *
     * @param topCount number of top movies to be displayed
     * @return list of top movies
     */
    @GetMapping("/topMovies")
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
