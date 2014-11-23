package com.example.vlik1234.tmdb;

/**
 * Created by ASUS on 22.11.2014.
 */
public class ApiTMDB {
    public static final String BASE_PATH = "http://api.themoviedb.org/3/";
    public static final String DISCOVER_MOVIE = "/discover/movie";
    public static final String API_KEY = "?api_key=f413bc4bacac8dff174a909f8ef535ae";

    public static final String DISCOVER_MOVIE_GET = BASE_PATH + DISCOVER_MOVIE + API_KEY;
}
