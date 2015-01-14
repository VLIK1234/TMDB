package com.example.vlik1234.tmdb;

/**
 * Created by ASUS on 22.11.2014.
 */
public class ApiTMDB {

    //Poster size PSIZE_(size in ppi), h-height, w-width; if original
    public enum SizePoster{
        w45,
        w92,
        w154,
        w185,
        w300,
        w342,
        w500,
        h632,
        w780,
        w1000,
        w1280,
        original
    }

    private static final String BASE_PATH = "https://api.themoviedb.org/3/";
    private static final String DISCOVER_MOVIE = "discover/movie";
    private static final String NOW_PLAYING = "movie/now_playing";
    private static final String MOVIE = "movie/";
    private static final String TV = "tv/";
    private static final String ON_THE_AIR ="tv/on_the_air";


    public static final String ON_THE_AIR_GET = BASE_PATH + ON_THE_AIR;
    public static final String DISCOVER_MOVIE_GET = BASE_PATH + DISCOVER_MOVIE;
    public static final String NOW_PLAYING_GET = BASE_PATH + NOW_PLAYING;
    public static  String getMovie(Long id){return BASE_PATH + MOVIE + id;}
    public static  String getTV(Long id){return BASE_PATH + TV + id;}


}
