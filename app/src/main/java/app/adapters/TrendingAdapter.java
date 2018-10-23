package app.adapters;

import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.w3c.dom.Text;

import java.util.List;

import app.api.Movie;
import app.movies.R;
import app.utils.DetailsListener;

public class TrendingAdapter extends RecyclerView.Adapter<TrendingAdapter.ViewHolder> {
  private List<Movie.Results> movies;
  private DetailsListener listener;

  public TrendingAdapter(List<Movie.Results> movies, DetailsListener listener) {
    this.movies = movies;
    this.listener = listener;
  }

  // Provide a reference to the views for each data item
  public static class ViewHolder extends RecyclerView.ViewHolder {
    private ImageView imageView;

    public ViewHolder(View v, final DetailsListener listener) {
      super(v);
      imageView = v.findViewById(R.id.image_view);
    }
  }

  @Override
  public TrendingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_trending, parent, false);
    return new ViewHolder(v, this.listener);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
    String posterUrl = "https://image.tmdb.org/t/p/original" + movies.get(position).poster_path;
        Glide
        .with(holder.imageView.getContext())
        .load(posterUrl)
        .into(holder.imageView);
    holder.imageView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        listener.onRowClick(position);
      }
    });
  }

  @Override
  public int getItemCount() {
    return movies.size();
  }
}
