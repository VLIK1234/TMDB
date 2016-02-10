package github.tmdb.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.util.Log;

import github.tmdb.R;
import github.tmdb.utils.PreferenceUtil;

/**
 * @author IvanBakach
 * @version on 02.11.2015
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private ListPreference mLanguageList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        mLanguageList = (ListPreference) findPreference(getActivity().getString(R.string.key_language));
        String[] languageArray = {"Ru", "En"};
        mLanguageList.setEntries(languageArray);
        mLanguageList.setEntryValues(languageArray);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d("Setting", "SharedPreferenceChange");
        if (key.equals(getString(R.string.key_language))) {
            Log.d("Setting", "key_language");
            mLanguageList.setSummary(PreferenceUtil.getString(R.string.key_language));
        }
    }
}
