package github.tmdb.database.cursor;

import android.database.Cursor;

import by.istin.android.xcore.model.CursorModel;
import by.istin.android.xcore.utils.Log;
import github.tmdb.api.ApiTMDB;
import github.tmdb.database.model.MovieItemEntity;
import github.tmdb.database.model.Series;

/**
 * @author Ivan_Bakach
 * @version on 5/12/16
 */
public class SeriesCursor extends CursorModel {

    public SeriesCursor(Cursor cursor) {
        super(cursor);
    }

    public SeriesCursor(Cursor cursor, boolean isMoveToFirst) {
        super(cursor, isMoveToFirst);
    }

    public final static CursorModelCreator CREATOR = new CursorModelCreator() {
        @Override
        public CursorModel create(Cursor cursor) {
            return new SeriesCursor(cursor);
        }
    };

    public Long getId() {
        return getLong(Series.ID);
    }

    public String getBackdropPath(@ApiTMDB.ImageScale String sizeImage) {
        return ApiTMDB.getImagePath(sizeImage, getString(Series.BACKDROP_PATH));
    }

    public String getPosterPath(@ApiTMDB.ImageScale String sizeImage) {
        return ApiTMDB.getImagePath(sizeImage, getString(Series.POSTER_PATH));
    }

    public String getFirstAirDate() {
        return getString(Series.FIRSRT_AIR_DATE);
    }

    public String getOriginalLanguage() {
        return getString(Series.ORIGINAL_LANGUAGE);
    }

    public String getOriginalName() {
        return getString(Series.ORIGINAL_NAME);
    }

    public String getOverview() {
        return getString(Series.OVERVIEW);
    }

    public String getName() {
        return getString(Series.NAME);
    }

    public float getVoteAverage() {
        return getFloat(MovieItemEntity.VOTE_AVERAGE);
    }

    public double getPopularity() {
        return getDouble(MovieItemEntity.POPULARITY);
    }

    public int getVoteCount() {
        return getInt(MovieItemEntity.VOTE_COUNT);
    }
}
