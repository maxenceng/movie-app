package app.utils;

import android.view.View;

public interface DetailsListener {
  void onRowClick(int position);
  void onViewClick(View view, int position);
}
