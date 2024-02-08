package APIExcercise.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class MovieDBEx {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static void main(String[] args) throws IOException, InterruptedException {
        MovieDBEx mdbe = new MovieDBEx();
        String movieId = "tt0468569";
        MovieDTO decorated = mdbe.addJsonData(movieId);
        System.out.println("DECORATED: \n"+decorated);

    }



    private MovieDTO addJsonData(String movieId){
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIwYmM5MWU3NjZhMzI5OGYwMDUwODI3YTJlYjBlM2U5NSIsInN1YiI6IjY1YzM5MjZjM2ZlNzk3MDE4M2ZlZmU5MyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.4qUcS1ruu1ghofnjpNSi7v0_VhDhyvMChv-q4GzKRH4";
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
            String title = firstMovie.get("title").getAsString();
            Double rating = firstMovie.get("vote_average").getAsDouble();
            int numberOfRatings = firstMovie.get("vote_count").getAsInt();
            JsonArray genreId = firstMovie.get("genre_ids").getAsJsonArray();
            String releaseDate = firstMovie.get("release_date").getAsString();
            MovieDTO newMovie = new MovieDTO(); // return a copy
            newMovie.setOverview(overview);
            newMovie.setReleaseDate(LocalDate.parse(releaseDate));
            newMovie.setTitle(title);
            newMovie.setRating(rating);
            newMovie.setNumberOfRatings(numberOfRatings);
            newMovie.setGenreId(genreId);

            return newMovie;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



        @Setter
        @ToString
        @AllArgsConstructor
        @NoArgsConstructor
        @Builder
        private static class MovieDTO {
            private String title;
            private String overview;
            private String year;
            private double rating;
            private int numberOfRatings;
            private JsonArray genreId;
            private LocalDate releaseDate;
            public MovieDTO(MovieDTO that) {
                this(that.title, that.overview,that.year, that.rating, that.numberOfRatings, that.genreId, that.releaseDate);
            }
        }
    }

