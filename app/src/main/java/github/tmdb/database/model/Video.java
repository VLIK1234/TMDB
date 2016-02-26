package github.tmdb.database.model;

import android.provider.BaseColumns;

import com.google.gson.annotations.SerializedName;

import by.istin.android.xcore.annotations.dbInteger;
import by.istin.android.xcore.annotations.dbLong;
import by.istin.android.xcore.annotations.dbString;

/**
 * @author Ivan Bakach
 * @version on 03.01.2016
 */
public class Video implements BaseColumns {

    @dbLong
    @SerializedName(value = "id")
    public static final String ID = _ID;

    @dbLong
    public static final String EXTERNAL_ID = "id";

    @dbString
    public static final String NAME = "name";

    @dbString
    public static final String ISO_639_1 = "iso_639_1";

    @dbString
    public static final String KEY = "key";

    @dbString
    public static final String SITE = "site";

    @dbInteger
    public static final String SIZE = "size";

    @dbString
    public static final String TYPE = "type";

    @dbLong
    public static final String MOVIE_ID = "movieId";
}
