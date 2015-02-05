package github.tmdb.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;

import github.tmdb.R;
import github.tmdb.app.AbstractActivity;
import github.tmdb.fragment.LanguageDialogFragment;

/**
 * Created by ASUS on 05.02.2015.
 */
public class Language {
    public static final String LANGUAGE_SETTING = "Language setting";
    public static final String LANGUAGE = "Language";
    public static SharedPreferences setting;
    public static SharedPreferences.Editor editor;

    public static void initialize(Context context){
        setting = context.getSharedPreferences(LANGUAGE_SETTING, Context.MODE_PRIVATE);
        editor = setting.edit();
        editor.apply();
    }

    public static void getLanguageDialog(AbstractActivity activity){
        DialogFragment dialogFragment = new LanguageDialogFragment();
        dialogFragment.show(activity.getSupportFragmentManager(), activity.getString(R.string.alpha));
    }

    public static void setLanguage(String language){
        editor.putString(LANGUAGE, language);
        editor.apply();
    }

    public static String getLanguage(){
        return setting.getString(LANGUAGE,"");
    }
}
