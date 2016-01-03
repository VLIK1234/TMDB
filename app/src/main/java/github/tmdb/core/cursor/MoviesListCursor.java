package github.tmdb.core.cursor;

import android.database.Cursor;

import by.istin.android.xcore.model.CursorModel;

public class MoviesListCursor extends CursorModel {

    public MoviesListCursor(Cursor cursor) {
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

}