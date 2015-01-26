package org.tmdb;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import org.tmdb.vlik1234.R;

/**
 * Created by ASUS on 21.01.2015.
 */
public class AlphaActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alpha);
    }

    public void onActionClick(View view) {
        DialogFragment alphaFragment = new AlphaFragment();
        alphaFragment.show(getSupportFragmentManager(), "alpha_fragment");
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.radio_default:
                if (checked) {
                    Toast.makeText(getApplicationContext(), "Default", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.radio_english:
                if (checked) {
                    Toast.makeText(getApplicationContext(), "English", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.radio_russian:
                if (checked) {
                    Toast.makeText(getApplicationContext(), "Russian", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}