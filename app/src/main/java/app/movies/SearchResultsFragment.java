package app.movies;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import app.api.CallApis;
import app.api.Movie;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchResultsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false); // REPLACE WITH R.layout.fragment_search results
        this.getMovies(rootView);
        return rootView;
    }

    private void getMovies(final View rootView) {
        // Indicate the base URL and the converter used to bring the json into objects, here gson.
        Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create());

        // Builds the retrofit object
        Retrofit retrofit = builder.build();
        // Get the functions containing the API calls from the CallApis interface
        CallApis apiCaller = retrofit.create(CallApis.class);

        // Creating the call to get a specific movie from a query string
        Call<Movie> searchMoviesCall = apiCaller.searchMovies("chti");
        // Managing the response of the call
        searchMoviesCall.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                // Gets the message contained in the response
                Movie resp = response.body();
                Log.i("API_CALL_LOG", "Results : title = " + resp.results.get(0).title + ", overview = " + resp.results.get(0).overview);
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Log.i("searchMovies", "onFailure: ");
            }
        });
    }
}
