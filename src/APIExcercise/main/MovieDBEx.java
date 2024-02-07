package APIExcercise.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import lombok.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MovieDBEx {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static void main(String[] args) throws IOException, InterruptedException {
        MovieDBEx mdbe = new MovieDBEx();
        List<MovieDTO> movies = mdbe.getScrapedMovies();
        String movieId = "tt0468569";
        MovieDTO decorated = mdbe.addJsonData(movieId, movies.get(0));
        System.out.println("DECORATED: \n"+decorated);

    }

    private MovieDTO addJsonData(String movieId, MovieDTO movieDTO){
        String token = System.getenv("eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIwYmM5MWU3NjZhMzI5OGYwMDUwODI3YTJlYjBlM2U5NSIsInN1YiI6IjY1YzM5MjZjM2ZlNzk3MDE4M2ZlZmU5MyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.4qUcS1ruu1ghofnjpNSi7v0_VhDhyvMChv-q4GzKRH4");
//        String key = System.getenv("TMDB_KEY");
        String url = "https://api.themoviedb.org/3/find/#?api_key=$&external_source=imdb_id";
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
                .url(url.replace("#", movieId))
                .method("GET", null)
                .header("Authorization","Bearer "+token)
                .header("accept","application/json")
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            String res = response.body().string();
            System.out.println(res);
            JsonObject jsonObject = gson.fromJson(res, JsonObject.class);
            JsonObject firstMovie = jsonObject.get("movie_results").getAsJsonArray().get(0).getAsJsonObject();
            String overview = firstMovie.get("overview").getAsString();
            String releaseDate = firstMovie.get("release_date").getAsString();
            MovieDTO newMovie = new MovieDTO(movieDTO); // return a copy
            newMovie.setOverview(overview);
            newMovie.setReleaseDate(LocalDate.parse(releaseDate));
            return newMovie;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public List<MovieDTO> getScrapedMovies() {
        List<MovieDTO> movieDTOs = new ArrayList<>();

        String url = "https://www.imdb.com/chart/top/?ref_=nv_mv_250";
        try {
            Document document = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
                            "(KHTML, like Gecko) Chrome/94.0.4606.81 Safari/537.36")
                    .get();
            Elements movies = document.select("li.ipc-metadata-list-summary-item");
            System.out.println(movies.size());
            System.out.println(movies.first().html());
            movies.forEach(movie -> {
                String year = movie.select("li > div > div > div > div > span:nth-child(1)").get(0).text();
                String durationText = movie.select("li > div > div > div > div > span:nth-child(2)").get(0).text();
                String mpaa = movie.select("li > div > div > div > div > span:nth-child(3)").get(0).text();
                String rating = movie.select("li > div > div > div > span > div > span.ipc-rating-star").get(0).text();

                MovieDTO movieDTO = MovieDTO.builder()
                        .title(movie.select("h3.ipc-title__text").text().split(" ")[1])
                        .imdbId(movie.select("a.ipc-title-link-wrapper").attr("href").split("/")[2])
                        .thumbnailUrl(movie.select("img.ipc-image").attr("src"))
                        .year(year)
                        .rating(Double.parseDouble(rating.split(" ")[0]))
                        .numberOfRatings(rating.split(" ")[1].substring(1, rating.split(" ")[1].length() - 1))
                        .mpaa(mpaa)
                        .durationInMins(getMinutes(durationText))
                        .build();
//                System.out.println(movieDTO);
                movieDTOs.add(movieDTO);
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return movieDTOs;
    }

    private int getMinutes(String durationText) {
        // Get the duration in minutes from the scraped text, formatted like: "2h 22m"
        if (durationText.equals("") || durationText.equals(" ") || durationText == null) {
            return 0;
        }
        String[] split = durationText.split(" ");
        if (split.length == 1) {
            return Integer.parseInt(split[0].substring(0, split[0].length() - 1));
        }
        String hoursText = split[0];
        String minutesText = split[1];
        int hours = hoursText.length() > 0 ? Integer.parseInt(hoursText.substring(0, hoursText.length() - 1)) : 0; //remove the h
        int minutes = minutesText.length() > 0 ? Integer.parseInt(minutesText.substring(0, minutesText.length() - 1)) : 0; //remove the m
        return hours * 60 + minutes;
    }
    /*private List<MovieDTO> getScrapedMovies(String movieId) throws IOException, InterruptedException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.themoviedb.org/3/movie/top_rated?language=en-US&page=3")
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIwYmM5MWU3NjZhMzI5OGYwMDUwODI3YTJlYjBlM2U5NSIsInN1YiI6IjY1YzM5MjZjM2ZlNzk3MDE4M2ZlZmU5MyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.4qUcS1ruu1ghofnjpNSi7v0_VhDhyvMChv-q4GzKRH4")
                .build();

        Response response = client.newCall(request).execute();

        return
    } */


        @Setter
        @ToString
        @AllArgsConstructor
        @NoArgsConstructor
        @Builder
        private static class MovieDTO {
            private String imdbId;
            private String title;
            private String overview;
            private String thumbnailUrl;
            private String year;
            private double rating;
            private String numberOfRatings;
            private String mpaa;
            private int durationInMins;
            private LocalDate releaseDate;
            public MovieDTO(MovieDTO that) {
                this(that.imdbId, that.title, that.overview, that.thumbnailUrl, that.year, that.rating, that.numberOfRatings, that.mpaa, that.durationInMins, that.releaseDate);
            }
        }
    }

