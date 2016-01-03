package github.tmdb.core.cursor;

import android.database.Cursor;

import by.istin.android.xcore.db.impl.DBHelper;
import by.istin.android.xcore.model.CursorModel;
import github.tmdb.core.model.Genre;
import github.tmdb.core.model.MovieDetailEntity;

public class MoviesDetailCursor extends CursorModel {

    public static String SQL_REQUEST = new StringBuilder()
            .append("SELECT ")
            .append("* ")
            .append("FROM " + DBHelper.getTableName(MovieDetailEntity.class) + " m ")
            .append("LEFT JOIN " + DBHelper.getTableName(Genre.class) + " g ")
            .toString();

    public static String DETAIL_SQL_REQUEST = new StringBuilder()
            .append("SELECT ")
            .append("* ")
            .append("FROM " + DBHelper.getTableName(MovieDetailEntity.class) + " ")
            .append("WHERE " + MovieDetailEntity.ID + " = %s")
            .toString();

    public MoviesDetailCursor(Cursor cursor) {
        super(cursor);
    }

    public MoviesDetailCursor(Cursor cursor, boolean isMoveToFirst) {
        super(cursor, isMoveToFirst);
    }

    public final static CursorModelCreator CREATOR = new CursorModelCreator() {
        @Override
        public CursorModel create(Cursor cursor) {
            return new MoviesDetailCursor(cursor);
        }
    };

    public static String getDetailSqlRequest(long idMovie){
        return String.format(DETAIL_SQL_REQUEST, idMovie);
    }
}