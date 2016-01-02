package github.tmdb.core.model;

import android.provider.BaseColumns;

import com.google.gson.annotations.SerializedName;

import by.istin.android.xcore.annotations.dbLong;
import by.istin.android.xcore.annotations.dbString;

/**
 * Created by IstiN on 13.11.13.
 */
public class SampleEntity implements BaseColumns {

    @dbLong
    @SerializedName(value = "id")
    public static final String ID = _ID;

    @dbString
    public static final String TITLE = "title";

    @dbString
    public static final String ABOUT = "about";

    @dbString
    public static final String IMAGE_URL = "image_url";

}
