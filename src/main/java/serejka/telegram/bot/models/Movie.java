package serejka.telegram.bot.models;

import java.util.List;

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

    public Movie(int id, String title, String originalTitle, String overview, List<String> genres,
                 String year, String premiere, List<String> country, int runtime,
                 int voteAverage, List<String> pathToImages, String imdb, long budget) {
        this.id = id;
        this.title = title;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.genres = genres;
        this.year = year;
        this.premiere = premiere;
        this.country = country;
        this.runtime = runtime;
        this.voteAverage = voteAverage;
        this.pathToImages = pathToImages;
        this.imdb = imdb;
        this.budget = budget;
    }

    public Movie() {

    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public long getBudget() {
        return budget;
    }

    public void setBudget(long budget) {
        this.budget = budget;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPremiere() {
        return premiere;
    }

    public void setPremiere(String premiere) {
        this.premiere = premiere;
    }

    public List<String> getCountry() {
        return country;
    }

    public void setCountry(List<String> country) {
        this.country = country;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public float getVoteAverage() {
        return voteAverage;
    }


    public List<String> getPathToImages() {
        return pathToImages;
    }

    public void setPathToImages(List<String> pathToImages) {
        this.pathToImages = pathToImages;
    }

    public String getImdb() {
        return imdb;
    }

    public void setImdb(String imdb) {
        this.imdb = imdb;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", originalTitle='" + originalTitle + '\'' +
                ", overview='" + overview + '\'' +
                ", genres=" + genres +
                ", year='" + year + '\'' +
                ", premiere='" + premiere + '\'' +
                ", country='" + country + '\'' +
                ", runtime=" + runtime +
                ", voteAverage=" + voteAverage +
                ", pathToImages=" + pathToImages +
                ", imdb='" + imdb + '\'' +
                ", budget=" + budget +
                '}';
    }
}
