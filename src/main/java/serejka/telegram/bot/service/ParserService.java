package serejka.telegram.bot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import serejka.telegram.bot.botapi.Commands;
import serejka.telegram.bot.config.APIConfig;
import serejka.telegram.bot.models.Movie;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ParserService {

    RestTemplate restTemplate;
    static int NUMBER_OF_FILMS = 5;

    private static String getGenreById(Integer key) throws IOException {
        Properties properties = new Properties();
        FileInputStream in = new FileInputStream("src/main/resources/genres.properties");
        BufferedReader inBuf = new BufferedReader(new InputStreamReader(in, "Cp1251"));
        properties.load(inBuf);
        inBuf.close();
        return properties.getProperty("genre." + key, null);
    }

    public List<Movie> getListMoviesBySearch(String title) {
        ResponseEntity<String> entity;
        entity = restTemplate.getForEntity(APIConfig.getMovieBySearchRequest(title), String.class);
        return parseListMovies(entity.getBody());
    }

    public List<Movie> getListMovies(Commands commands) {
        ResponseEntity<String> entity;
        switch (commands) {
            case TOPDAY:
                entity = restTemplate.getForEntity(APIConfig.getDayMovie(), String.class);
                break;
            case TOPWEEK:
                entity = restTemplate.getForEntity(APIConfig.getWeekMovie(), String.class);
                break;
            case TOP:
                entity = restTemplate.getForEntity(APIConfig.getTop(), String.class);
                break;
            default:
                return null;
        }
        return parseListMovies(entity.getBody());
    }

    private List<Movie> parseListMovies(String responseBody) {
        List<Movie> movies = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(responseBody);
            JSONArray results = jsonObject.getJSONArray("results");
            for (int i = 0; i < NUMBER_OF_FILMS; i++) {
                JSONObject temp = results.getJSONObject(i);
                Movie movie = new ObjectMapper().readValue(temp.toString(), Movie.class);
                movie.setYear(temp.getString("release_date").split("-")[0]);
                movie.setVoteAverage(temp.getFloat("vote_average"));
                JSONArray jsonArray = temp.getJSONArray("genre_ids");
                List<String> genres = new ArrayList<>();
                for (int j = 0; j < jsonArray.length(); j++) {
                    genres.add(getGenreById((Integer) jsonArray.get(j)));
                }
                movie.setGenres(genres);
                movies.add(movie);
            }
            return movies;
        } catch (Exception e) {
            return movies;
        }
    }

    public Movie parseMovie(Integer id) {
        String response;
        try {
            response = restTemplate.getForEntity(APIConfig.getMovieRequest(id), String.class).getBody();
        } catch (Exception e) {
            return null;
        }
        if (response != null) {
            Movie movie = new Movie();
            try {
                JSONObject jsonObject = new JSONObject(response);
                movie.setBudget(jsonObject.getInt("budget"));
                movie.setId(jsonObject.getInt("id"));
                movie.setImdb(jsonObject.getString("imdb_id"));
                movie.setOriginalTitle(jsonObject.getString("original_title"));
                movie.setOverview(jsonObject.getString("overview"));
                movie.setTitle(jsonObject.getString("title"));
                movie.setVotes(jsonObject.getInt("vote_count"));
                movie.setVoteAverage(jsonObject.getFloat("vote_average"));
                movie.setReleaseDate(jsonObject.getString("release_date"));
                JSONArray genresJSON = jsonObject.getJSONArray("genres");
                List<String> genres = new ArrayList<>();
                for (int i = 0; i < genresJSON.length(); i++) {
                    genres.add(genresJSON.getJSONObject(i).getString("name"));
                }
                movie.setGenres(genres);
                JSONArray countriesJSON = jsonObject.getJSONArray("production_countries");
                List<String> countries = new ArrayList<>();
                for (int i = 0; i < countriesJSON.length(); i++) {
                    countries.add(countriesJSON.getJSONObject(i).getString("name"));
                }
                movie.setCountry(countries);
                movie.setRuntime(jsonObject.getInt("runtime"));
                JSONObject images = jsonObject.getJSONObject("images");
                JSONArray photos = images.getJSONArray("backdrops");
                List<String> pathToImages = new ArrayList<>();
                pathToImages.add(jsonObject.getString("poster_path"));
                int number = Math.min(photos.length(), 2);
                for (int i = 0; i < number; i++) {
                    pathToImages.add(photos.getJSONObject(i).getString("file_path"));
                }
                movie.setPathToImages(pathToImages);
                movie.setYear(jsonObject.getString("release_date").split("-")[0]);
            } catch (JSONException e) {
                return null;
            }
            return movie;
        } else {
            return null;
        }
    }

}
