package github.tmdb.core.cursor;

import android.database.Cursor;

import by.istin.android.xcore.db.impl.DBHelper;
import by.istin.android.xcore.model.CursorModel;
import github.tmdb.core.model.Genre;
import github.tmdb.core.model.MovieDetailEntity;

public class MoviesDetailCursor extends CursorModel {

    public static String SQL_REQUEST = new StringBuilder()
            .append("SELECT ")
            .append("m.* ")
            .append("FROM " + DBHelper.getTableName(MovieDetailEntity.class) + " m ")
            .append("WHERE m." + MovieDetailEntity.ID + " = %1$s")
            .toString();

//    SELECT s.SupplierID, s.SupplierName, GROUP_CONCAT(p.ProductName) FROM Suppliers s LEFT JOIN Products p ON p.SupplierID = 11 WHERE s.SupplierID = 11
    public static String DETAIL_SQL_REQUEST = new StringBuilder()
        .append("SELECT m.*, ")
        .append("GROUP_CONCAT(g.").append(Genre.NAME).append(", ' | ') AS " + Genre.NAME)
        .append(" FROM ")
        .append(DBHelper.getTableName(MovieDetailEntity.class)).append(" m ")
        .append("LEFT JOIN ")
        .append(DBHelper.getTableName(Genre.class)).append(" g ")
        .append("ON g." + Genre.MOVIE_ID + " = %1$d ")
        .append("WHERE m." + MovieDetailEntity._ID + " = %1$d")
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