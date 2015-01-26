package org.tmdb.processing;

import org.json.JSONObject;
import org.tmdb.bo.Film;

import java.io.InputStream;

/**
 * Created by ASUS on 24.11.2014.
 */
public class FilmProcessor implements Processor<Film, InputStream> {

    @Override
    public Film process(InputStream inputStream) throws Exception {
        String string = new StringProcessor().process(inputStream);
        JSONObject jsonObject = new JSONObject(string);

        Film film = new Film(jsonObject);
        film.initTitle();

        return film;
    }
}
