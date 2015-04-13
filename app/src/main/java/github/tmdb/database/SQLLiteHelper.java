package github.tmdb.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ASUS on 12.04.2015.
 */
public class SQLLiteHelper extends SQLiteOpenHelper{

    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_BASE =
            "CREATE TABLE " + TMDBContract.FilmTable.TABLE_NAME + " (" +
                    TMDBContract.FilmTable._ID + " INTEGER PRIMARY KEY," +
                    TMDBContract.FilmTable.COLUMN_FILM_ID + TEXT_TYPE + COMMA_SEP +
                    TMDBContract.FilmTable.COLUMN_TITLE + TEXT_TYPE + COMMA_SEP + TMDBContract.FilmTable.COLUMN_OVERVIEW
                    + TEXT_TYPE + TMDBContract.FilmTable.COLUMN_RUNTIME + INT_TYPE + " )";

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
