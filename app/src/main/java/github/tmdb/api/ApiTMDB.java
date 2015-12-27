package github.tmdb.api;

import android.support.annotation.StringDef;
import android.text.TextUtils;
import android.util.Log;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import github.tmdb.utils.TextUtilsImpl;

/**
 @author IvanBakach
 @version on 22.11.2014
 */
public class ApiTMDB {

    public static final String POSTER_45X68_BACKDROP_45X25 = "w45";
    public static final String POSTER_92X138_BACKDROP_92X52 = "w92";
    public static final String POSTER_154X231_BACKDROP_154X87 = "w154";
    public static final String POSTER_185X278_BACKDROP_185X104 = "w185";
    public static final String POSTER_300X450_BACKDROP_300X169 = "w300";
    public static final String POSTER_342X513_BACKDROP_342X192 = "w342";
    public static final String POSTER_500X750_BACKDROP_500X281 = "w500";
    public static final String POSTER_421X632_BACKDROP_1124X632 = "h632";
    public static final String POSTER_780X1170_BACKDROP_780X439 = "w780";
    public static final String POSTER_1000X1500_BACKDROP_1000X563 = "w1000";
    public static final String POSTER_1280X1920_BACKDROP_1280X720 = "w1280";
    public static final String POSTER_ORIGINAL_BACKDROP_ORIGINAL = "original";

    @StringDef({POSTER_45X68_BACKDROP_45X25, POSTER_92X138_BACKDROP_92X52, POSTER_154X231_BACKDROP_154X87, POSTER_185X278_BACKDROP_185X104,
            POSTER_300X450_BACKDROP_300X169, POSTER_342X513_BACKDROP_342X192, POSTER_500X750_BACKDROP_500X281, POSTER_421X632_BACKDROP_1124X632,
            POSTER_780X1170_BACKDROP_780X439, POSTER_1000X1500_BACKDROP_1000X563, POSTER_1280X1920_BACKDROP_1280X720, POSTER_ORIGINAL_BACKDROP_ORIGINAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ImageScale {}

    public static final String SEARCH_TYPE_PHRASE = "phrase";
    public static final String SEARCH_TYPE_NGRAM = "ngram";
    @StringDef({SEARCH_TYPE_PHRASE, SEARCH_TYPE_NGRAM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SearchType {}

    public static final String APPEND_TO_RESPONSE_ALTERNATIVE_TITLE = "alternative_titles";
    public static final String APPEND_TO_RESPONSE_CREDITS = "credits";
    public static final String APPEND_TO_RESPONSE_IMAGES = "images";
    public static final String APPEND_TO_RESPONSE_KEYWORDS = "keywords";
    public static final String APPEND_TO_RESPONSE_RELEASES = "releases";
    public static final String APPEND_TO_RESPONSE_VIDEOS = "videos";
    public static final String APPEND_TO_RESPONSE_TRANSLATIONS = "translations";
    public static final String APPEND_TO_RESPONSE_SIMILAR = "similar";
    public static final String APPEND_TO_RESPONSE_REVIEWS = "reviews";
    public static final String APPEND_TO_RESPONSE_LISTS = "lists";
    @StringDef({APPEND_TO_RESPONSE_ALTERNATIVE_TITLE, APPEND_TO_RESPONSE_CREDITS, APPEND_TO_RESPONSE_IMAGES, APPEND_TO_RESPONSE_KEYWORDS,
            APPEND_TO_RESPONSE_RELEASES, APPEND_TO_RESPONSE_VIDEOS, APPEND_TO_RESPONSE_TRANSLATIONS, APPEND_TO_RESPONSE_SIMILAR,
            APPEND_TO_RESPONSE_REVIEWS, APPEND_TO_RESPONSE_LISTS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AppendToResponse {}

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

    public static String getSearchMovie(String query, @SearchType String searchType) {
        StringBuilder url = new StringBuilder(BASE_PATH + SEARCH_MOVIE);
        url.append(sign(url.toString(), QUERY)).append(query);
        url.append(sign(url.toString(), SEARCH_TYPE)).append(searchType);
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

    public static String getImagePath(@ImageScale String sizePoster, String imageKey) {
        return !TextUtilsImpl.isEmpty(imageKey) ? IMAGE_PATH_TMDB + sizePoster + imageKey : null;
    }
}
