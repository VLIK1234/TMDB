package com.example.vlik1234.tmdb.source;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.example.vlik1234.tmdb.Api;
import com.example.vlik1234.tmdb.CoreApplication;
import com.example.vlik1234.tmdb.auth.VkOAuthHelper;

import java.io.InputStream;

/**
 * Created by ASUS on 22.11.2014.
 */
public class TMDBDataSource extends HttpDataSource {

    public static final String KEY = "TMDBDataSource";

    public static TMDBDataSource get(Context context) {
        return CoreApplication.get(context, KEY);
    }

    @Override
    public InputStream getResult(String p) throws Exception {
        String signUrl = p+"?api_key=f413bc4bacac8dff174a909f8ef535ae";
        return super.getResult(signUrl);
    }

}

