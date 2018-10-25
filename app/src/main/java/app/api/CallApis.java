package app.api;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CallApis {


    @GET("trending/movie/week?api_key=524fb1cf1f2f350e3fba699187b503ce")
    Call<Movie> getTrendingMovies();


    @GET("search/movie?api_key=524fb1cf1f2f350e3fba699187b503ce")
    Call<Movie> searchMovies(@Query("query") String movieName);

    @GET("movie/{movie_id}?api_key=524fb1cf1f2f350e3fba699187b503ce")
    Call<Movie.Results> getMovieFromId(@Path("movie_id") String id);

}
