package serejka.telegram.bot.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class Movie {

    private int id;
    private String title;
    private String originalTitle;
    private String overview;
    private List<String> genres;
    private String year;
    private String releaseDate;
    private List<String> country;
    private int runtime;
    private float voteAverage;
    private int votes;
    private List<String> pathToImages;
    private String imdb;
    private long budget;

}
