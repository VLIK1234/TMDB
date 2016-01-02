package github.tmdb.core.cursor;

import android.database.Cursor;

import by.istin.android.xcore.model.CursorModel;

public class FollowListCursor extends CursorModel {


    public FollowListCursor(Cursor cursor) {
        super(cursor);
    }

    public FollowListCursor(Cursor cursor, boolean isMoveToFirst) {
        super(cursor, isMoveToFirst);
    }

    public final static CursorModelCreator CREATOR = new CursorModelCreator() {
        @Override
        public CursorModel create(Cursor cursor) {
            return new FollowListCursor(cursor);
        }
    };

}