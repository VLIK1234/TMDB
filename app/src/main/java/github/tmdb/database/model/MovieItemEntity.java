package github.tmdb.database.model;

import android.content.ContentValues;
import android.provider.BaseColumns;

import com.google.gson.annotations.SerializedName;

import by.istin.android.xcore.annotations.dbBoolean;
import by.istin.android.xcore.annotations.dbDouble;
import by.istin.android.xcore.annotations.dbInteger;
import by.istin.android.xcore.annotations.dbLong;
import by.istin.android.xcore.annotations.dbString;
import by.istin.android.xcore.db.IDBConnection;
import by.istin.android.xcore.db.entity.IBeforeArrayUpdate;
import by.istin.android.xcore.db.impl.DBHelper;
import by.istin.android.xcore.source.DataSourceRequest;
import by.istin.android.xcore.utils.StringUtil;

/**
 * @author Ivan Bakach
 * @version on 02.01.2016
 */
public class MovieItemEntity implements BaseColumns, IBeforeArrayUpdate {

    @dbLong
    @SerializedName(value = "id")
    public static final String ID = _ID;

    @dbString
    public static final String POSTER_PATH = "poster_path";

    @dbBoolean
    public static final String ADULT = "adult";

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

    @dbBoolean
    public static final String VIDEO = "video";

    @dbDouble
    public static final String VOTE_AVERAGE = "vote_average";

    @dbInteger
    public static final String POSITION = "position";

    @dbString
    public static final String CATEGORY = "category";


    @Override
    public void onBeforeListUpdate(DBHelper dbHelper, IDBConnection dbConnection, DataSourceRequest dataSourceRequest, int position, ContentValues contentValues) {
        if (dataSourceRequest == null) {
            return;
        }

        String urlSource = StringUtil.isEmpty(dataSourceRequest.getUri()) ? null : dataSourceRequest.getUri().split("[?]")[0];

        if (!StringUtil.isEmpty(urlSource)) {
            contentValues.put(CATEGORY, urlSource);
        }

        String range = dataSourceRequest.getParam("range");

        if (!StringUtil.isEmpty(range)) {
            Integer startPosition = Integer.parseInt(range);
            contentValues.put(POSITION, position + startPosition);
        } else {
            contentValues.put(POSITION, position);
        }
    }
}
