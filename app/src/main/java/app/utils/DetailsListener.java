package app.utils;

import app.movies.AddedMovie;
import app.movies.Movie;

/**
 * Listener interface to be able to access the Details Fragment
 */
public interface DetailsListener {
  void onMovieClick(Movie.Results movie);
  void onAddedMovieClick(AddedMovie movie);
}
