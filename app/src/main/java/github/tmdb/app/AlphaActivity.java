package github.tmdb.app;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import github.tmdb.R;
import github.tmdb.fragment.AlphaFragment;

/**
 * Created by ASUS on 21.01.2015.
 */
public class AlphaActivity extends ActionBarActivity{

    private DialogFragment dialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alpha);
        dialogFragment = new AlphaFragment();
    }

    public void onButtonClick(View view) {
        dialogFragment.show(getSupportFragmentManager(), "Alpha");
    }
}