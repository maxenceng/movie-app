package app.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import app.activities.R;

/**
 * View holder for favorite movies, used by FavoritesAdapter to populate a RecyclerView element
 */
public class FavoritesViewHolder extends RecyclerView.ViewHolder {
    private TextView textView;

    // Class constructor, binds the TextView ID to an attribute
    public FavoritesViewHolder(View v) {
        super(v);
        this.textView = v.findViewById(R.id.text_movie_list);
    }


    // Adds a TextView with the movie title to the list every time it is called
    public void populateMovieText(final String movieTitle) {
        textView.setText(movieTitle);
    }
}
