package com.backbase.assesment.movies.model;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Rating {

    private Long userRating;
    private String comments;
    private BigDecimal movieRating;
    private Long votes;
}
