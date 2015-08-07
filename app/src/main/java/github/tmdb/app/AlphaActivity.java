package github.tmdb.app;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import github.tmdb.R;
import github.tmdb.database.SQLLiteHelper;
import github.tmdb.database.TMDBContract;

/**
 @author IvanBakach
 @version on 21.01.2015
 */
public class AlphaActivity extends Activity {

    final String LOG_TAG = "myLogs";

    private EditText etTitle, etOverview;
    private SQLLiteHelper helper;
    private SQLiteDatabase db;

    private EditText etRuntime;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alpha);
        helper = new SQLLiteHelper(this, SQLLiteHelper.DATABASE_NAME, null, SQLLiteHelper.DATABASE_VERSION);
        etTitle = (EditText) findViewById(R.id.etTitle);
        etOverview = (EditText) findViewById(R.id.etOverview);
        etRuntime = (EditText) findViewById(R.id.etRuntime);
        final ContentValues values = new ContentValues();
//        helper.onDowngrade(db,SQLLiteHelper.DATABASE_VERSION,SQLLiteHelper.DATABASE_VERSION);
        db = helper.getWritableDatabase();
//        db.execSQL("ALTER TABLE "+ TMDBContract.FilmTable.TABLE_NAME+ " ADD "+"runtime "+"INT");

        Button btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                values.put(TMDBContract.FilmTable.COLUMN_TITLE, etTitle.getText().toString());
                values.put(TMDBContract.FilmTable.COLUMN_OVERVIEW, etOverview.getText().toString());
                values.put(TMDBContract.FilmTable.COLUMN_RUNTIME, etRuntime.getText().toString());

                long newRowId = db.insert(
                        TMDBContract.FilmTable.TABLE_NAME,
                        null,
                        values);
                Toast.makeText(getBaseContext(), String.valueOf(newRowId), Toast.LENGTH_LONG).show();

            }
        });

        Button btnRead = (Button) findViewById(R.id.btnRead);
        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Button btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.onDowngrade(db, SQLLiteHelper.DATABASE_VERSION, SQLLiteHelper.DATABASE_VERSION);
            }
        });

    }
}
