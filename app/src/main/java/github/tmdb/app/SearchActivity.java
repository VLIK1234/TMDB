package github.tmdb.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import github.tmdb.R;
import github.tmdb.api.ApiTMDB;
import github.tmdb.bo.DescriptionOfTheFilm;
import github.tmdb.fragment.ListFilmFragment;
import github.tmdb.helper.ErrorHelper;

/**
 @author IvanBakach
 @version on 18.01.2015
 */
public class SearchActivity extends AbstractActivity implements SearchView.OnQueryTextListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction fragmentTransaction;
        Fragment fragment;

        DescriptionOfTheFilm description = getIntent().getParcelableExtra(
                DescriptionOfTheFilm.class.getCanonicalName());

        setTitle(getString(R.string.search_by) + "\"" + description.getQueryWord() + "\"");

        if (savedInstanceState == null) {
            fragment = ListFilmFragment.newInstance(description.getDetailsUrl());
//            fragment = MainFragment.newInstance(description.getDetailsUrl());
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.content, fragment);
            fragmentTransaction.commit();
        }

    }

    @Override
    protected Fragment createFragment() {
        return null;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.language_setting:
                Intent intentSetting = new Intent(SearchActivity.this, SettingsActivity.class);
                startActivity(intentSetting);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onSearch(String search) throws UnsupportedEncodingException {
        DescriptionOfTheFilm description = new DescriptionOfTheFilm(ApiTMDB.getSearchMovie(URLEncoder.encode(search, getString(R.string.utf_8)), ApiTMDB.SEARCH_TYPE_PHRASE), search);
        Intent intent = getIntent();
        intent.putExtra(DescriptionOfTheFilm.class.getCanonicalName(), description);
        startActivity(intent);
//        restartActivity();
    }


    @Override
    public boolean onQueryTextSubmit(String s) {
        try {
            onSearch(s);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            ErrorHelper.showDialog(getString(R.string.unsup_encod_exсept) + e.getMessage(),
                    getSupportFragmentManager().beginTransaction());
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return true;
    }
}
