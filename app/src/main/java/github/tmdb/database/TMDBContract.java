package github.tmdb.database;

import android.provider.BaseColumns;

/**
 @author IvanBakach
 @version on 12.04.2015
 */
public class TMDBContract {
    private TMDBContract(){

    }

    public static abstract class ActorTable implements BaseColumns {
        public static final String TABLE_NAME = "Actor";
        public static final String COLUMN_NAME_ACTOR = "name_actor";
    }

    public static abstract class FilmTable implements BaseColumns {
        public static final String TABLE_NAME = "Film";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RUNTIME = "runtime";
        public static final String COLUMN_LIST_ACTOR = "list_actor";
        public static final String COLUMN_LIST_GENRE = "list_genre";
    }
    public static abstract class GenreTable implements BaseColumns {
        public static final String TABLE_NAME = "Genre";
        public static final String COLUMN_NAME_ACTOR = "name_actor";
    }
    public static abstract class ListActorTable implements BaseColumns {
        public static final String TABLE_NAME = "List_actor";
        public static final String COLUMN_NAME_ACTOR = "name_actor";
    }
    public static abstract class ListGenreTable implements BaseColumns {
        public static final String TABLE_NAME = "List_genre";
        public static final String COLUMN_NAME_ACTOR = "name_actor";
    }

}
