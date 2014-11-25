package com.example.vlik1234.tmdb;

/**
 * Created by ASUS on 22.11.2014.
 */
public class ApiTMDB {
    public static final String BASE_PATH = "https://api.themoviedb.org/3";
    public static final String DISCOVER_MOVIE = "/discover/movie";
    public static final String NOW_PLAYING = "/movie/now_playing";
    private static final String MOVIE = "/movie/";

    public static final String DISCOVER_MOVIE_GET = BASE_PATH + DISCOVER_MOVIE;
    public static final String NOW_PLAYING_GET = BASE_PATH + NOW_PLAYING;
    public static final String getMovie(String id){return BASE_PATH + MOVIE + id;}


}
