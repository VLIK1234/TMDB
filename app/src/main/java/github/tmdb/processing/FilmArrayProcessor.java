package github.tmdb.processing;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import github.tmdb.bo.Film;

public class FilmArrayProcessor extends WrapperArrayProcessor<Film> implements Processor<List<Film>, InputStream> {

    @Override
    public List<Film> process(InputStream inputStream) throws Exception {
        String string = new StringProcessor().process(inputStream);
        JSONArray array = new JSONObject(string).getJSONArray("results");

        List<Film> noteArray = new ArrayList<>(array.length());
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            Film film = new Film(jsonObject);
            noteArray.add(film);
        }
        return noteArray;
    }

    @Override
    protected Film createObject(JSONObject jsonObject) {
        return null;
    }
}