package github.tmdb.core.model;

import android.provider.BaseColumns;

import com.google.gson.annotations.SerializedName;

import by.istin.android.xcore.annotations.dbDouble;
import by.istin.android.xcore.annotations.dbInteger;
import by.istin.android.xcore.annotations.dbLong;
import by.istin.android.xcore.annotations.dbString;

/**
 * @author Ivan Bakach
 * @version on 02.01.2016
 */
public class MovieEntity implements BaseColumns {

    @dbLong
    @SerializedName(value = "id")
    public static final String ID = "id";

    @dbString
    public static final String POSTER_PATH = "poster_path";

    @dbString
    public static final String ADULT = "adult";//boolean

    @dbString
    public static final String OVERVIEW = "overview";

    @dbString
    public static final String RELEASE_DATE = "release_date";

    @dbString
    public static final String ORIGINAL_TITLE = "original_title";

    @dbString
    public static final String ORIGINAL_LANGUAGE = "original_language";

    @dbString
    public static final String TITLE = "title";

    @dbString
    public static final String BACKDROP_PATH = "backdrop_path";

    @dbDouble
    public static final String POPULARITY = "popularity";

    @dbInteger
    public static final String VOTE_COUNT = "vote_count";

    @dbString
    public static final String VIDEO = "video";//boolean

    @dbDouble
    public static final String VOTE_AVERAGE = "vote_average";
}
