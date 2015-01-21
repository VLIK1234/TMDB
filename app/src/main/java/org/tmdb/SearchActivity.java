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

/**
 * Created by VLIK on 18.01.2015.
 */
public class SearchActivity extends ActionBarActivity implements SearchView.OnQueryTextListener{


    private FragmentTransaction fragmentTransaction;

    private Fragment fragment;


    public static final String EXTRA_LANG = "extra_lang";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DescriptionOfTheFilm description = getIntent().getParcelableExtra(
                DescriptionOfTheFilm.class.getCanonicalName());

        setTitle("Search by \"" + description.getQueryWord() + "\"");

        if (savedInstanceState == null) {
            this.fragment = SearchFragment.newInstance(description.getDetailsUrl());

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
        searchView.setQueryHint("Search");
        searchView.setOnQueryTextListener(this);
        return true;
    }

    private void onSearch(String search){
        DescriptionOfTheFilm description = new DescriptionOfTheFilm(ApiTMDB.getSearchMovie(search), search);
        Intent intent = getIntent();
        intent.putExtra(DescriptionOfTheFilm.class.getCanonicalName(), description);
        finish();
        startActivity(intent);
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
}