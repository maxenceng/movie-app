package app.movies;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import app.adapters.TrendingAdapter;
import app.api.CallApis;
import app.api.Movie;
import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailsFragment extends Fragment {
    @BindView(R.id.image_view) ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false); // REPLACE WITH R.layout.fragment_details
        this.getDetails(rootView);
        return rootView;
    }

    private void getDetails(final View rootView) {
        Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/trending/")
            .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        CallApis apiCaller = retrofit.create(CallApis.class);
        // Creating the call to get a specific movie from a query string
        Call<Movie.Results> getMovieFromIdCall = apiCaller.getMovieFromId(Integer.toString(456165));
        // Managing the response of the call
        getMovieFromIdCall.enqueue(new Callback<Movie.Results>() {
            @Override
            public void onResponse(Call<Movie.Results> call, Response<Movie.Results> response) {
                // Gets the message contained in the response
                Movie.Results resp = response.body();
                Log.i("API_CALL_LOG", "Result : title = " + resp.title + ", overview = " + resp.overview);
                String posterUrl = "https://image.tmdb.org/t/p/original" + resp.poster_path;
                Glide
                    .with(imageView.getContext())
                    .load(posterUrl)
                    .into(imageView);
                // Add content for the details
            }

            @Override
            public void onFailure(Call<Movie.Results> call, Throwable t) {
                Log.i("getMovieFromId", "onFailure: ");
            }
        });
    }
}