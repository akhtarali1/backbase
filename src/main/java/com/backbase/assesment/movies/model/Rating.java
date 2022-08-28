package com.backbase.assesment.movies.model;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "Individual user rating")
public class Rating {

    @Schema(description = "User give rating")
    @NotNull
    private Long userRating;

    @Schema(description = "Movie comments given by user")
    private String comments;

    @Schema(description = "Average movie rating calculate based on all users rating")
    private BigDecimal movieRating;

    @Schema(description = "Total number of votes voted for movie")
    private Long votes;
}
