package github.tmdb.database;

import android.provider.BaseColumns;

/**
 * Created by ASUS on 12.04.2015.
 */
public class TMDBContract {
    private TMDBContract(){

    }

    public static abstract class FilmTable implements BaseColumns {
        public static final String TABLE_NAME = "film";
        public static final String COLUMN_FILM_ID = "filmid";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RUNTIME = "runtime";
    }
}
