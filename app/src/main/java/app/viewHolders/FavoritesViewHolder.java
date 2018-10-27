package app.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import app.movies.R;

public class FavoritesViewHolder extends RecyclerView.ViewHolder {
    private TextView textView;

    public FavoritesViewHolder(View v) {
        super(v);
        this.textView = v.findViewById(R.id.text_movie_list);
    }

    public void populateMovieText(final String movieTitle) {
        textView.setText(movieTitle);
    }
}
