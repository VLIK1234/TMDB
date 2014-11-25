package com.example.vlik1234.tmdb.processing;

import com.example.vlik1234.tmdb.bo.Film;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASUS on 24.11.2014.
 */
public class FilmProcessor implements Processor<Film,InputStream>{

    @Override
    public Film process(InputStream inputStream) throws Exception {
        String string = new StringProcessor().process(inputStream);
       JSONObject jsonObject = new JSONObject(string);


        Film film = new Film(jsonObject);
        film.initTitle();

        return film;
    }
}
