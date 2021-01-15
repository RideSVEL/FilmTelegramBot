package serejka.telegram.bot.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Movie {

    private int id;
    private String title;
    private String originalTitle;
    private String overview;
    private List<String> genres;
    private String year;
    private String premiere;
    private List<String> country;
    private int runtime;
    private float voteAverage;
    private int votes;
    private List<String> pathToImages;
    private String imdb;
    private long budget;

}
