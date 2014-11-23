package com.example.vlik1234.tmdb.processing;

import com.example.vlik1234.tmdb.bo.Film;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FilmArrayProcessor implements Processor<List<Film>,InputStream>{

    @Override
    public List<Film> process(InputStream inputStream) throws Exception {
        String string = new StringProcessor().process(inputStream);
        JSONArray array = new JSONObject(string).getJSONArray("results");
        //TODO wrapper for array
        List<Film> noteArray = new ArrayList<Film>(array.length());
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            Film friend = new Film(jsonObject);
            friend.initTitle();
            noteArray.add(friend);
        }
        return noteArray;
    }

}
