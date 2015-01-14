package com.example.vlik1234.tmdb;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by VLIK on 12.01.2015.
 */
public class FragmentWithActivity extends ActionBarActivity implements SearchView.OnQueryTextListener{

    FragmentTransaction ft;

    Fragment fragmentPart;
    Fragment fragment2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);


        this.fragmentPart = new FragmentPart();

        this.ft = getFragmentManager().beginTransaction();
        this.ft.add(R.id.frame_dinamic, fragmentPart);
        this.ft.commit();
    }



    public void onClickNext(View view){
        this.fragment2 = new Fragment2();

        this.ft = getFragmentManager().beginTransaction();
        this.ft.replace(R.id.frame_dinamic, fragment2);
        this.ft.addToBackStack(null);
        this.ft.commit();
    }
    public void onClickPrevious(View view){
        this.fragmentPart = new FragmentPart();

        this.ft = getFragmentManager().beginTransaction();
        this.ft.replace(R.id.frame_dinamic, fragmentPart);
        this.ft.addToBackStack(null);
        this.ft.commit();
    }

    public String getUrlFromFragment(){
        return ApiTMDB.NOW_PLAYING_GET;
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

    @Override
    public boolean onQueryTextSubmit(String s) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return true;
    }
}
