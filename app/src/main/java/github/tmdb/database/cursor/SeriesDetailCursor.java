package github.tmdb.database.cursor;

import android.database.Cursor;

import by.istin.android.xcore.db.impl.DBHelper;
import by.istin.android.xcore.model.CursorModel;
import github.tmdb.database.model.Genre;
import github.tmdb.database.model.MovieDetailEntity;
import github.tmdb.database.model.SeriesDetailEntity;

public class SeriesDetailCursor extends CursorModel {

//    private static final String DETAIL_SQL_REQUEST = "SELECT s.* FROM " + DBHelper.getTableName(SeriesDetailEntity.class) + " s WHERE s." + SeriesDetailEntity._ID + " = %1$d";
    private static final String DETAIL_SQL_REQUEST = "SELECT s.*, "
            + "GROUP_CONCAT(g." + Genre.NAME + ", ' | ') AS " + Genre.GENRE_NAME
            + " FROM " + DBHelper.getTableName(SeriesDetailEntity.class) + " s "
            + "LEFT JOIN " + DBHelper.getTableName(Genre.class) + " g ON g." + Genre.SERIES_ID + " = %1$d "
            + "WHERE s." + SeriesDetailEntity._ID + " = %1$d";
//    private static final String DETAIL_SQL_REQUEST = "SELECT m.*, "
//            + "GROUP_CONCAT(g." + Genre.NAME + ", ' | ') AS " + Genre.GENRE_NAME +
//            " FROM " + DBHelper.getTableName(MovieDetailEntity.class) + " m "
//            + "LEFT JOIN " + DBHelper.getTableName(Genre.class) + " g ON g." + Genre.MOVIE_ID + " = %1$d "
//            + "AND m." + MovieDetailEntity._ID + " = %1$d";
    private SeriesDetailCursor(Cursor cursor) {
        super(cursor);
    }

    public SeriesDetailCursor(Cursor cursor, boolean isMoveToFirst) {
        super(cursor, isMoveToFirst);
    }

    public final static CursorModelCreator CREATOR = new CursorModelCreator() {
        @Override
        public CursorModel create(Cursor cursor) {
            return new SeriesDetailCursor(cursor);
        }
    };

    public static String getDetailSqlRequest(long id){
        return String.format(DETAIL_SQL_REQUEST, id);
    }
}