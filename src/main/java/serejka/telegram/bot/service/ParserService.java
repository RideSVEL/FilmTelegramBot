package serejka.telegram.bot.service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
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

@Service
public class ParserService {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ParserService.class);


    private static String getResponse(String request) {
        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = null;
        try {
            response = Unirest.get(request).asString();
            log.info("Get response from API with code {}", response.getStatus());
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        if (response != null) {
            return response.getBody();
        } else {
            return null;
        }
    }

    private static String getGenreById(Integer key) throws IOException {
        Properties properties = new Properties();
        FileInputStream in = new FileInputStream("src/main/resources/genres.properties");
        BufferedReader inBuf = new BufferedReader(new InputStreamReader(in, "Cp1251"));
        properties.load(inBuf);
        inBuf.close();
        return properties.getProperty("genre." + key, null);
    }

    public List<Movie> getListMovies(Commands commands) throws IOException {
        String response = null;
        int number = 5;
        switch (commands) {
            case TOPDAY -> response = getResponse(APIConfig.getDayMovie());
            case TOPWEEK -> response = getResponse(APIConfig.getWeekMovie());
            case TOP -> {
                response = getResponse(APIConfig.getTop());
                number = 15;
            }
        }
        if (response != null) {
            List<Movie> movies = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(response);
            JSONArray results = jsonObject.getJSONArray("results");
            for (int i = 0; i < number; i++) {
                JSONObject temp = results.getJSONObject(i);
                Movie movie = new Movie();
                movie.setId(temp.getInt("id"));
                movie.setTitle(temp.getString("title"));
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
        }
        return null;
    }

    public Movie parseMovie(Integer id) {
        String response = getResponse(APIConfig.getMovieRequest(id));
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
                movie.setPremiere(jsonObject.getString("release_date"));
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
