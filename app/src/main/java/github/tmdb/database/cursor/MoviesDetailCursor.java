package github.tmdb.database.cursor;

import android.database.Cursor;

import by.istin.android.xcore.db.impl.DBHelper;
import by.istin.android.xcore.model.CursorModel;
import github.tmdb.database.model.Genre;
import github.tmdb.database.model.MovieDetailEntity;

public class MoviesDetailCursor extends CursorModel {

    public static String SQL_REQUEST = "SELECT " + "m.* " + "FROM " + DBHelper.getTableName(MovieDetailEntity.class) + " m " + "WHERE m." + MovieDetailEntity.ID + " = %1$s";

//    SELECT s.SupplierID, s.SupplierName, GROUP_CONCAT(p.ProductName) FROM Suppliers s LEFT JOIN Products p ON p.SupplierID = 11 WHERE s.SupplierID = 11
    private static final String DETAIL_SQL_REQUEST = "SELECT m.*, "
        + "GROUP_CONCAT(g." + Genre.NAME + ", ' | ') AS " + Genre.GENRE_NAME +
        " FROM " + DBHelper.getTableName(MovieDetailEntity.class) + " m "
        + "LEFT JOIN " + DBHelper.getTableName(Genre.class) + " g ON g."
        + Genre.MOVIE_ID + " = %1$d " + "WHERE m." + MovieDetailEntity._ID + " = %1$d";
    private MoviesDetailCursor(Cursor cursor) {
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