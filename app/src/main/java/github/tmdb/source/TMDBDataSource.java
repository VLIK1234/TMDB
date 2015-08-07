package github.tmdb.source;

import android.content.Context;

import java.io.InputStream;

import github.tmdb.CoreApplication;

/**
 @author IvanBakach
 @version on 22.11.2014
 */
public class TMDBDataSource extends HttpDataSource {

    public static final String KEY = "TMDBDataSource";
    private static final String API_KEY = "f413bc4bacac8dff174a909f8ef535ae";

    public static TMDBDataSource get(Context context) {
        return CoreApplication.get(context, KEY);
    }

    public static String sign(String url) {
        if (url.contains("?")) {
            return url + "&api_key=" + API_KEY;
        } else {
            return url + "?api_key=" + API_KEY;
        }
    }

    @Override
    public InputStream getResult(String baseUrlAndAppResponse) throws Exception {
        return super.getResult(sign(baseUrlAndAppResponse));
    }

}

