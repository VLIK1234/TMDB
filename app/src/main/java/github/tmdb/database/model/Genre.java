package github.tmdb.database.model;

import android.provider.BaseColumns;

import com.google.gson.annotations.SerializedName;

import by.istin.android.xcore.annotations.dbLong;
import by.istin.android.xcore.annotations.dbString;

/**
 * @author Ivan Bakach
 * @version on 03.01.2016
 */
public class Genre implements BaseColumns{

    public static final String GENRE_NAME = "genre_name";

    @dbLong
    @SerializedName(value = "id")
    public static final String ID = _ID;

    @dbString
    public static final String NAME = "name";

    @dbLong
    public static final String MOVIE_ID = "movieId";

    @dbLong
    public static final String SERIES_ID = "seriesId";
}
