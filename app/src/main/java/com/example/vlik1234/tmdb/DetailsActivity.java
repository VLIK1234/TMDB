package com.example.vlik1234.tmdb;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import com.example.vlik1234.tmdb.bo.NoteGsonModel;


public class DetailsActivity extends ActionBarActivity {
    TextView film_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        film_id = (TextView) findViewById(R.id.id_film);
        MainActivity mainActivity = getIntent().getParcelableExtra("MainActivity");
        film_id.setText(mainActivity.selectItemID);

    }

}
