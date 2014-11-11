package com.example.vlik1234.tmdb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

/**
 * Created by ASUS on 11.11.2014.
 */
public class FragmentActivity extends ActionBarActivity{
        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
    }
    public void onStartSampleClick(View view) {
        startActivity(new Intent(this, FragmentActivity.class));
    }
}
