package app.movies;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileInputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import app.addMovie.AddedMovie;

import static app.utils.StorageTools.getFileContent;
import static app.utils.StorageTools.getStringFromFile;
import static app.utils.StorageTools.writeFile;

public class AddMovieFragment extends Fragment {
    @BindView(R.id.add_movie_title) TextInputEditText movieTitle;
    @BindView(R.id.add_movie_overview) TextInputEditText movieOverview;
    @BindView(R.id.button_add_movie) Button button;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick(R.id.button_add_movie) void addMovie() {
        // The object of the movie we want to add
        // public String overview;
        // public String poster_path;
        // public String title;
        AddedMovie myLocalMovie = new AddedMovie();
        myLocalMovie.setTitle(movieTitle.getText().toString());
        myLocalMovie.setOverview(movieOverview.getText().toString());


        // The added movies are written in added Movies in /data/data/app.movies/files/addedMovies
        // To see the device files in Android Studio : View -> Tool Window -> Device Explorer
        String filename = "addedMovies";


        // READING THE FILE -------------------------------------------------------------------------
        Context ctx = getActivity().getApplicationContext();
        FileInputStream fileInputStream;

        fileInputStream = getFileContent(ctx, filename, "[]");

        String lineData = getStringFromFile(fileInputStream);

        // Convert the json to a list of object
        Type listType = new TypeToken<ArrayList<AddedMovie>>() {
        }.getType();
        List<AddedMovie> localMovieList = new Gson().fromJson(lineData, listType);

        // We add the movie to the list of AddedMovie
        localMovieList.add(myLocalMovie);

        // We convert back to json to write in the file
        Gson gson = new Gson();
        String jsonInString = gson.toJson(localMovieList);
        writeFile(ctx, filename, jsonInString);
    }
}
