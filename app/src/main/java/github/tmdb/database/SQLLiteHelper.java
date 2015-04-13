package github.tmdb.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ASUS on 12.04.2015.
 */
public class SQLLiteHelper extends SQLiteOpenHelper {

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ", ";
    private static final String OPEN_PATHESIS = "( ";
    private static final String CLOSE_PATHESIS = " )";
    private static final String NOT_NULL = " NOT NULL";
    private static final String PRIMARY_KEY = " PRIMARY KEY";
    private static final String FOREIGN_KEY = " FOREIGN KEY ";
    private static final String REFERENCES = " REFERENCES ";
    private static final String CREATE_TABLE = "CREATE TABLE ";

    private static final String CREATE_ACTOR_TABLE = CREATE_TABLE + "Actor " + OPEN_PATHESIS + TMDBContract.ActorTable._ID + INTEGER_TYPE + NOT_NULL + PRIMARY_KEY + COMMA_SEP +
            TMDBContract.ActorTable.COLUMN_NAME_ACTOR + TEXT_TYPE + NOT_NULL + CLOSE_PATHESIS;
    private static final String CREATE_FILM_TABLE = CREATE_TABLE + "Film" + OPEN_PATHESIS + TMDBContract.FilmTable._ID + INTEGER_TYPE + NOT_NULL + PRIMARY_KEY + COMMA_SEP+
            TMDBContract.FilmTable.COLUMN_TITLE+TEXT_TYPE+NOT_NULL+COMMA_SEP+TMDBContract.FilmTable.COLUMN_OVERVIEW+TEXT_TYPE+COMMA_SEP+
            TMDBContract.FilmTable.COLUMN_RUNTIME+INTEGER_TYPE+COMMA_SEP+TMDBContract.FilmTable.COLUMN_LIST_ACTOR+INTEGER_TYPE+NOT_NULL+COMMA_SEP+
            TMDBContract.FilmTable.COLUMN_LIST_GENRE+INTEGER_TYPE+NOT_NULL+COMMA_SEP+FOREIGN_KEY+"("+TMDBContract.FilmTable.COLUMN_LIST_ACTOR+")"+
            REFERENCES+TMDBContract.FilmTable.COLUMN_LIST_ACTOR+"("+TMDBContract.ListActorTable._ID+")"+COMMA_SEP+
            FOREIGN_KEY+"("+TMDBContract.FilmTable.COLUMN_LIST_GENRE+")"+
            REFERENCES+TMDBContract.FilmTable.COLUMN_LIST_ACTOR+"("+TMDBContract.ListActorTable._ID+")";
    private static final String SQL_CREATE_BASE =
            CREATE_TABLE + TMDBContract.FilmTable.TABLE_NAME + " (" +
                    TMDBContract.FilmTable._ID + " INTEGER PRIMARY KEY" + NOT_NULL + COMMA_SEP +
                    TMDBContract.FilmTable.COLUMN_TITLE + TEXT_TYPE + COMMA_SEP +
                    TMDBContract.FilmTable.COLUMN_OVERVIEW + TEXT_TYPE + " )";

    private static final String SQL_DELETE_BASE =
            "DROP TABLE IF EXISTS " + TMDBContract.FilmTable.TABLE_NAME;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FilmBase.db";

    public SQLLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_BASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_BASE);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
