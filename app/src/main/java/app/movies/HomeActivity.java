package app.movies;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import app.addMovie.AddedMovie;
import app.api.CallApis;
import app.api.Movie;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {
  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.drawer_layout) DrawerLayout drawer;
  @BindView(R.id.nav_view) NavigationView navigationView;

  public void writeFile(Context context, String fileName, String stringToWrite) {
    FileOutputStream outputStream;
    try {
      outputStream = openFileOutput(fileName, context.MODE_PRIVATE);
      outputStream.write(stringToWrite.getBytes());
      outputStream.close();
      Log.i("writeFile", "File written without problem");
    } catch (Exception e) {
      Log.i("writeFile", "Problem writting the file");
      e.printStackTrace();
    }
  }


  public boolean fileExists(Context context, String filename) {
    File file = context.getFileStreamPath(filename);
    if(file == null || !file.exists()) {
      return false;
    }
    return true;
  }

  @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_home);
      Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      setSupportActionBar(toolbar);

      FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
      fab.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
              .setAction("Action", null).show();
        }
      });

      DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
      ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
          this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
      drawer.addDrawerListener(toggle);
      toggle.syncState();

      NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
      navigationView.setNavigationItemSelectedListener(this);







      // API Calls  --------------------------------------------------

      // Indicate the base URL and the converter used to bring the json into objects, here gson.
      Retrofit.Builder builder = new Retrofit.Builder()
              .baseUrl("https://api.themoviedb.org/3/")
              .addConverterFactory(GsonConverterFactory.create());

      // Builds the retrofit object
      Retrofit retrofit = builder.build();
      // Get the functions containing the API calls from the CallApis interface
      CallApis apiCaller = retrofit.create(CallApis.class);

      // Creating the call to get all the trending movies
      Call<Movie> call =  apiCaller.getAllMovies();

      // Managing the response of the call
      call.enqueue(new Callback<Movie>() {
          @Override
          public void onResponse(Call<Movie> call, Response<Movie> response) {
              // Gets the message contained in the response
              Movie resp = response.body();
              // Test the result
              Log.i("getTrendingMovies", "Page Number = %s" + resp.page + ", original language = " + resp.results.get(0).overview);
          }

          @Override
          public void onFailure(Call<Movie> call, Throwable t) {
              Log.i("getTrendingMovies", "Fail");
          }
      });

      // Creating the call to get a specific movie from a query string
      Call<Movie> callk =  apiCaller.searchMovies("chti");

      // Managing the response of the call
      callk.enqueue(new Callback<Movie>() {
        @Override
        public void onResponse(Call<Movie> call, Response<Movie> response) {
          // Gets the message contained in the response
          Movie resp = response.body();
          Log.i("searchMovies", "Titre = %s" + resp.results.get(0).title + ", original language = " + resp.results.get(0).overview);
        }

        @Override
        public void onFailure(Call<Movie> call, Throwable t) {
          Log.i("searchMovies", "onFailure: ");
        }
      });

      // END API CALLS ----------------------------------------------------------------------------




      // ADD NEW MOVIES ---------------------------------------------------------------------------

      // The object of the movie we want to add
      // public String overview;
      // public String poster_path;
      // public String title;
      AddedMovie myLocalMovie = new AddedMovie();

      // The added movies are written in added Movies in /data/data/app.movies/files/addedMovies
      // To see the device files in Android Studio : View -> Tool Window -> Device Explorer
      String filename = "addedMovies";


      // READING THE FILE -------------------------------------------------------------------------
      Context ctx = getApplicationContext();
      FileInputStream fileInputStream = null;

      // If the file exist we just open it
      if (fileExists(this,filename)) {
        try {
          fileInputStream = ctx.openFileInput(filename);
        } catch (FileNotFoundException e) {
          e.printStackTrace();
        }
      }

      // If the file doesn't exist we create one with an empty list in json format ie "[]"
      else {
        // The default empty list
        String emptyListJson = "[]";
        // OutputStream cause we write into the file
        FileOutputStream outputStreamNoExist = null;
        try {
          outputStreamNoExist = openFileOutput(filename, this.MODE_PRIVATE);
          // We write the empty list in the file
          outputStreamNoExist.write(emptyListJson.getBytes());
          outputStreamNoExist.close();
        } catch (Exception e) {
          e.printStackTrace();
        }

        try {
          // Once created we get it
          fileInputStream = ctx.openFileInput(filename);
        } catch (FileNotFoundException e) {
          e.printStackTrace();
        }

      }

      // We now read the file
      InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
      BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
      String lineData = null;
      try {
        lineData = bufferedReader.readLine();
      } catch (IOException e) {
        e.printStackTrace();
      }
      // Print what we found
      Log.i("readFile", lineData);

      // Convert the json to a list of object
      Type listType = new TypeToken<ArrayList<AddedMovie>>(){}.getType();
      List<AddedMovie> localMovieList = new Gson().fromJson(lineData, listType);

      // We add the movie to the list of AddedMovie
      localMovieList.add(myLocalMovie);

      // We convert back to json to write in the file
      Gson gson = new Gson();
      String jsonInString = gson.toJson(localMovieList);
      String fileContents =  jsonInString;
      writeFile(this, filename, fileContents);


      // END ADD NEW MOVIES------------------------------------------------------------------------


    }

  @Override
  public void onBackPressed() {
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.home, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    // Handle navigation view item clicks here.
    int id = item.getItemId();
    switch (id) {
      case R.id.nav_trending:
        this.fragmentReplacement(new TrendingFragment());
        break;
      case R.id.nav_gallery:
        this.fragmentReplacement(new GalleryFrament());
        break;
      case R.id.nav_slideshow:
        this.fragmentReplacement(new SlideshowFragment());
        break;
      case R.id.nav_manage:
        this.fragmentReplacement(new ManageFragment());
        break;
      default:
        this.fragmentReplacement(new TrendingFragment());
        break;
    }

    drawer.closeDrawer(GravityCompat.START);
    return true;
  }

  private void fragmentReplacement(Fragment fragment) {
    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
  }
}
