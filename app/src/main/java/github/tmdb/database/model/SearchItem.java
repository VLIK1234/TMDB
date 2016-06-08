package github.tmdb.database.model;

import android.provider.BaseColumns;

import com.google.gson.annotations.SerializedName;

import by.istin.android.xcore.annotations.dbDouble;
import by.istin.android.xcore.annotations.dbLong;
import by.istin.android.xcore.annotations.dbString;

/**
 * @author Ivan Bakach
 * @version on 04.06.2016
 */
public class SearchItem implements BaseColumns {

    @SerializedName("id")
    @dbLong
    public static final String ID = _ID;

    @dbString
    public static final String MEDIA_TYPE = "media_type";

    @dbLong
    public static final String VOTE_AVERAGE = "vote_average";

    @dbString
    public static final String BACKDROP_PATH = "backdrop_path";

    @dbString
    public static final String ORIGINAL_NAME = "original_name";

    @dbString
    public static final String PROFILE_PATH = "profile_path";

    @dbString
    public static final String FIRST_AIR_DATE = "first_air_date";

    @dbString
    public static final String TITLE = "title";

    @dbString
    public static final String ORIGINAL_LANGUAGE = "original_language";

    @dbString
    public static final String OVERVIEW = "overview";

    @dbString
    public static final String NAME = "name";

    @dbString
    public static final String ORIGINAL_TITLE = "original_title";

    @dbString
    public static final String RELEASE_DATE = "release_date";

    @dbLong
    public static final String VOTE_COUNT = "vote_count";

    @dbString
    public static final String POSTER_PATH = "poster_path";

    @dbDouble
    public static final String POPULARITY = "popularity";
}
