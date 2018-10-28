package app.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.adapters.FavoritesAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.gson.Gson;

import java.io.FileInputStream;
import java.util.List;

import static app.utils.StorageTools.getFileContent;
import static app.utils.StorageTools.getStringFromFile;
import static app.utils.StorageTools.toList;

/**
 * Fragment called when the user clicks on 'Favorites' in the navigation drawer
 */
public class FavoritesFragment extends Fragment {
    @BindView(R.id.recycler_favorites) RecyclerView recyclerView;

    // Basic onCreateView function with an added call to get all favorites for this device
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorites, container, false);
        ButterKnife.bind(this, rootView);
        this.getFavorites();
        return rootView;
    }


    // Gets all the movies added in the local 'favorites' file
    // Then links the RecyclerView element to the favoritesAdapter
    // The favorites are written in /data/data/app.movies/files/favorites
    // To see the device files in Android Studio : View -> Tool Window -> Device Explorer
    private void getFavorites() {
        Context ctx = getActivity().getApplicationContext();
        String defaultValue = "[]";
        // Get the file, creates it if doesn't exist
        FileInputStream fileInputStream = getFileContent(ctx, "favorites", defaultValue);
        // Get the data inside the file
        String lineData = getStringFromFile(fileInputStream);
        Gson parser = new Gson();
        List<String> favorites = toList(lineData, parser);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        FavoritesAdapter favoritesAdapter = new FavoritesAdapter(favorites);
        recyclerView.setAdapter(favoritesAdapter);
    }

}
