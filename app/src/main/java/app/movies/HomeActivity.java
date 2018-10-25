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
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;

    public void writeFile(Context context, String fileName, String stringToWrite) {
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(fileName, context.MODE_PRIVATE);
            outputStream.write(stringToWrite.getBytes());
            outputStream.close();
            Log.i("writeFile_LOG", stringToWrite + " written into "+fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean fileExists(Context context, String filename) {
        File file = context.getFileStreamPath(filename);
        if (file == null || !file.exists()) {
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

/*    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show();
      }
    });*/

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
        Call<Movie> getTrendingMoviesCall = apiCaller.getTrendingMovies();
        // Managing the response of the call
        getTrendingMoviesCall.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                // Gets the message contained in the response
                Movie resp = response.body();

                if (checkApiCall(call, response)) {
                    Log.i("API_CALL_LOG", "Results : title = " + resp.results.get(0).title + ", overview = " + resp.results.get(0).overview);
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Log.i("getTrendingMovies", "Fail");
            }
        });

        // Creating the call to get a specific movie from a query string
        Call<Movie> searchMoviesCall = apiCaller.searchMovies("chti");
        // Managing the response of the call
        searchMoviesCall.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                // Gets the message contained in the response
                Movie resp = response.body();

                if (checkApiCall(call, response)) {
                    Log.i("API_CALL_LOG", "Results : title = " + resp.results.get(0).title + ", overview = " + resp.results.get(0).overview);
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Log.i("searchMovies", "onFailure: ");
            }
        });


        // Creating the call to get a specific movie from a query string
        Call<Movie.Results> getMovieFromIdCall = apiCaller.getMovieFromId(Integer.toString(456165));
        // Managing the response of the call
        getMovieFromIdCall.enqueue(new Callback<Movie.Results>() {
            @Override
            public void onResponse(Call<Movie.Results> call, Response<Movie.Results> response) {
                // Gets the message contained in the response
                Movie.Results resp = response.body();

                if (checkApiCall(call, response)) {
                    Log.i("API_CALL_LOG", "Result : title = " + resp.title + ", overview = " + resp.overview);
                }
            }

            @Override
            public void onFailure(Call<Movie.Results> call, Throwable t) {
                Log.i("getMovieFromId", "onFailure: ");
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

        fileInputStream = getFileContent(ctx, filename, "[]");

        String lineData = getStringFromFile(fileInputStream);

        // Print what we found
        Log.i("READ_FILE_LOG", "Data from "+ filename + " : " + lineData);

        // Convert the json to a list of object
        Type listType = new TypeToken<ArrayList<AddedMovie>>() {}.getType();
        List<AddedMovie> localMovieList = new Gson().fromJson(lineData, listType);

        // We add the movie to the list of AddedMovie
        localMovieList.add(myLocalMovie);

        // We convert back to json to write in the file
        Gson gson = new Gson();
        String jsonInString = gson.toJson(localMovieList);
        String fileContents = jsonInString;
        writeFile(this, filename, fileContents);


        // END ADD NEW MOVIES ------------------------------------------------------------------------


        // ADD TO FAVORITES ------------------------------------------------------------------------

        String favoritesFileName = "favoritesMovies";
        addFavorites(this, favoritesFileName, 356);

        Log.i("getFav_LOG", gson.toJson(getFavorites(this, favoritesFileName)));


        // END FAVORITES ------------------------------------------------------------------------

    }

    private static Boolean checkApiCall(Call call, Response response) {
        Log.i("API_CALL_LOG", "Call made to URL : " + call.request().url().toString());
        if (response.code() != 200) {
            Log.i("API_CALL_LOG", "Wrong response code : " + response.code());
            return false;
        }
        Log.i("API_CALL_LOG", "Successful");
        return true;
    }

    private static <T> List<T> toList(String json, Gson parser) {
        return parser.fromJson(json, List.class);
    }

    // Returns null if the file doesn't exist

    private FileInputStream getFileContent(Context context, String filename, String defaultValue) {
        FileInputStream fileInputStream = null;
        if (fileExists(context, filename)) {
            Log.i("getFileContent_LOG",filename + " exists");
            try {
                fileInputStream = context.openFileInput(filename);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            Log.i("getFileContent_LOG",filename + " doesn't exists");
            writeDefaultValue(filename, defaultValue);
            try {
                fileInputStream = context.openFileInput(filename);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return fileInputStream;
    }

    private void writeDefaultValue(String filename, String defaultValue) {
        FileOutputStream outputStreamNoExist = null;
        try {
            outputStreamNoExist = openFileOutput(filename, this.MODE_PRIVATE);
            // We write the empty list in the file
            outputStreamNoExist.write(defaultValue.getBytes());
            outputStreamNoExist.close();
            Log.i("writeDefaultValue_LOG",filename + " was created with default value "+defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String getStringFromFile(FileInputStream fileInputStream) {
        // We now read the file
        Log.i("getStringFromFile_LOG", "Just got the string from the file");
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String lineData = null;
        try {
            lineData = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lineData;
    }

    private <T> String addObjectToString(T object, Gson parser, String jsonString) {
        List<T> objectList = toList(jsonString, parser);
        objectList.add(object);
        return parser.toJson(objectList);
    }

    // Adds the ID of the favorite to the list of integer contained in filename
    public void addFavorites(Context context, String filename, Integer id) {
        String defaultValue = "[]";
        // Get the file, creates it if doesn't exist
        FileInputStream fileInputStream = getFileContent(context, filename, defaultValue);
        // Get the data inside the file
        String lineData = getStringFromFile(fileInputStream);

        Gson parser = new Gson();

        // We add the id to the json list in the string format
        String jsonInString = addObjectToString(id, parser, lineData);

        writeFile(context, filename, jsonInString);

        Log.i("addFavorites_LOG", "Just added " + Integer.toString(id) + " to " + filename);

    }

    // Returns the list of ID of the favorites
    public List<Integer> getFavorites(Context context, String filename) {
        String defaultValue = "[]";
        // Get the file, creates it if doesn't exist
        FileInputStream fileInputStream = getFileContent(context, filename, defaultValue);
        // Get the data inside the file
        String lineData = getStringFromFile(fileInputStream);
        Gson parser = new Gson();
        return toList(lineData, parser);
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
