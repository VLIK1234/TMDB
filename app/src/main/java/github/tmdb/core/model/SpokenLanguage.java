package github.tmdb.core.model;

import android.provider.BaseColumns;

import com.google.gson.annotations.SerializedName;

import by.istin.android.xcore.annotations.dbLong;
import by.istin.android.xcore.annotations.dbString;

/**
 * @author Ivan Bakach
 * @version on 03.01.2016
 */
public class SpokenLanguage implements BaseColumns {

    @dbLong
    @SerializedName(value = "id")
    public static final String ID = _ID;

    @dbString
    public static final String NAME = "name";

    @dbString
    public static final String ISO_3166_1 = "iso_3166_1";

    @dbLong
    public static final String MOVIE_ID = "movieId";
}
