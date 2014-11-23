package com.example.vlik1234.tmdb.source;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.example.vlik1234.tmdb.Api;
import com.example.vlik1234.tmdb.CoreApplicationTMDB;
import com.example.vlik1234.tmdb.auth.VkOAuthHelper;

import java.io.InputStream;

/**
 * Created by ASUS on 22.11.2014.
 */
public class TMDBDataSource extends HttpDataSource {

    public static final String KEY = "TMDBDataSource";

    public static VkDataSource get(Context context) {
        return CoreApplicationTMDB.get(context, KEY);
    }

    @Override
    public InputStream getResult(String p) throws Exception {
        String signUrl = VkOAuthHelper.sign(p);
        String versionValue = Uri.parse(signUrl).getQueryParameter(Api.VERSION_PARAM);
        if (TextUtils.isEmpty(versionValue)) {
            signUrl = signUrl + "&" + Api.VERSION_PARAM + "=" + Api.VERSION_VALUE;
        }
        return super.getResult(signUrl);
    }

}

