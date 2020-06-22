package serejka.telegram.bot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

// https://developers.themoviedb.org/3
@Component
public class APIConfig {

    private static final String API_KEY_V3 = "//";
    private static final String API_MOVIE = "https://api.themoviedb.org/3/movie/";
    private static final String API_KEY = "?api_key=" + API_KEY_V3;
    private static final String API_IMAGES = "&append_to_response=images&include_image_language=en,null,ru";
    private static final String API_LANGUAGE = "&language=ru-RU";
    private static final String API_SEARCH_MOVIE = "https://api.themoviedb.org/3/search/movie?api_key=";
    private static final String API_QUERY = "&query=";
    private static final String API_IMAGE = "https://image.tmdb.org/t/p/original";

    private static final String ON_IMDB = "https://www.imdb.com/title/";

    public static String getMovieRequest(Integer id) {
        return API_MOVIE + id + API_KEY + API_LANGUAGE + API_IMAGES;
    }


    public static String getMovieBySearchRequest(String title) {
        String[] forQuery = title.split(" ");
        StringBuilder sb = new StringBuilder();
        sb.append(API_SEARCH_MOVIE).append(API_KEY_V3).append(API_QUERY);
        for (int i = 0; i < forQuery.length; i++) {
            sb.append(forQuery[i]);
            if (i + 1 < forQuery.length) {
              sb.append("+");
            }
        }
        return sb.toString();
    }

    public static String getMovieOnImdb(String id){
        return ON_IMDB + id;
    }

    public static String getPathToImage(String path) {
        return API_IMAGE + path;
    }


}
