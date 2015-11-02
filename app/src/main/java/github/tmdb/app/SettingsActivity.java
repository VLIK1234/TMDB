package github.tmdb.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import github.tmdb.R;
import github.tmdb.fragment.SettingsFragment;

/**
 * @author IvanBakach
 * @version on 02.11.2015
 */
public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getFragmentManager().beginTransaction().replace(R.id.fl_settings, new SettingsFragment()).commit();
    }
}
