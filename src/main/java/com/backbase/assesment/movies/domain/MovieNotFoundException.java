package com.backbase.assesment.movies.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Handles Movie not found scenarios
 *
 * @author Akhtar
 */
@Getter
@AllArgsConstructor
public class MovieNotFoundException extends RuntimeException {

    private final String movieName;

}
