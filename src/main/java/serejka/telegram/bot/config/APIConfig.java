package serejka.telegram.bot.config;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

// https://developers.themoviedb.org/3
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class APIConfig {

    static String API_KEY_V3 = "948e6670159df009cc3be4b3cbab0697";
    static String API_MOVIE = "https://api.themoviedb.org/3/movie/";
    static String API_KEY = "?api_key=" + API_KEY_V3;
    static String API_IMAGES = "&append_to_response=images&include_image_language=en,null,ru";
    static String API_LANGUAGE = "&language=ru-RU";
    static String API_SEARCH_MOVIE = "https://api.themoviedb.org/3/search/movie" + API_KEY;
    static String API_QUERY = "&query=";
    static String API_IMAGE = "https://image.tmdb.org/t/p/original";
    static String API_MOVIE_DAY = "https://api.themoviedb.org/3/trending/movie/day";
    static String API_MOVIE_WEEK = "https://api.themoviedb.org/3/trending/movie/week";
    static String ON_IMDB = "https://www.imdb.com/title/";

    public static String getMovieRequest(Integer id) {
        return API_MOVIE + id + API_KEY + API_LANGUAGE + API_IMAGES;
    }

    public static String getDayMovie() {
        return API_MOVIE_DAY + API_KEY + API_LANGUAGE;
    }

    public static String getWeekMovie() {
        return API_MOVIE_WEEK + API_KEY + API_LANGUAGE;
    }

    public static String getMovieBySearchRequest(String title) {
        String[] forQuery = title.split(" ");
        StringBuilder sb = new StringBuilder();
        sb.append(API_SEARCH_MOVIE).append(API_QUERY);
        for (int i = 0; i < forQuery.length; i++) {
            sb.append(forQuery[i]);
            if (i + 1 < forQuery.length) {
                sb.append("+");
            }
        }
        sb.append(API_LANGUAGE);
        return sb.toString();
    }

    public static String getMovieOnImdb(String id) {
        return ON_IMDB + id;
    }

    public static String getPathToImage(String path) {
        return API_IMAGE + path;
    }

    public static String getTop() {
        return API_MOVIE + "popular" + API_KEY + API_LANGUAGE;
    }
}
