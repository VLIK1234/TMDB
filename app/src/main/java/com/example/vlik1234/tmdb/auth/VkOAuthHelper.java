package com.example.vlik1234.tmdb.auth;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.example.vlik1234.tmdb.VkLoginActivity;
import com.example.vlik1234.tmdb.auth.secure.EncrManager;
import org.apache.http.auth.AuthenticationException;

/**
 * Created by IstiN on 29.10.2014.
 */
public class VkOAuthHelper {

    public static interface Callbacks {

        void onError(Exception e);

        void onSuccess();

    }

    private static String sToken;
    public static final String REDIRECT_URL = "https://oauth.vk.com/blank.html";
    public static final String AUTORIZATION_URL = "https://oauth.vk.com/authorize?client_id=4616332&scope=offline,wall,photos,status&redirect_uri=" + REDIRECT_URL + "&display=touch&response_type=token";

    public static String sign(String url) {
            return url + "?api_key=f413bc4bacac8dff174a909f8ef535ae";

    }

    public static boolean isLogged() {
        return !TextUtils.isEmpty(sToken);
    }

    public static boolean proceedRedirectURL(Activity activity, String url, Callbacks callbacks) {
        if (url.startsWith(REDIRECT_URL)) {
            Uri uri = Uri.parse(url);
            String fragment = uri.getFragment();
            Uri parsedFragment = Uri.parse("http://temp.com?" + fragment);
            String accessToken = parsedFragment.getQueryParameter("access_token");
            if (!TextUtils.isEmpty(accessToken)) {
                //TODO save sToken to the secure store
                //TODO create account in account manager
                sToken = accessToken;
                callbacks.onSuccess();
                return true;
            } else {
                String error = parsedFragment.getQueryParameter("error");
                String errorDescription = parsedFragment.getQueryParameter("error_description");
                String errorReason = parsedFragment.getQueryParameter("error_reason");
                if (!TextUtils.isEmpty(error)) {
                    callbacks.onError(new AuthenticationException(error+", reason : " + errorReason +"("+errorDescription+")"));
                    return false;
                } else {
                    //WTF?
                }
            }
        }
        return false;
    }
}
