package github.tmdb.database.model;

import android.provider.BaseColumns;

import com.google.gson.annotations.SerializedName;

import by.istin.android.xcore.annotations.dbDouble;
import by.istin.android.xcore.annotations.dbLong;
import by.istin.android.xcore.annotations.dbString;

/**
 * @author IvanBakach
 * @version on 26.02.2016
 */
public class Person implements BaseColumns{

    @dbLong
    @SerializedName(value = "id")
    public static final String ID = _ID;

    @dbString
    public static final String ADULT = "adult";

    @dbString
    public static final String BIOGRAPHY = "biography";

    @dbString
    public static final String BIRTHDAY = "birthday"; //    birthday: "1955-08-07",

    @dbString
    public static final String DEATHDAY = "deathday";

    @dbString
    public static final String HOMEPAGE = "homepage";

    @dbString
    public static final String IMDB_ID = "imdb_id";

    @dbString
    public static final String NAME = "name";

    @dbString
    public static final String PLACE_OF_BIRTH = "place_of_birth";

    @dbDouble
    public static final String POPULARITY = "popularity";

    @dbString
    public static final String PROFILE_PATH = "profile_path";
}
