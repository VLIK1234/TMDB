package github.tmdb.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import github.tmdb.R;
import github.tmdb.api.ApiTMDB;
import github.tmdb.api.Language;
import github.tmdb.bo.DescriptionOfTheFilm;
import github.tmdb.fragment.ListFilmFragment;
import github.tmdb.fragment.RecyclerViewFragment;
import github.tmdb.helper.ErrorHelper;
/**
 @author IvanBakach
 @version on 12.01.2015
 */
public class MainScreenActivity extends AbstractActivity implements SearchView.OnQueryTextListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
//        // Inflate the "decor.xml"
//        DrawerLayout drawer = (DrawerLayout) LayoutInflater.from(getBaseContext()).inflate(R.layout.activity_main_screen, null); // "null" is important.
//d
//        // HACK: "steal" the first child of decor view
//        ViewGroup decor = (ViewGroup) getWindow().getDecorView();
//        View child = decor.getChildAt(0);
//        decor.removeView(child);
//        FrameLayout container = (FrameLayout) drawer.findViewById(R.id.frame_dinamic); // This is the container we defined just now.
//        container.addView(child);
//
//        // Make the drawer replace the first child
//        decor.addView(drawer);
//

        setTitle(getString(R.string.now_playing));
        FragmentTransaction fragmentTransaction;
        Fragment fragment;

        if (savedInstanceState == null) {
            fragment = RecyclerViewFragment.newInstance(ApiTMDB.getNowPlayingGet());
//            fragment = MainFragment.newInstance(ApiTMDB.getNowPlayingGet());
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
        searchView.clearFocus();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.language_setting:
                Intent intentSetting = new Intent(MainScreenActivity.this, SettingsActivity.class);
                startActivity(intentSetting);
//                Language.getLanguageDialog(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onSearch(String search) throws UnsupportedEncodingException {
        DescriptionOfTheFilm description = new DescriptionOfTheFilm(ApiTMDB.getSearchMovie(URLEncoder.encode(search, getString(R.string.utf_8)), ApiTMDB.SEARCH_TYPE_PHRASE), search);
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
