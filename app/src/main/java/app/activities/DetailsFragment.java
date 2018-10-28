package app.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import android.widget.TextView;
import app.utils.RetrofitBuilder;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import app.api.CallApis;
import app.movies.Movie;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.FileInputStream;
import java.util.List;

import static app.utils.StorageTools.*;

/**
 * Fragment called when clicking on the image of a movie (when navigating on the trending list or the search result list)
 * Displays the poster, the title, the overview and the rating of the current movie
 * The user has the option to add/remove the movie to his favorites and can share the movie's information
 */
public class DetailsFragment extends Fragment {
    @BindView(R.id.image_details) ImageView imageView;
    @BindView(R.id.movie_title) TextView movieTitle;
    @BindView(R.id.movie_overview) TextView movieOverview;
    @BindView(R.id.movie_rating) TextView movieRating;
    @BindView(R.id.button_add_favorites) Button addToFavorites;
    @BindView(R.id.button_remove_favorites) Button removeFromFavorites;

    // Creates the fragment view and if arguments were passed to the fragment, it will display the movie's details
    // It can display both movies from the API and movies that were added locally
    // Else, if there were no arguments, simply displays the view
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, rootView);
        Bundle args = getArguments();
        if (args == null) return rootView;
        boolean addedMovie = args.getBoolean("addedMovie");
        if (addedMovie) {
            // Displays a custom movie
            String addedMovieTitle = args.getString("movieTitle");
            String addedMovieOverview = args.getString("movieOverview");
            this.getAddedMovieDetails(addedMovieTitle, addedMovieOverview);
        } else {
            // Displays a movie from the API
            int movieId = args.getInt("id");
            this.getDetails(movieId);
        }
        return rootView;
    }

    // Checks if the movie is in the user's favorites
    private Boolean movieInFavorites() {
        String defaultValue = "[]";
        Context ctx = getActivity().getApplicationContext();
        String filename = "favorites";
        // Get the file, creates it if doesn't exist
        FileInputStream fileInputStream = getFileContent(ctx, filename, defaultValue);
        // Get the data inside the file
        String lineData = getStringFromFile(fileInputStream);
        return lineData.contains(movieTitle.getText().toString());
    }

    // Adds the movie title into the user's favorites
    @OnClick(R.id.button_add_favorites) void addToFavorites() {
        String defaultValue = "[]";
        Context ctx = getActivity().getApplicationContext();
        String filename = "favorites";
        String movieTitleText = movieTitle.getText().toString();
        // Get the file, creates it if doesn't exist
        FileInputStream fileInputStream = getFileContent(ctx, filename, defaultValue);
        // Get the data inside the file
        String lineData = getStringFromFile(fileInputStream);

        Gson parser = new Gson();

        // We add the id to the json list in the string format
        String jsonInString = addObjectToString(movieTitleText, parser, lineData);

        // The 'Add' button is set to invisible so that the user can click the 'Remove' button
        addToFavorites.setVisibility(View.INVISIBLE);
        removeFromFavorites.setVisibility(View.VISIBLE);
        writeFile(ctx, filename, jsonInString);
    }

    // Remove the movie title from the user's favorites
    @OnClick(R.id.button_remove_favorites) void removeFromFavorites() {
        String defaultValue = "[]";
        Context ctx = getActivity().getApplicationContext();
        String filename = "favorites";
        String movieTitleText = movieTitle.getText().toString();
        // Get the file, creates it if doesn't exist
        FileInputStream fileInputStream = getFileContent(ctx, filename, defaultValue);
        // Get the data inside the file
        String lineData = getStringFromFile(fileInputStream);

        Gson parser = new Gson();

        // We add the id to the json list in the string format
        String jsonInString = removeObjectFromString(movieTitleText, parser, lineData);
        addToFavorites.setVisibility(View.VISIBLE);
        removeFromFavorites.setVisibility(View.INVISIBLE);
        writeFile(ctx, filename, jsonInString);
    }

    // Allows the user to share the movie's information
    @OnClick(R.id.button_share_movie) void shareMovie() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        String content = movieTitle.getText().toString()
                + ":\n"
                + movieOverview.getText().toString()
                ;
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.setType("text/plain");
        startActivity(intent);

    }

    // Removes the title from the list then returns it as a string
    private <T> String removeObjectFromString(T object, Gson parser, String jsonString) {
        List<T> objectList = toList(jsonString, parser);
        objectList.remove(object);
        return parser.toJson(objectList);
    }

    // Adds the title from the list then returns it as a string
    private <T> String addObjectToString(T object, Gson parser, String jsonString) {
        List<T> objectList = toList(jsonString, parser);
        objectList.add(object);
        return parser.toJson(objectList);
    }

    // Sets the TextView elements values to the information we have on the custom movie
    private void getAddedMovieDetails(String addedMovieTitle, String addedMovieOverview) {
        movieTitle.setText(addedMovieTitle);
        movieOverview.setText(addedMovieOverview);
    }

    // Gets the movie details from the API thanks to its ID, then sets all the xml elements needed to
    // the values we got from the call
    private void getDetails(int movieId) {
        CallApis apiCaller = RetrofitBuilder.buildCallApis();
        // Creating the call to get a specific movie from a query string
        Call<Movie.Results> getMovieFromIdCall = apiCaller.getMovieFromId(Integer.toString(movieId));
        // Managing the response of the call
        getMovieFromIdCall.enqueue(new Callback<Movie.Results>() {
            @Override
            public void onResponse(@NonNull Call<Movie.Results> call, @NonNull Response<Movie.Results> response) {
                // Gets the message contained in the response
                Movie.Results resp = response.body();
                if (resp == null) return;
                String posterUrl = "https://image.tmdb.org/t/p/original" + resp.getPoster_path();
                Glide
                    .with(getContext())
                    .load(posterUrl)
                    .apply(new RequestOptions().override(400, 400))
                    .into(imageView);
                movieTitle.setText(resp.getTitle());
                movieOverview.setText(resp.getOverview());
                String rating = String.format("%.2f", resp.getVote_average()) + " / 10";
                movieRating.setText(rating);
                if (!movieInFavorites()) {
                    removeFromFavorites.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Movie.Results> call, @NonNull Throwable t) {
            }
        });
    }
}