package app.movies;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import app.adapters.TrendingAdapter;
import app.utils.DetailsListener;

public class TrendingFragment extends Fragment implements DetailsListener {
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_trending, container, false);
    RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view);
    recyclerView.setHasFixedSize(true);
    GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
    recyclerView.setLayoutManager(layoutManager);
    TrendingAdapter trendingAdapter = new TrendingAdapter(this.generateImages(), this);
    recyclerView.setAdapter(trendingAdapter);
    return rootView;
  }

  @Override
  public void onRowClick(int position) {
    Log.i("TrendingFragment", "clicked row" + position);
  }

  @Override
  public void onViewClick(View view, int position) {
    Log.i("TrendingFragment", "clicked view");
  }

  private List<String> generateImages() {
    List<String> images = new ArrayList<>();
    images.add("AAA");
    images.add("AAA");
    images.add("AAA");
    images.add("AAA");
    images.add("AAA");
    images.add("AAA");
    images.add("AAA");
    images.add("AAA");
    images.add("AAA");
    images.add("AAA");
    images.add("AAA");
    images.add("AAA");
    images.add("AAA");
    images.add("AAA");
    images.add("AAA");
    images.add("AAA");
    images.add("AAA");
    images.add("AAA");
    images.add("AAA");
    images.add("AAA");
    return images;

  }
}
