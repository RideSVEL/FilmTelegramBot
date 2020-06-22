package serejka.telegram.bot.service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import serejka.telegram.bot.config.APIConfig;
import serejka.telegram.bot.models.Movie;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParseMovieService {

    private static String getResponse(String request) {
        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = null;
        try {
            response = Unirest.get(request).asString();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        if (response != null) {
            return response.getBody();
        } else {
            return null;
        }
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
                    countries.add(countriesJSON.getJSONObject(i).getString("iso_3166_1"));
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
