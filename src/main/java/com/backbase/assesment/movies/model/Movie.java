package com.backbase.assesment.movies.model;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode(callSuper = false, exclude = {"awards", "rating", "votes", "boxOfficeValue"})
public class Movie extends RepresentationModel<Movie> {

    private String id;
    private String name;
    private String year;
    private List<String> awards;
    private BigDecimal rating;
    private Long votes;
    private String boxOfficeValue;

    @JsonIgnore
    public Integer getBoxOfficeValueAsNumber() {
        return Integer.valueOf(boxOfficeValue.replaceAll("[$,]", ""));
    }
}
