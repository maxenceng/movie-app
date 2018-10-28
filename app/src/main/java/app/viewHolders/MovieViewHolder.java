package app.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import app.movies.Movie;
import app.activities.R;
import app.utils.DetailsListener;
import com.bumptech.glide.Glide;

/**
 * View holder for movies from the API, used by ImageListAdapter to populate a RecyclerView element
 */
public class MovieViewHolder extends RecyclerView.ViewHolder {
    private ImageView imageViewMovie;
    private DetailsListener listener;

    // Class constructor, binds the ImageView ID to an attribute
    public MovieViewHolder(View v, DetailsListener listener) {
        super(v);
        this.imageViewMovie = v.findViewById(R.id.image_movie_list);
        this.listener = listener;
    }

    // Adds an image to the list every time it is called
    public void populateMovieImage(final Movie.Results movie) {
        String posterUrl = "https://image.tmdb.org/t/p/original" + movie.getPoster_path();
        Glide
                .with(imageViewMovie.getContext())
                .load(posterUrl)
                .into(imageViewMovie);
        imageViewMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onMovieClick(movie);
            }
        });
    }
}
