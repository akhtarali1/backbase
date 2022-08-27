package com.backbase.assesment.movies.client;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

/**
 * OMDB response Model with required fields
 *
 * @author Akhtar
 */
@Getter
@Builder
public class OMDB {

    private String imdbID;
    @JsonProperty("Title")
    private String title;
    @JsonProperty("Year")
    private String year;
    @JsonProperty("BoxOffice")
    private String boxOffice;
}
