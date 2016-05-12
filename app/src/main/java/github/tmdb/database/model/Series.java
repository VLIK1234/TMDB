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
 * @author Ivan_Bakach
 * @version on 5/12/16
 */
public class Series implements BaseColumns, IBeforeArrayUpdate {

    @dbLong
    @SerializedName(value = "id")
    public static final String ID = _ID;

    @dbString
    public static final String BACKDROP_PATH = "backdrop_path";

    @dbString
    public static final String FIRSRT_AIR_DATE = "first_air_date";

    @dbString
    public static final String ORIGINAL_LANGUAGE = "original_language";

    @dbString
    public static final String ORIGINAL_NAME = "original_name";

    @dbString
    public static final String OVERVIEW = "overview";

    @dbString
    public static final String POSTER_PATH = "poster_path";

    @dbDouble
    public static final String POPULARITY = "popularity";

    @dbString
    public static final String NAME = "name";

    @dbDouble
    public static final String VOTE_AVERAGE = "vote_average";

    @dbInteger
    public static final String VOTE_COUNT = "vote_count";
    @dbInteger
    public static final String POSITION = "position";

    @Override
    public void onBeforeListUpdate(DBHelper dbHelper, IDBConnection dbConnection, DataSourceRequest dataSourceRequest, int position, ContentValues contentValues) {
        if (dataSourceRequest == null) {
            return;
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
