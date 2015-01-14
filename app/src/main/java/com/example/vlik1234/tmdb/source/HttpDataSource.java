package com.example.vlik1234.tmdb.source;

import android.content.Context;

import com.example.vlik1234.tmdb.CoreApplication;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class HttpDataSource implements DataSource<InputStream, String> {

    public static final String KEY = "HttpDataSource";

    public static HttpDataSource get(Context context) {
        return CoreApplication.get(context, KEY);
    }

    @Override
    public InputStream getResult(String p) throws Exception {
        //download data and return
        URL url = new URL(p);
        // Read all the text returned by the server
        return url.openStream();
    }

    public static void close(Closeable in) {
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
