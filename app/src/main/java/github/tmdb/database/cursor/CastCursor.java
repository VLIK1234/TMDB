package github.tmdb.database.cursor;

import android.database.Cursor;

import by.istin.android.xcore.model.CursorModel;
import github.tmdb.api.ApiTMDB;
import github.tmdb.database.model.Cast;

public class CastCursor extends CursorModel {

    private CastCursor(Cursor cursor) {
        super(cursor);
    }

    public CastCursor(Cursor cursor, boolean isMoveToFirst) {
        super(cursor, isMoveToFirst);
    }

    public final static CursorModelCreator CREATOR = new CursorModelCreator() {
        @Override
        public CursorModel create(Cursor cursor) {
            return new CastCursor(cursor);
        }
    };

    public String getCharacter(){
        return getString(Cast.CHARACTER);
    }

    public String getCreditId(){
        return getString(Cast.CREDIT_ID);
    }

    public String getName(){
        return getString(Cast.NAME);
    }

    public String getProfilePath() {
        return ApiTMDB.getImagePath(ApiTMDB.POSTER_154X231_BACKDROP_154X87, getString(Cast.PROFILE_PATH));
    }

    public long getId() {
        return getLong(Cast.ID);
    }

}