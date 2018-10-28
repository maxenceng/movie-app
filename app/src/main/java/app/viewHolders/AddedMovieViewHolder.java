package app.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import app.movies.AddedMovie;
import app.activities.R;
import app.utils.DetailsListener;
import com.bumptech.glide.Glide;

/**
 * View holder for added movies, used by ImageListAdapter to populate a RecyclerView element
 */
public class AddedMovieViewHolder extends RecyclerView.ViewHolder {
    private ImageView imageViewAddedMovie;
    private DetailsListener listener;

    // Class constructor, binds the ImageView ID to an attribute
    public AddedMovieViewHolder(View v, DetailsListener listener) {
        super(v);
        this.imageViewAddedMovie = v.findViewById(R.id.image_added_movie_list);
        this.listener = listener;
    }

    // Adds an image to the list every time it is called
    public void populateAddedMovieImage(final AddedMovie movie) {
        String defaultUrl = "https://nsa39.casimages.com/img/2018/10/28/18102803064175331.jpg";
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
