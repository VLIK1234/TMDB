package github.tmdb.app;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

import github.tmdb.R;
import github.tmdb.database.SQLLiteHelper;
import github.tmdb.database.TMDBContract;

/**
 * Created by ASUS on 21.01.2015.
 */
public class AlphaActivity extends Activity {

    final String LOG_TAG = "myLogs";

    private Button btnAdd, btnRead, btnClear;
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
        final Random random = new Random();

        btnAdd = (Button) findViewById(R.id.btnAdd);
//        final long newRowId;

        db = helper.getWritableDatabase();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = Math.abs(random.nextInt());
                values.put(TMDBContract.FilmTable.COLUMN_FILM_ID, id);
                values.put(TMDBContract.FilmTable.COLUMN_TITLE, etTitle.getText().toString());
                values.put(TMDBContract.FilmTable.COLUMN_OVERVIEW, etOverview.getText().toString());
                values.put(TMDBContract.FilmTable.COLUMN_RUNTIME, Integer.valueOf(etRuntime.getText().toString()));

                long newRowId = db.insert(
                        TMDBContract.FilmTable.TABLE_NAME,
                        null,
                        values);
                Toast.makeText(getBaseContext(), String.valueOf(id), Toast.LENGTH_SHORT).show();
            }
        });

        btnRead = (Button) findViewById(R.id.btnRead);
        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });

        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.onDowngrade(db, SQLLiteHelper.DATABASE_VERSION, SQLLiteHelper.DATABASE_VERSION);
            }
        });

    }
}
