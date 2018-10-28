package app.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.FileInputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import app.adapters.ImageListAdapter;
import app.movies.AddedMovie;
import app.api.CallApis;
import app.movies.Movie;
import app.utils.DetailsListener;
import app.utils.RetrofitBuilder;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static app.utils.StorageTools.getFileContent;
import static app.utils.StorageTools.getStringFromFile;

public class ImageListFragment extends Fragment implements DetailsListener {
    @BindView(R.id.recycler_images) RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, rootView);
        Bundle args = getArguments();
        if (args == null) {
            this.getTrending();
        } else {
            String search = args.getString("search");
            this.getMoviesFromSearch(search);
        }
        return rootView;
    }

    @Override
    public void onMovieClick(Movie.Results movie) {
        Fragment detailsFragment = new DetailsFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle args = new Bundle();
        args.putBoolean("AddedMovie", false);
        args.putInt("id", movie.getId());
        detailsFragment.setArguments(args);
        fragmentTransaction.replace(R.id.fragment_container, detailsFragment).commit();
    }

    @Override
    public void onAddedMovieClick(AddedMovie movie) {
        Fragment detailsFragment = new DetailsFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle args = new Bundle();
        args.putBoolean("addedMovie", true);
        args.putString("movieTitle", movie.getTitle());
        args.putString("movieOverview", movie.getOverview());
        detailsFragment.setArguments(args);
        fragmentTransaction.replace(R.id.fragment_container, detailsFragment).commit();
    }

    private void getTrending() {
        CallApis apiCaller = RetrofitBuilder.buildCallApis();
        Call<Movie> call = apiCaller.getTrendingMovies();
        processCall(call, null);
    }

    private void getMoviesFromSearch(String search) {
        CallApis apiCaller = RetrofitBuilder.buildCallApis();
        // Creating the call to get a specific movie from a query string
        Call<Movie> call = apiCaller.searchMovies(search);
        List <AddedMovie> filteredAddedMovies = this.getAddedMoviesWithSearch(search);
        // Managing the response of the call
        processCall(call, filteredAddedMovies);
    }

    private List<AddedMovie> getAddedMovies() {
        Context ctx = getActivity().getApplicationContext();
        String defaultValue = "[]";
        // Get the file, creates it if doesn't exist
        FileInputStream fileInputStream = getFileContent(ctx, "addedMovies", defaultValue);
        // Get the data inside the file
        String lineData = getStringFromFile(fileInputStream);
        Type listType = new TypeToken<ArrayList<AddedMovie>>() {
        }.getType();
        return new Gson().fromJson(lineData, listType);
    }

    private List<AddedMovie> getAddedMoviesWithSearch(String search) {
        List<AddedMovie> originalList = this.getAddedMovies();
        List<AddedMovie> filteredList = new ArrayList<>();
        for (AddedMovie am : originalList) {
            if (am.getTitle().contains(search)) {
                filteredList.add(am);
            }
        }
        return filteredList;
    }

    private void processCall(Call<Movie> call, final List<AddedMovie> filteredList) {
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(@NonNull Call<Movie> call, @NonNull Response<Movie> response) {
                Movie resp = response.body();
                if (resp == null) return;
                List<Movie.Results> movies = resp.results;
                recyclerView.setHasFixedSize(true);
                GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
                recyclerView.setLayoutManager(layoutManager);
                ImageListAdapter imageListAdapter;
                if (filteredList == null) {
                    imageListAdapter = new ImageListAdapter(movies, getAddedMovies() ,ImageListFragment.this);
                } else {
                    imageListAdapter = new ImageListAdapter(movies, filteredList, ImageListFragment.this);
                }
                recyclerView.setAdapter(imageListAdapter);
            }
            @Override
            public void onFailure(@NonNull Call<Movie> call, @NonNull Throwable t) {
            }
        });
    }
}
