package org.tmdb.processing;

import org.json.JSONArray;
import org.json.JSONObject;
import org.tmdb.bo.Film;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FilmArrayProcessor implements Processor<List<Film>, InputStream> {

    @Override
    public List<Film> process(InputStream inputStream) throws Exception {
        String string = new StringProcessor().process(inputStream);
        JSONArray array = new JSONObject(string).getJSONArray("results");

        List<Film> noteArray = new ArrayList<Film>(array.length());
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            Film film = new Film(jsonObject);
            noteArray.add(film);
        }
        return noteArray;
    }
}
