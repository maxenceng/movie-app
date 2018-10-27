package app.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import app.movies.R;
import app.viewHolders.FavoritesViewHolder;


public class FavoritesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<String> movies;

    public FavoritesAdapter(List<String> movies) {
        this.movies = movies;
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_favorites, parent, false);
        return new FavoritesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof FavoritesViewHolder){
            ((FavoritesViewHolder) holder).populateMovieText(movies.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
}
