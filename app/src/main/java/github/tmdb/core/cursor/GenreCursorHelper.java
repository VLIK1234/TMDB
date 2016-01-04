package github.tmdb.core.cursor;
import by.istin.android.xcore.db.impl.DBHelper;
import github.tmdb.core.model.Genre;

/**
 * @author IvanBakach
 * @version on 17.11.2015
 */
public class GenreCursorHelper {

    public final static String SQL_BY_MOVIE;
//    public final static String SQL_BY_SERIES;

    static {
        SQL_BY_MOVIE = new StringBuilder()
                .append("SELECT * ")
                .append(" FROM ")
                .append(DBHelper.getTableName(Genre.class))
                .append(" WHERE ")
                .append(Genre.MOVIE_ID)
                .append(" == ")
                .append(" %d ")
                .toString();
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
