package app.movies;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import static app.utils.StorageTools.getFileContent;
import static app.utils.StorageTools.getStringFromFile;
import static app.utils.StorageTools.writeFile;

public class FavoritesFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    private void addFavorite() {
        String favoritesFileName = "favoritesMovies";
        Context ctx = getActivity().getApplicationContext();
        addFavorites(ctx, favoritesFileName, 356);
        Gson gson = new Gson();
        Log.i("getFav_LOG", gson.toJson(getFavorites(getActivity().getApplicationContext(), favoritesFileName)));
    }

    // Adds the ID of the favorite to the list of integer contained in filename
    public void addFavorites(Context context, String filename, Integer id) {
        String defaultValue = "[]";
        // Get the file, creates it if doesn't exist
        FileInputStream fileInputStream = getFileContent(context, filename, defaultValue);
        // Get the data inside the file
        String lineData = getStringFromFile(fileInputStream);

        Gson parser = new Gson();

        // We add the id to the json list in the string format
        String jsonInString = addObjectToString(id, parser, lineData);

        writeFile(context, filename, jsonInString);

        Log.i("addFavorites_LOG", "Just added " + Integer.toString(id) + " to " + filename);

    }

    // Returns the list of ID of the favorites
    public List<Integer> getFavorites(Context context, String filename) {
        String defaultValue = "[]";
        // Get the file, creates it if doesn't exist
        FileInputStream fileInputStream = getFileContent(context, filename, defaultValue);
        // Get the data inside the file
        String lineData = getStringFromFile(fileInputStream);
        Gson parser = new Gson();
        return toList(lineData, parser);
    }

    private <T> String addObjectToString(T object, Gson parser, String jsonString) {
        List<T> objectList = toList(jsonString, parser);
        objectList.add(object);
        return parser.toJson(objectList);
    }

    private static <T> List<T> toList(String json, Gson parser) {
        return parser.fromJson(json, List.class);
    }

}
