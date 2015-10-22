package github.tmdb.api;

import android.text.TextUtils;
import android.util.Log;

import github.tmdb.utils.TextUtilsImpl;

/**
 @author IvanBakach
 @version on 22.11.2014
 */
public class ApiTMDB {

    //Poster size PSIZE_(size in ppi), h-height, w-width; if original
    public enum SizePoster {
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

    public enum SearchType {
        phrase,//-by contains phrase
        ngram//-by autocomplete
    }

    public static String sign(String url, String constant) {
        if (url.contains("?")) {
            return "&" + constant;
        } else {
            return "?" + constant;
        }
    }

    private static final String BASE_PATH = "https://api.themoviedb.org/3/";
    private static final String IMAGE_PATH_TMDB = "https://image.tmdb.org/t/p/";

    private static final String DISCOVER_MOVIE = "discover/movie";
    private static final String NOW_PLAYING = "movie/now_playing";
    private static final String MOVIE = "movie/";
    private static final String TV = "tv/";
    private static final String ON_THE_AIR = "tv/on_the_air";
    private static final String SEARCH_MOVIE = "search/movie";

    private static final String PAGE = "page=";
    private static final String QUERY = "query=";
    private static final String SEARCH_TYPE = "search_type=";
    private static final String LANGUAGE = "language=";

    public static final String ON_THE_AIR_GET = BASE_PATH + ON_THE_AIR;
    public static final String DISCOVER_MOVIE_GET = BASE_PATH + DISCOVER_MOVIE;

    public static String getPage(String url, int page) {
        return sign(url, PAGE) + page;
    }

    public static String getLanguage(String url) {
        return sign(url, LANGUAGE);
    }

    public static String getSearchMovie(String query) {
        StringBuilder url = new StringBuilder(BASE_PATH + SEARCH_MOVIE);
        url.append(sign(url.toString(), QUERY)).append(query);
        url.append(sign(url.toString(), SEARCH_TYPE)).append(SearchType.phrase);
        return url.toString();
    }

    public static String getNowPlayingGet() {
        return BASE_PATH + NOW_PLAYING;
    }

    public static String getMovie(Long id) {
        return BASE_PATH + MOVIE + id;
    }

    public static String getTV(Long id) {
        return BASE_PATH + TV + id;
    }

    public static String getImagePath(SizePoster sizePoster, String imageKey) {
        return !TextUtilsImpl.isEmpty(imageKey) ? IMAGE_PATH_TMDB + sizePoster + imageKey : null;
    }
}
