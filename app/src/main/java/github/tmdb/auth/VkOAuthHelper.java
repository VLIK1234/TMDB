package github.tmdb.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.TextUtils;

import org.apache.http.auth.AuthenticationException;

public class VkOAuthHelper {

    public static final String TOKEN = "TOKEN";

    public static interface Callbacks {
        void onError(Exception e);

        void onSuccess();
    }

    private static String token;
    public static final String REDIRECT_URL = "https://oauth.vk.com/blank.html";
    public static final String AUTORIZATION_URL = "https://oauth.vk.com/authorize?client_id=4616332&scope=offline,wall,photos,status&redirect_uri=" + REDIRECT_URL + "&display=touch&response_type=token";

    public static String sign(String url) {
        if (url.contains("?")) {
            return url + "&access_token=" + token;
        } else {
            return url + "?access_token=" + token;
        }
    }

    public static boolean isLogged() {
        return !TextUtils.isEmpty(token);
    }

    public static boolean proceedRedirectURL(String url, Callbacks callbacks) {
        if (url.startsWith(REDIRECT_URL)) {
            Uri uri = Uri.parse(url);
            String fragment = uri.getFragment();
            Uri parsedFragment = Uri.parse("http://temp.com?" + fragment);
            String accessToken = parsedFragment.getQueryParameter("access_token");
            if (!TextUtils.isEmpty(accessToken)) {
                callbacks.onSuccess();
                token = accessToken;
                return true;
            } else {
                String error = parsedFragment.getQueryParameter("error");
                String errorDescription = parsedFragment.getQueryParameter("error_description");
                String errorReason = parsedFragment.getQueryParameter("error_reason");
                if (!TextUtils.isEmpty(error)) {
                    callbacks.onError(new AuthenticationException(error + ", reason : " + errorReason + "(" + errorDescription + ")"));
                    return false;
                }
            }
        }
        return false;
    }

    public static void SavePreferences(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TOKEN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, token);
        editor.apply();
    }

//    private void LoadPreferences() {
//        SharedPreferences sharedPreferences = activity.getSharedPreferences(LAST_INDEX_CHECK, activity.MODE_PRIVATE);
//        int savedRadioIndex = sharedPreferences.getInt(KEY_SAVED_RADIO_BUTTON_INDEX, 0);
//        RadioButton savedCheckedRadioButton = (RadioButton) radioGroup.getChildAt(savedRadioIndex);
//        savedCheckedRadioButton.setChecked(true);
//    }
}

