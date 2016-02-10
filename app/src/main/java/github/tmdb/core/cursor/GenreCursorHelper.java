package github.tmdb.core.cursor;
import by.istin.android.xcore.db.impl.DBHelper;
import github.tmdb.core.model.Genre;
import github.tmdb.core.model.MovieDetailEntity;

/**
 * @author IvanBakach
 * @version on 17.11.2015
 */
public class GenreCursorHelper {

    public final static String SQL_BY_MOVIE_ID;
//    public final static String SQL_BY_SERIES;

    static {
        //SELECT s.*, p.ProductName FROM Products p INNER JOIN Suppliers s ON p.SupplierID = 11 AND s.SupplierID = 11
        SQL_BY_MOVIE_ID = "SELECT m.*, " + DBHelper.getTableName(MovieDetailEntity.class) + "." + Genre.NAME + " FROM " + DBHelper.getTableName(MovieDetailEntity.class) + " m " + "INNER JOIN " + DBHelper.getTableName(Genre.class) + " g " + "ON g." + Genre.MOVIE_ID + " = %1$d AND m." + MovieDetailEntity._ID + " = %1$d";
    }

//    static {
//        SQL_BY_SERIES = new StringBuilder()
//                .append("SELECT * ")
//                .append(" FROM ")
//                .append(DBHelper.getTableName(Asset.class))
//                .append(" WHERE ")
//                .append(Asset.SERIES_ID)
//                .append(" == ")
//                .append(" %d ")
//                .toString();
//    }
}
