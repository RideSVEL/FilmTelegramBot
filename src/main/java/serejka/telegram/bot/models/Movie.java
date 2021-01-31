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

    public Movie(Movie movie) {
        this.id = movie.getId();
        this.title = movie.getTitle();
        this.originalTitle = movie.getOriginalTitle();
        this.overview = movie.getOverview();
        this.genres = movie.getGenres();
        this.year = movie.getYear();
        this.releaseDate = movie.getReleaseDate();
        this.country = movie.getCountry();
        this.runtime = movie.getRuntime();
        this.voteAverage = movie.getVoteAverage();
        this.votes = movie.getVotes();
        this.pathToImages = movie.getPathToImages();
        this.imdb = movie.getImdb();
        this.budget = movie.getBudget();
    }

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
