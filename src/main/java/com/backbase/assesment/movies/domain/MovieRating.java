package com.backbase.assesment.movies.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Domain class for inter mapping and DB queries
 *
 * @author Akhtar
 */
@Getter
@AllArgsConstructor
public class MovieRating {

    private String id;
    private Double rating;
    private Long votes;
}
