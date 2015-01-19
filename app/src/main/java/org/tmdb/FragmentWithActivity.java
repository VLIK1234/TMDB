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

import java.util.Locale;

/**
 * Created by VLIK on 12.01.2015.
 */
public class FragmentWithActivity extends ActionBarActivity implements SearchView.OnQueryTextListener{

    //TODO accessors, renames
    FragmentTransaction ft;

    Fragment fragmentPart;

    public static final String EXTRA_LANG = "extra_lang";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        setTitle("Now playing");
        //TODO check savedInstance for null
        this.fragmentPart = FragmentPart.newInstance(ApiTMDB.getNowPlayingGet(1)+"?language="+ Locale.getDefault());

        this.ft = getFragmentManager().beginTransaction();
        this.ft.add(R.id.frame_dinamic, fragmentPart);
        this.ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint("Search");
        searchView.setOnQueryTextListener(this);
        searchView.clearFocus();
        return true;
    }

    private void onSearch(String search){
        DescriptionOfTheFilm description = new DescriptionOfTheFilm(ApiTMDB.getSearchMovie(search), search);
        Intent intent = new Intent(FragmentWithActivity.this, SearchActivity.class);
        intent.putExtra(DescriptionOfTheFilm.class.getCanonicalName(), description);
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
