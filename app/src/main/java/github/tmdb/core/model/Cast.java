package github.tmdb.core.model;

import android.provider.BaseColumns;

import com.google.gson.annotations.SerializedName;

import by.istin.android.xcore.annotations.dbLong;
import by.istin.android.xcore.annotations.dbString;

/**
 * @author Ivan Bakach
 * @version on 03.01.2016
 */
public class Cast implements BaseColumns {

    @dbLong
    @SerializedName(value = "id")
    public static final String ID = _ID;

    @dbLong
    public static final String CAST_ID = "cast_id";

    @dbString
    public static final String CHARACTER = "character";

    @dbString
    public static final String CREDIT_ID = "credit_id";

    @dbLong
    public static final String EXTERNAL_ID = "id";

    @dbString
    public static final String NAME = "name";
//
//    @dbInteger
//    public static final String ORDER = "order";

    @dbString
    public static final String PROFILE_PATH = "profile_path";
}
