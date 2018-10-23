package app.movies;

import android.os.Bundle;
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
import android.widget.Toast;

import java.util.List;

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

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);
    ButterKnife.bind(this);
    setSupportActionBar(toolbar);


    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.addDrawerListener(toggle);
    toggle.syncState();

    navigationView.setNavigationItemSelectedListener(this);

    this.fragmentReplacement(new TrendingFragment());
    // Tests CalApis

      Retrofit.Builder builder = new Retrofit.Builder()
              .baseUrl("https://api.themoviedb.org/3/trending/")
              .addConverterFactory(GsonConverterFactory.create());
    // CallApis testobject = new CallApis("524fb1cf1f2f350e3fba699187b503ce");
   // Log.i("CallAPIs", testobject.apiKey);
      Retrofit retrofit = builder.build();

      CallApis apiCaller = retrofit.create(CallApis.class);
      Call<Movie> call =  apiCaller.getAllMovies();

      call.enqueue(new Callback<Movie>() {
          @Override
          public void onResponse(Call<Movie> call, Response<Movie> response) {

              Movie resp = response.body();
              Log.i("MONTAG", "Titre = %s" + resp.page + ", original language = " + resp.results.get(0).overview);
              Toast.makeText(HomeActivity.this, "dddd :'(", Toast.LENGTH_SHORT).show();
          }

          @Override
          public void onFailure(Call<Movie> call, Throwable t) {
              Log.i("MONTAG", "onFailure: ");
              Toast.makeText(HomeActivity.this, "error :'(", Toast.LENGTH_SHORT).show();
          }
      });
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
