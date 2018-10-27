package app.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.addMovie.AddedMovie;
import app.viewHolders.AddedMovieViewHolder;
import app.viewHolders.MovieViewHolder;

import java.util.List;

import app.api.Movie;
import app.movies.R;
import app.utils.DetailsListener;

public class ImageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_MOVIE = 0;
    private final int VIEW_TYPE_ADDED_MOVIE = 1;
    private List<Movie.Results> movies;
    private List<AddedMovie> addedMovies;
    private DetailsListener listener;

    public ImageListAdapter(List<Movie.Results> movies, List<AddedMovie> addedMovies, DetailsListener listener) {
        this.movies = movies;
        this.addedMovies = addedMovies;
        this.listener = listener;
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_list, parent, false);
        if(viewType == VIEW_TYPE_MOVIE){
            return new MovieViewHolder(v, listener);
        }
        return new AddedMovieViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MovieViewHolder){
            ((MovieViewHolder) holder).populateMovieImage(movies.get(position));
        }

        if(holder instanceof AddedMovieViewHolder){
            ((AddedMovieViewHolder) holder).populateAddedMovieImage(addedMovies.get(position - movies.size()));
        }
    }

    @Override
    public int getItemCount() {
        return movies.size() + addedMovies.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position < movies.size()) {
            return VIEW_TYPE_MOVIE;
        }
        if (position - movies.size() < addedMovies.size()) {
            return VIEW_TYPE_ADDED_MOVIE;
        }
        return -1;
    }
}
