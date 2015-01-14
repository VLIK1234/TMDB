package com.example.vlik1234.tmdb.source;

import android.content.Context;

import com.example.vlik1234.tmdb.CoreApplication;

import java.io.InputStream;

/**
 * Created by VLIK on 22.11.2014.
 */
public class TMDBDataSource extends HttpDataSource {

    public static final String KEY = "TMDBDataSource";

    public static TMDBDataSource get(Context context) {
        return CoreApplication.get(context, KEY);
    }

    @Override
    public InputStream getResult(String baseUrlAndAppResponse) throws Exception {
        StringBuilder sb = new StringBuilder();

        sb.append(baseUrlAndAppResponse);
        sb.append("?api_key=f413bc4bacac8dff174a909f8ef535ae&language=");
        //Locale.getDefault().getLanguage()
        sb.append("en");

        return super.getResult(sb.toString());
    }

}

