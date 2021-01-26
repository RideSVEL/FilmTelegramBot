package serejka.telegram.bot.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Comparator;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Movie {

    int id;
    String title;
    String originalTitle;
    String overview;
    List<String> genres;
    String year;
    String releaseDate;
    List<String> country;
    int runtime;
    float voteAverage;
    int votes;
    List<String> pathToImages;
    String imdb;
    long budget;

}
