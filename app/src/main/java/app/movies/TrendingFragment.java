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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import app.adapters.TrendingAdapter;
import app.api.CallApis;
import app.api.Movie;
import app.utils.DetailsListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TrendingFragment extends Fragment implements DetailsListener {
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_trending, container, false);
    this.generateImages(rootView);
    return rootView;
  }

  @Override
  public void onRowClick(int position) {
    Log.i("TrendingFragment", "clicked row" + position);
  }

  @Override
  public void onViewClick(View view, int position) {
    Log.i("TrendingFragment", "clicked view");
  }

  private void generateImages(final View rootView) {
    Retrofit.Builder builder = new Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/trending/")
        .addConverterFactory(GsonConverterFactory.create());
    // CallApis testobject = new CallApis("524fb1cf1f2f350e3fba699187b503ce");
    // Log.i("CallAPIs", testobject.apiKey);
    Retrofit retrofit = builder.build();

    CallApis apiCaller = retrofit.create(CallApis.class);
    Call<Movie> call =  apiCaller.getAllMovies();

    call.enqueue(new Callback<Movie>() {
      @Override
      public void onResponse(Call<Movie> call, Response<Movie> response) {

        Movie resp = response.body();
        List<Movie.Results> movies = response.body().results;
        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);
        TrendingAdapter trendingAdapter = new TrendingAdapter(movies, TrendingFragment.this);
        recyclerView.setAdapter(trendingAdapter);
      }

      @Override
      public void onFailure(Call<Movie> call, Throwable t) {
      }
    });
  }
}
