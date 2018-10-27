package app.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import app.api.Movie;
import app.movies.R;
import app.utils.DetailsListener;
import com.bumptech.glide.Glide;

public class MovieViewHolder extends RecyclerView.ViewHolder {
    private ImageView imageViewMovie;
    private DetailsListener listener;

    public MovieViewHolder(View v, DetailsListener listener) {
        super(v);
        this.imageViewMovie = v.findViewById(R.id.image_movie_list);
        this.listener = listener;
    }

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
