package app.utils;

import app.addMovie.AddedMovie;
import app.api.Movie;

public interface DetailsListener {
  void onMovieClick(Movie.Results movie);
  void onAddedMovieClick(AddedMovie movie);
}
