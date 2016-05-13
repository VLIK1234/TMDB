package github.tmdb.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import by.istin.android.xcore.utils.StringUtil;
import github.tmdb.R;
import github.tmdb.api.ApiTMDB;
import github.tmdb.fragment.HomeFragment;
import github.tmdb.fragment.MoviesFragment;
import github.tmdb.fragment.SeriesFragment;

/**
 * @author IvanBakach
 * @version on 12.01.2015
 */
public class MainScreenActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, NavigationView.OnNavigationItemSelectedListener, FragmentManager.OnBackStackChangedListener {

    private static final String TAG = "MainScreenActivity";
    private ActionBarDrawerToggle mToggle;
    private DrawerLayout mDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(mToggle);
        mToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Fragment homeFragment = Fragment.instantiate(getBaseContext(), HomeFragment.class.getName());
        setCurrentFragment(homeFragment, false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shouldDisplayHomeUp()) {
                    onBackPressed();
                } else {
                    mDrawer.openDrawer(GravityCompat.START);
                }
            }
        });
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        shouldDisplayHomeUp();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDrawer.removeDrawerListener(mToggle);
    }

    public void setCurrentFragment(Fragment fragment, boolean withBackStack) {
        String fragmentName = fragment.getClass().getName();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.content, fragment, fragmentName);
        if (withBackStack) {
            getSupportFragmentManager().popBackStack(fragmentName, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentTransaction.addToBackStack(fragmentName);
        }
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setOnQueryTextListener(this);
        searchView.clearFocus();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.language_setting:
                Intent intentSetting = new Intent(MainScreenActivity.this, SettingsActivity.class);
                startActivity(intentSetting);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onSearch(String search) {
        String urlSearch = ApiTMDB.getSearchMovie(StringUtil.encode(search), ApiTMDB.SEARCH_TYPE_PHRASE);
//        setCurrentFragment(RecyclerViewFragment.newInstance(urlSearch), true);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        onSearch(s);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer!= null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                setCurrentFragment(new HomeFragment(), false);
                break;
            case R.id.nav_now_playing:
                setCurrentFragment(MoviesFragment.newInstance(ApiTMDB.getMovieNowPlaying()), false);
                break;
            case R.id.nav_popular:
                setCurrentFragment(MoviesFragment.newInstance(ApiTMDB.getMoviePopular()), false);
                break;
            case R.id.nav_maps:
                Intent mapActivity = new Intent(this, MapActivity.class);
                startActivity(mapActivity);
                Toast.makeText(getBaseContext(), "Maps", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_series_on_the_air:
                setCurrentFragment(SeriesFragment.newInstance(ApiTMDB.getTvOnTheAir()), false);
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    @Override
    public void onBackStackChanged() {
        shouldDisplayHomeUp();
    }

    public boolean shouldDisplayHomeUp(){
        //Enable Up button only  if there are entries in the back stack
        boolean canback = getSupportFragmentManager().getBackStackEntryCount()>0;
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(canback);
        }
        return canback;
    }

    @Override
    public boolean onSupportNavigateUp() {
        //This method is called when the up button is pressed. Just the pop back stack.
        getSupportFragmentManager().popBackStack();
        return true;
    }
}