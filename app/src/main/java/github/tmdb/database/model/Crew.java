package github.tmdb.database.model;

import android.provider.BaseColumns;

import com.google.gson.annotations.SerializedName;

import by.istin.android.xcore.annotations.dbLong;
import by.istin.android.xcore.annotations.dbString;

/**
 * @author Ivan Bakach
 * @version on 03.01.2016
 */
public class Crew implements BaseColumns {

    @dbLong
    @SerializedName(value = "id")
    public static final String ID = _ID;

    @dbString
    public static final String DEPARTMENT = "department";

    @dbString
    public static final String CREDIT_ID = "credit_id";

    @dbString
    public static final String JOB = "job";

    @dbLong
    public static final String EXTERNAL_ID = "id";

    @dbString
    public static final String NAME = "name";

    @dbString
    public static final String PROFILE_PATH = "profile_path";
}
