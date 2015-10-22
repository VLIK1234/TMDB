package github.tmdb.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;

import github.tmdb.CoreApplication;
import github.tmdb.R;
import github.tmdb.app.AbstractActivity;
import github.tmdb.fragment.LanguageDialogFragment;
import github.tmdb.utils.PreferenceUtil;

/**
 @author IvanBakach
 @version on 05.02.2015
 */
public class Language {
    public static void getLanguageDialog(AbstractActivity activity){
        DialogFragment dialogFragment = new LanguageDialogFragment();
        dialogFragment.show(activity.getSupportFragmentManager(), activity.getString(R.string.alpha));
    }

    public static void setLanguage(String language){
        PreferenceUtil.putString(CoreApplication.getAppContext().getString(R.string.key_language), language);
    }

    public static String getLanguage(){
        return PreferenceUtil.getString(CoreApplication.getAppContext().getString(R.string.key_language));
    }
}
