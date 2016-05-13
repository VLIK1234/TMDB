package github.tmdb.database.cursor;

import android.database.Cursor;

import by.istin.android.xcore.model.CursorModel;
import github.tmdb.api.ApiTMDB;
import github.tmdb.database.model.MovieItemEntity;

public class MoviesListCursor extends CursorModel {

    private MoviesListCursor(Cursor cursor) {
        super(cursor);
    }

    public MoviesListCursor(Cursor cursor, boolean isMoveToFirst) {
        super(cursor, isMoveToFirst);
    }

    public final static CursorModelCreator CREATOR = new CursorModelCreator() {
        @Override
        public CursorModel create(Cursor cursor) {
            return new MoviesListCursor(cursor);
        }
    };

    public long getId() {
        return getLong(MovieItemEntity.ID);
    }

    public String getTitle() {
        return getString(MovieItemEntity.TITLE);
    }

    public String getReleaseDate() {
        return getString(MovieItemEntity.RELEASE_DATE);
    }

    public String getBackdropPath(@ApiTMDB.ImageScale String sizeImage) {
        return ApiTMDB.getImagePath(sizeImage, getString(MovieItemEntity.BACKDROP_PATH));
    }

    public String getPosterPath(@ApiTMDB.ImageScale String sizeImage) {
        return ApiTMDB.getImagePath(sizeImage, getString(MovieItemEntity.POSTER_PATH));
    }

    public float getVoteAverage() {
        return getFloat(MovieItemEntity.VOTE_AVERAGE);
    }

    public int getVoteCount() {
        return getInt(MovieItemEntity.VOTE_COUNT);
    }
}