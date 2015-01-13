package com.example.vlik1234.tmdb;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.vlik1234.tmdb.image.ImageLoader;

/**
 * Created by VLIK on 12.01.2015.
 */
public class FragmentWithActivity extends Activity {

    FragmentTransaction ft;

    Fragment fragmentPart;
    Fragment fragment2;

    public ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        mImageLoader = ImageLoader.get(FragmentWithActivity.this);

        /*Bundle bundle = new Bundle();
        bundle.putString("edttext", ApiTMDB.NOW_PLAYING_GET);
        // set Fragmentclass Arguments
        FragmentPart fragobj = new FragmentPart();
        fragobj.setArguments(bundle);*/


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

    public void onMainClick(View view){
        ((TextView)findViewById(R.id.text_frag)).setText("Fragment");

        DescriptionOfTheFilm description = new DescriptionOfTheFilm((long)127585);
        Intent intent = new Intent(FragmentWithActivity.this, DetailsActivity.class);
        intent.putExtra(DescriptionOfTheFilm.class.getCanonicalName(), description);
        startActivity(intent);
    }
}
