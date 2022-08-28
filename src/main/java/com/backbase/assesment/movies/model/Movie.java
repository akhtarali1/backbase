package com.backbase.assesment.movies.model;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode(callSuper = false, exclude = {"awards", "rating", "votes", "boxOfficeValue"})
@Schema(description = "Movie with rating, awards and rating details")
public class Movie extends RepresentationModel<Movie> {

    @Schema(description = "OMDB movie Id")
    @NotNull
    @Size(min = 1, max = 200)
    private String id;

    @Schema(description = "Movie full name")
    @NotNull
    private String name;

    @Schema(description = "Movie year of release")
    @NotNull
    @Size(min = 1, max = 4)
    private String year;

    @Schema(description = "List of Oscar awards movie won")
    private List<String> awards;

    @Schema(description = "Movie average rating given by all users")
    private BigDecimal rating;

    @Schema(description = "Total number of votes voted for movie")
    private Long votes;

    @Schema(description = "Movie Box office collection in $")
    private String boxOfficeValue;

    /**
     * @return box office value converted as number
     */
    @JsonIgnore
    public Integer getBoxOfficeValueAsNumber() {
        return Integer.valueOf(boxOfficeValue.replaceAll("[$,]", ""));
    }
}
