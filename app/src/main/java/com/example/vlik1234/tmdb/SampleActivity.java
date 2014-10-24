package com.example.vlik1234.tmdb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class SampleActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        TextView tvName = (TextView) findViewById(R.id.textView1);
        TextView tvWhiskers = (TextView) findViewById(R.id.textView2);
        TextView tvPaws = (TextView) findViewById(R.id.textView3);
        TextView tvTail = (TextView) findViewById(R.id.textView4);

        String name = "";
        String whiskers = "";
        String paws = "";
        String tail = "";

        DocumentInfo mydocuments = (DocumentInfo)getIntent().getParcelableExtra("DocumentInfo");

        name = mydocuments.getCatName();
        whiskers = mydocuments.getWhiskers();
        paws = mydocuments.getPaws();
        tail = mydocuments.getTail();

        tvName.setText("Имя кота: " + name);
        tvWhiskers.setText("Усы: " + whiskers);
        tvPaws.setText("Лапы: " + paws);
        tvTail.setText("Хвост: " + tail);
    }
    public void onReturnHomeClick(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void onStartSampleClick(View view) {
        startActivity(new Intent(this, SampleActivity.class));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }
}
