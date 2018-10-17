package app.api;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CallApis {


    @GET("movie/week?api_key=524fb1cf1f2f350e3fba699187b503ce")
    Call<Movie> getAllMovies();


    @GET("/search/{name}")
    Movie getMovie(@Path("name") String movieName);

}
