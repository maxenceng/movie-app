package app.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import app.addMovie.AddedMovie;
import app.movies.R;
import app.utils.DetailsListener;
import com.bumptech.glide.Glide;

public class AddedMovieViewHolder extends RecyclerView.ViewHolder {
    private ImageView imageViewAddedMovie;
    private DetailsListener listener;

    public AddedMovieViewHolder(View v, DetailsListener listener) {
        super(v);
        this.imageViewAddedMovie = v.findViewById(R.id.image_added_movie_list);
        this.listener = listener;
    }

    public void populateAddedMovieImage(final AddedMovie movie) {
        String defaultUrl = "https://via.placeholder.com/200x300";
        Glide
                .with(imageViewAddedMovie.getContext())
                .load(defaultUrl)
                .into(imageViewAddedMovie);
        imageViewAddedMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAddedMovieClick(movie);
            }
        });
    }
}
