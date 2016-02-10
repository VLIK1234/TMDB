package github.tmdb.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import github.tmdb.CoreApplication;

public class PreferenceUtil {

    private static SharedPreferences mSharedPreferences = null;

    public PreferenceUtil() {
    }

    public static String getString(String key) {
        return getPref().getString(key, "");
    }

    public static String getString(int stringResourceKey) {
        return getPref().getString(CoreApplication.getAppContext().getString(stringResourceKey), "");
    }

    public static void putString(String key, String value) {
        getPref().edit().putString(key, value).apply();
    }

    public static void putInt(String key, int value) {
        getPref().edit().putInt(key, value).apply();
    }

    public static int getInt(String key, int value) {
        return getPref().getInt(key, value);
    }

    public static boolean getBoolean(String key) {
        return getPref().getBoolean(key, false);
    }

    public static void putBoolean(String key, boolean value) {
        getPref().edit().putBoolean(key, value).apply();
    }

    private static SharedPreferences getPref() {
        if (mSharedPreferences == null) {
            mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(CoreApplication.getAppContext());
        }
        return mSharedPreferences;
    }
}