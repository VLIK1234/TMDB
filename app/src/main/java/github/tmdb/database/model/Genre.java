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

    @dbLong
    @SerializedName(value = "id")
    public static final String ID = _ID;

    public static final String ID_KEY = "id";

    @dbString
    public static final String NAME = "name";

    @dbLong
    public static final String MOVIE_ID = "movieId";
}
