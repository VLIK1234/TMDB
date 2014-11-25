package com.example.vlik1234.tmdb;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vlik1234.tmdb.bo.Film;
import com.example.vlik1234.tmdb.helper.DataManager;
import com.example.vlik1234.tmdb.processing.BitmapProcessor;
import com.example.vlik1234.tmdb.processing.FilmProcessor;
import com.example.vlik1234.tmdb.source.HttpDataSource;
import com.example.vlik1234.tmdb.source.TMDBDataSource;


public class DetailsActivity extends ActionBarActivity implements DataManager.Callback<Film> {

    private FilmProcessor mFilmProcessor = new FilmProcessor();
    String id_film = "";
    TextView error ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        final HttpDataSource dataSource = getHttpDataSource();
        final FilmProcessor processor = getProcessor();

        error = (TextView) findViewById(R.id.error);
        MainActivity mainActivity = getIntent().getParcelableExtra("MainActivity");
        this.id_film = mainActivity.selectItemID;
        error.setText(id_film);
        update(dataSource, processor);
    }
    private FilmProcessor getProcessor() {
        return mFilmProcessor;
    }

    private HttpDataSource getHttpDataSource() {
        return  new TMDBDataSource();
    }

    private void update(HttpDataSource dataSource, FilmProcessor processor) {
        DataManager.loadData(DetailsActivity.this,
                getUrl(),
                dataSource,
                processor);
    }

    private String getUrl() {
        return ApiTMDB.getMovie(id_film);
    }

    @Override
    public void onDataLoadStart() {

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onDone(Film data) {

                    Film item = data;

                    TextView title = (TextView) findViewById(R.id.title);
                    title.setText(item.getTitle());
                    TextView overview = (TextView) findViewById(R.id.overview);
                    overview.setText(item.getOverview());

                    final ImageView imageView = (ImageView) findViewById(R.id.poster);
                    final String url = item.getPosterPath();
                    imageView.setImageBitmap(null);
                    imageView.setTag(url);
                    if (!TextUtils.isEmpty(url)) {
                        //TODO add delay and cancel old request or create limited queue
                        //TODO create sync Map to check existing request and existing callbacks
                        //TODO create separate thread pool for manager
                        DataManager.loadData(new DataManager.Callback<Bitmap>() {
                            @Override
                            public void onDataLoadStart() {

                            }

                            @Override
                            public void onDone(Bitmap bitmap) {
                                if (url.equals(imageView.getTag())) {
                                    imageView.setImageBitmap(bitmap);
                                }
                            }

                            @Override
                            public void onError(Exception e) {

                            }

                        }, url, new TMDBDataSource(), new BitmapProcessor());
                    }

                }




    @Override
    public void onError(Exception e) {
        e.printStackTrace();
        error.setText(e.getMessage());

    }

}
