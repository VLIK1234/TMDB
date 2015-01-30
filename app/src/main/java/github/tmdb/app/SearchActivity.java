package github.tmdb.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import github.tmdb.R;
import github.tmdb.api.ApiTMDB;
import github.tmdb.bo.DescriptionOfTheFilm;
import github.tmdb.fragment.SearchFragment;
import github.tmdb.helper.ErrorHelper;

/**
 * Created by VLIK on 18.01.2015.
 */
public class SearchActivity extends ActionBarActivity implements SearchView.OnQueryTextListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        FragmentTransaction fragmentTransaction;
        Fragment fragment;

        DescriptionOfTheFilm description = getIntent().getParcelableExtra(
                DescriptionOfTheFilm.class.getCanonicalName());

        setTitle(getString(R.string.search_by) + "\"" + description.getQueryWord() + "\"");

        if (savedInstanceState == null) {
            fragment = SearchFragment.newInstance(description.getDetailsUrl());
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.frame_dinamic, fragment);
            fragmentTransaction.commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setOnQueryTextListener(this);
        return true;
    }

    private void onSearch(String search) throws UnsupportedEncodingException {
        DescriptionOfTheFilm description = new DescriptionOfTheFilm(ApiTMDB.getSearchMovie(URLEncoder.encode(search, getString(R.string.utf_8))), search);
        Intent intent = getIntent();
        intent.putExtra(DescriptionOfTheFilm.class.getCanonicalName(), description);
        finish();
        startActivity(intent);
    }


    @Override
    public boolean onQueryTextSubmit(String s) {
        try {
            onSearch(s);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            ErrorHelper.showDialog(getString(R.string.unsup_encod_exept) + e.getMessage(),
                    getSupportFragmentManager().beginTransaction());
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return true;
    }
}