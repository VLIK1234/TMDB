package github.tmdb.database.cursor;

import android.database.Cursor;

import by.istin.android.xcore.model.CursorModel;
import github.tmdb.api.ApiTMDB;
import github.tmdb.database.model.SearchItem;

/**
 * @author Ivan Bakach
 * @version on 04.06.2016
 */
public class SearchCursor extends CursorModel {
    public SearchCursor(Cursor cursor) {
        super(cursor);
    }

    public SearchCursor(Cursor cursor, boolean isMoveToFirst) {
        super(cursor, isMoveToFirst);
    }

    public final static CursorModelCreator CREATOR = new CursorModelCreator() {
        @Override
        public CursorModel create(Cursor cursor) {
            return new SearchCursor(cursor);
        }
    };

    public long getId() {
        return getLong(SearchItem.ID);
    }

    public String getMediaType() {
        return getString(SearchItem.MEDIA_TYPE);
    }

    public String getPosterPath(@ApiTMDB.ImageScale String sizeImage) {
        return ApiTMDB.getImagePath(sizeImage, getString(SearchItem.POSTER_PATH));
    }

    public String getProfilePath(@ApiTMDB.ImageScale String sizeImage) {
        return ApiTMDB.getImagePath(sizeImage, getString(SearchItem.PROFILE_PATH));
    }

    public String getOverview() {
        return getString(SearchItem.OVERVIEW);
    }

    public String getName() {
        return getString(SearchItem.NAME);
    }

    public String getMovieTitle() {
        return getString(SearchItem.TITLE);
    }
}
