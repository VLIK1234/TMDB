package com.example.vlik1234.tmdb.processing;

import com.example.vlik1234.tmdb.bo.Film;

import org.json.JSONObject;

public class FilmArrayProcessor extends WrapperArrayProcessor<Film> {

    @Override
    protected Film createObject(JSONObject jsonObject) {
        return new Film(jsonObject);
    }
}
