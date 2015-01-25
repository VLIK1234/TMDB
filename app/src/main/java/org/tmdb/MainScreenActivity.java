package org.tmdb;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import org.tmdb.bo.DescriptionOfTheFilm;
import org.tmdb.vlik1234.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by VLIK on 12.01.2015.
 */
public class MainScreenActivity extends ActionBarActivity implements SearchView.OnQueryTextListener{

    private FragmentTransaction fragmentTransaction;

    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        setTitle(getString(R.string.now_playing));

        if (savedInstanceState == null) {
            this.fragment = MainFragment.newInstance(ApiTMDB.getNowPlayingGet());

            this.fragmentTransaction = getFragmentManager().beginTransaction();
            this.fragmentTransaction.add(R.id.frame_dinamic, fragment);
            this.fragmentTransaction.commit();
        }
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

    private void onSearch(String search) throws UnsupportedEncodingException {
        DescriptionOfTheFilm description = new DescriptionOfTheFilm(ApiTMDB.getSearchMovie(URLEncoder.encode(search, getString(R.string.utf_8))), search);
        Intent intent = new Intent(MainScreenActivity.this, SearchActivity.class);
        intent.putExtra(DescriptionOfTheFilm.class.getCanonicalName(), description);
        startActivity(intent);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        try {
            onSearch(s);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return true;
    }
}
